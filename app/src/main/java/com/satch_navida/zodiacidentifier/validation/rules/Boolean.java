package com.satch_navida.zodiacidentifier.validation.rules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;

/**
 * Tests whether the value is a boolean or can be converted to a boolean.
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Boolean extends Rule implements RuleInterface {

	// OVERRIDE VARIABLES
	protected String message = "The :key must either be true or false";

	// CONSTRUCTORS
	/**
	 * Creates an instance of {@link Rule}, containing all the necessary parameters: the
	 * {@code key} and {@code value}, and optional parameters: {@code message} and {@code validatorValues}.<br>
	 *
	 * @param key A unique identifier (ID) of value being tested.
	 * @param value The value that will be tested.
	 * @param message A message that will be displayed when the test fails.
	 * @param validatorValues An array of {@link Object}s that will be used to test against the {@code value}.
	 */
	public Boolean(@NotNull String key, @NotNull Object value, @Nullable String message, @Nullable Object[] validatorValues) {
		super(key, value, message, validatorValues);
	}

	// PUBLIC METHOD

	@Override
	/**
	 * {@inheritDoc}
	 */
	public HashMap<String, Object> validate() {
		String[] matches = new String[]{"0", "1", "true", "false", "on", "off"};

		if (Arrays.stream(matches).anyMatch(Predicate.isEqual(this.value.toString().toLowerCase())))
			this.setValid(true);

		return new HashMap<String, Object>() {
			{
				this.put(Rule.VALIDATED_KEYS[0], isValid());
				this.put(Rule.VALIDATED_KEYS[1], isValid() ? "" : getFinalMessage());
				this.put(Rule.VALIDATED_KEYS[2], getRunOtherValidations());
			}
		};
	}
}
