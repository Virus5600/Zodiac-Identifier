package com.satch_navida.zodiacidentifier.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * A message bag is an instance of object containing various messages for different keys. It is
 * similar to a JSON object wherein you can provide an array of messages for a single key, with
 * multiple keys in a single instance of the object.<br>
 * <br>
 * The code of this {@code MessageBag} is designed to allow chain-calling and only stops when using
 * {@code getters} or calling {@code variables} (if there are any).<br>
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class MessageBag {
	// PRIVATE VARIABLES
	/**
	 * A map that contains all the messages, serving as a replacement for JSONs as JSON aren't easily
	 * traversable unlike maps in the Java context.
	 */
	private Map<String, Map<String, String>> messages;

	// CONSTRUCTORS

	/**
	 * Creates an instance of {@link MessageBag}, containing messages given through {@code messages}
	 * parameter.<br>
	 *
	 * @param messages A map containing key-value pair whereas the {@code key} is the identifier while
	 *                 {@code value} is a {@link Map} containing a key-value pair of {@code String}s where in this
	 *                 context, the {@code key} is the rule while the {@code value} is the message.
	 */
	public MessageBag(HashMap<String, Map<String, String>> messages) {
		this.messages = messages;
	}

	/**
	 * Creates an empty instance of {@link MessageBag}.
	 */
	public MessageBag() {
		this(new HashMap<String, Map<String, String>>());
	}

	// PUBLIC METHODS

	/**
	 * Fetches all the available keys in the {@link MessageBag}.
	 *
	 * @return String[] An array of keys.
	 */
	@SuppressWarnings("ConstantConditions")
	public String[] keys() {
		Object[] keys = this.messages.keySet().toArray();
		return Arrays.copyOf(keys, keys.length, String[].class);
	}

	/**
	 * Adds a new message to the array of messages from a key. If the key isn't present yet,
	 * a new instance of array will be created wherein the new message will be placed.
	 *
	 * @param key Key identifier of the message.
	 * @param message The new message to put for the key.
	 *
	 * @return MessageBag This instance of {@code MessageBag}.
	 */
	public MessageBag add(String key, String message) {
		String rule = key.split("\\.")[1];
		key = key.split("\\.")[0];

		// If no such key exists in the current message
		if (!this.messages.containsKey(key))
			this.messages.put(key, new HashMap<String, String>());

		Map<String, String> currentMessages = this.messages.get(key);

		// Checks if the rule exists or if it is empty. If one of those was true, the message will be placed.
		if (currentMessages.containsKey(rule)) {
			if (currentMessages.get(rule).isEmpty()) {
				currentMessages.put(rule, message);
			}
		}
		else {
			currentMessages.put(rule, message);
		}

		// Then finalizes it by inserting it to the messages map.
		this.messages.put(key, currentMessages);

		return this;
	}

	/**
	 * Merge a new array of messages into this message bag.
	 *
	 * @param messages An instance of {@link HashMap} containing a key-value pair whereas the {@code key} is the identifier while
	 *                  {@code value} is an {@link ArrayList} containing a list of {@code String}s.
	 *
	 * @return MessageBag This instance of {@code MessageBag}.
	 */
	public MessageBag merge(HashMap<String, Map<String, String>> messages) {
		return this.merge(new MessageBag(messages));
	}

	/**
	 * Merge all {@link MessageBag} into this single instance. The last parameter supplied to {@code messages}
	 * will be used and will overwrite all messages before it if same key and
	 *
	 * @param messages An instance of {@link MessageBag} containing all the messages that will be
	 *                 merged with this {@code MessageBag}.
	 *
	 * @return MessageBag This instance of {@code MessageBag}.
	 */
	public MessageBag merge(MessageBag... messages) {
		ConcurrentMap<String, Map<String, String>> cms = (ConcurrentMap<String, Map<String, String>>) Map.copyOf(this.messages);

		// Iterate through the message bags
		for (MessageBag m : messages) {
			// Iterate through each key-value (String - Map<>) pairs
			m.get().entrySet().parallelStream().forEach(pair1 -> {
				// Merges the provided pair to the message var of this instance
				cms.merge(pair1.getKey(), pair1.getValue(), (v1, v2) -> {
					// Puts all the contents of the new message bag to this instance
					v1.putAll(v2);
					return v1;
				});
			});
		}

		this.messages = cms;

		return this;
	}

	/**
	 * Fetches the first message within the given {@code key}. If no such {@code key} is present, a
	 * empty string will be returned. Likewise, if a {@code key} is present but has no existing values,
	 * an empty string will also be returned.
	 *
	 * @param key Key identifier of the message.
	 *
	 * @return String the very first message in the list of the given key.
	 */
	public String first(String key) {
		if (!this.messages.containsKey(key))
			return "";

		Map<String, String> keyMessages = this.messages.get(key);

		if (keyMessages == null) {
			return "";
		}
		else {
			String[] subkeys = Arrays.copyOf(keyMessages.keySet().toArray(), keyMessages.keySet().size(), String[].class);

			if (subkeys.length > 0)
				return keyMessages.get(subkeys[0]);
			else
				return "";
		}
	}

	public String get(String key, int index) {
		if (!this.messages.containsKey(key))
			return "";

		Map<String, String> keyMessage = this.messages.get(key);

		if (index >= keyMessage.keySet().size())
			throw new IndexOutOfBoundsException("Provided index is greater than the number of messages present.");

		return keyMessage.get(keyMessage.keySet().toArray()[index]);
	}

	/**
	 * Fetches a list of messages based on the provided key.
	 *
	 * @param key The key used to fetch the messages.
	 *
	 * @return ArrayList<String> The list of messages.
	 */
	public Map<String, String> get(String key) {
		return this.messages.get(key);
	}

	public String get(String key, String subkey) {
		return this.messages.get(key).get(subkey);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return
	 */
	@Override
	public String toString() {
		String toRet = "{";

		for (String key : this.messages.keySet())
			toRet += String.format("%1$s: \"%2$s\", ", key, this.messages.get(key));

		if (toRet.length() > 3)
			toRet = toRet.substring(0, toRet.length() - 2);

		toRet += "}";

		return toRet;
	}

	// PROTECTED METHODS

	/**
	 * Fetches and clone the message's raw form (a {@link HashMap}).
	 *
	 * @return HashMap<String, ArrayList<String>> A clone of the original map used by the
	 * {@link #messages} variable.
	 */
	@SuppressWarnings("unchecked")
	protected HashMap<String, Map<String, String>> get() {
		return (HashMap<String, Map<String, String>>) Map.copyOf(this.messages);
	}
}
