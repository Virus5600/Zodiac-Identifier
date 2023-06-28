package com.satch_navida.zodiacidentifier.validation;

import android.util.Log;

import com.satch_navida.zodiacidentifier.validation.rules.Rule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Validator {
	// PRIVATE VARIABLES
	/**
	 * A private global variable container for all the values passed to this.
	 */
	private final Map<String, Object> valueList;
	/**
	 * A private global variable container for all the rules passed to this.
	 */
	private final Map<String, String[]> ruleList;
	/**
	 * A private global variable container for all the messages passed to this.
	 */
	private final Map<String, String> msgList;
	/**
	 * A private global variable container for the {@link MessageBag} instance.
	 */
	private final MessageBag errorList = new MessageBag();
	/**
	 * A private global variable container for all the valid key-value pairs.
	 */
	private final Map<String, Object> validSets = new HashMap<String, Object>();
	/**
	 * A private global variable container that determines if the validation has been run already.
	 */
	private boolean validationDone = false;
	/**
	 * A private global variable container, identifying if this validator's validation has failed.
	 */
	private boolean failed = false;
	/**
	 * A private global variable container, which will be used as a container for all the already validated keys.
	 */
	private final ArrayList<String> validated = new ArrayList<String>();

	// CONSTRUCTORS

	/**
	 * Creates an instance of {@code Validator}.<br>
	 *
	 * @param values A {@link Map} object containing a key-value pair for the keys and its value.
	 * @param rules A {@link Map} object containing a key-value pair for the key's rules. A rule
	 *                 must be one of the classes that extends {@link Rule} class.
	 * @param messages A {@link Map} object containing a key-value pair for the keys' messages
	 */
	public Validator(Map<String, Object> values, Map<String, String[]> rules, Map<String, String> messages) {
		this.valueList = values;
		this.ruleList = rules;
		this.msgList = messages;
	}

	/**
	 * Creates an instance of {@code Validator}.<br>
	 *
	 * @param values A {@link Map} object containing a key-value pair for the keys and its value.
	 * @param rules A {@link Map} object containing a key-value pair for the key's rules. A rule
	 *                 must be one of the classes that extends {@link Rule} class.
	 */
	public Validator (Map<String, Object> values, Map<String, String[]> rules) {
		this(values, rules, null);
	}

	// PUBLIC METHODS
	/**
	 * Identifies whether the validation failed or not. A single rule that fail will result in a
	 * total fail of the validation.
	 *
	 * @return boolean Returns {@code true} if the validation fails; {@code false} otherwise.
	 */
	public boolean fails() {
		if (!this.validationDone)
			this.runValidation();

		return this.failed;
	}

	/**
	 * Fetches all the error message.
	 *
	 * @return MessageBag An instance of {@link MessageBag} containing all error messages.
	 *
	 * @see MessageBag
	 */
	public MessageBag errors() {
		if (!this.validationDone)
			this.runValidation();

		return this.errorList;
	}

	/**
	 * Validates the fields provided and returns a JSON object, containing all the values from the
	 * fields that passed.
	 *
	 * @return Map<String, Object> A {@link Map} object, containing a key-value pair format of keys
	 * and their values that passed the validation.
	 */
	public Map<String, Object> validate() {
		if (!this.validationDone)
			this.runValidation();

		return this.validSets;
	}

	/**
	 * Determine if messages exist for the given field.
	 *
	 * @param key field	The name of the key that will be tested.
	 *
	 * @return boolean A boolean value; either a {@code true} if the field exists, or {@code false} if it does not.
	 */
	public boolean has(String key) {
		return this.valueList.containsKey(key);
	}

	/**
	 * Get the first message from the message bag for a given field.
	 *
	 * @param key The name of the key that the message will be fetched upon.
	 *
	 * @return String The first message contained within the said field. Returns a {@code null} when the field does not exists.
	 */
	public String first(String key) {
		return this.errorList.first(key);
	}

	/**
	 * Get all of the messages from the message bag for a given field. If no value or a {@code null} is
	 * provided to the field, it returns all the messages contained within the current instance of
	 * {@link MessageBag}.
	 *
	 * @param key The name of the key that the message will be fetched upon.
	 *
	 * @return ArrayList<String> An {@link ArrayList} object that contains the key-value pair for all
	 * the message of the field.
	 */
	public ArrayList<String> get(String key) {
		return new ArrayList<String>(this.errorList.get(key).values());
	}

	/**
	 * Retrieves an array of field names that are invalid.
	 *
	 * @return String[] An array of field names that failed the validation.
	 */
	public String[] invalidFields() {
		return this.errorList.keys();
	}

	/**
	 * Retrieves an array of field names that are valid.
	 *
	 * @return String[] An array of field names that passed the validation.
	 */
	public String[] validFields() {
		Map<String, Object> vl = new HashMap<String, Object>();
		vl.putAll(this.valueList);

		vl.keySet().removeAll(new HashSet<String>(Arrays.asList(this.invalidFields())));

		return Arrays.copyOf(vl.keySet().toArray(), vl.keySet().size(), String[].class);
	}

	/**
	 * Retrieves all the field names that are used in this validator.
	 *
	 * @return String[] An array of all the field names used in this {@code Validator}.
	 */
	public String[] fields() {
		return (String[]) this.valueList.keySet().toArray();
	}

	// PRIVATE METHODS
	/**
	 * Runs the entire validation algorithm.
	 */
	private void runValidation() {
		// Iterate through the list of values provided.
		this.valueList.forEach((String field, Object value) -> {
			// Fetches the rules for iteration...
			String[] rules = this.ruleList.get(field);
			// Then iterates over them using for-each
			for (String rule : rules) {
				String[] validatorValues = rule.split(":");
				// Fetches the rule
				rule = validatorValues[0];
				// And if there are validator values present, overwrite it to the values variable
				if (validatorValues.length > 1)
					validatorValues = Arrays.copyOfRange(validatorValues, 1, validatorValues.length);
				else
					validatorValues = null;

				try {
					// Fetches the class dynamically using the rule name.
					Class clazz = Class.forName(this.getClass().getPackageName() + ".rules." + rule);
					// Then build the key for fetching the validation message for the current rule.
					String msgKey = String.format("%1$s.%2$s", field, rule);

					/**
					 * Fetch the constructor first using the dynamically acquired class, then proceed
					 * to create a new instance under it super class "Rule".
					 */
					Rule r = (Rule) clazz.getDeclaredConstructor(
							String.class,
							Object.class,
							String.class,
							Object[].class)
							.newInstance(
									field,
									this.valueList.get(field),
									this.msgList.get(msgKey),
									validatorValues
							);

					/*
					Call the .validate() method for the subclass of Rule using the dynamically acquired
					class from earlier. This will allow us to validate the values respectively.
					 */
					HashMap<String, Object> response = (HashMap<String, Object>) r.getClass()
							.asSubclass(clazz)
							.getMethod("validate")
							.invoke(r);

					// If the rule failed, immediately fail the entire validation.
					if (!(boolean) response.get("valid")) {
						this.failed = true;
						// Insert the error message to the message bag.
						this.errorList.add(msgKey, (String) response.get("message"));

						/*
						If the response returned a false value for "runOtherValidation", skip the entire
						field and proceed to the next field.
						 */
						if (!(boolean) response.get("runOtherValidation"))
							continue;
					}
					else {
						// Add the field to the list of validated inputs.
						this.validSets.put(field, value);
					}
				} catch (ClassNotFoundException e) {
					Log.e("ERROR", e.getMessage(), e);
				} catch (NoSuchMethodException e) {
					Log.e("ERROR", e.getMessage(), e);
				} catch (InvocationTargetException e) {
					Log.e("ERROR", e.getMessage(), e);
				} catch (IllegalAccessException e) {
					Log.e("ERROR", e.getMessage(), e);
				} catch (InstantiationException e) {
					Log.e("ERROR", e.getMessage(), e);
				}
			}
		});

		// Marks this instance as validation done.
		if (!this.validationDone)
			this.validationDone = true;
	}
}
