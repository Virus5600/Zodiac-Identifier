package com.satch_navida.zodiacidentifier.validation.rules;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Tests whether the value is null when no other value is provided.
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Nullable extends Rule implements RuleInterface {

	// OVERRIDE VARIABLES
	protected String message = "The :key field can be null";

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
	public Nullable(@NotNull String key, @NotNull Object value, @org.jetbrains.annotations.Nullable String message, @org.jetbrains.annotations.Nullable Object[] validatorValues) {
		super(key, value, message, validatorValues);
	}

	// PUBLIC METHOD

	@Override
	/**
	 * {@inheritDoc}
	 */
	public HashMap<String, Object> validate() {
		if (this.value == null)
			this.setValid(true);
		else if (this.value.toString().isEmpty())
			this.setValid(false);

		return new HashMap<String, Object>() {
			{
				this.put(Rule.VALIDATED_KEYS[0], isValid());
				this.put(Rule.VALIDATED_KEYS[1], isValid() ? "" : getFinalMessage());
				this.put(Rule.VALIDATED_KEYS[2], getRunOtherValidations());
			}
		};
	}
}
