package com.satch_navida.zodiacidentifier.validation.rules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.HashMap;

/**
 * Tests whether the {@code value} provided is no less than the provided validator value.
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Min extends Rule implements RuleInterface {

	// OVERRIDE VARIABLES
	protected String message = "The :key must be at least :val";

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
	public Min(@NotNull String key, @NotNull Object value, @Nullable String message, @Nullable Object[] validatorValues) {
		super(key, value, message, validatorValues);
	}

	// PUBLIC METHOD

	@Override
	/**
	 * {@inheritDoc}
	 */
	public HashMap<String, Object> validate() throws Exception {
		Exception exception = null;

		// Checks whether the values passed (including the validator value) are numeric. If not, then throws an error
		if (this.validatorValues.length < 1) {
			exception = new IllegalArgumentException(MessageFormat.format("Not enough validator value passed:\tNeeds 1, only {0} is passed.", this.validatorValues.length));
		}
		else if (!java.lang.Boolean.parseBoolean(
				new Numeric("min_value", this.validatorValues[0], null, null)
						.validate()
						.get(Rule.VALIDATED_KEYS[0])
						.toString() )) {
			exception = new IllegalArgumentException(MessageFormat.format("Incorrect validator value type:\tThe provided validator value ({0}) is not a number", this.validatorValues[0]));
		}

		// Throws an error if an exception is present
		if (exception != null)
			throw exception;

		if (java.lang.Boolean.parseBoolean(
				new Numeric(this.key, this.value, null, null)
						.validate()
						.get(Rule.VALIDATED_KEYS[0])
						.toString() )) {
			double val = Double.parseDouble(this.value.toString());
			double min = Double.parseDouble(this.validatorValues[0].toString());

			if (val >= min) {
				this.setValid(true);
			}
		}

		return new HashMap<String, Object>() {
			{
				this.put(Rule.VALIDATED_KEYS[0], isValid());
				this.put(Rule.VALIDATED_KEYS[1], isValid() ? "" : getFinalMessage());
				this.put(Rule.VALIDATED_KEYS[2], getRunOtherValidations());
			}
		};
	}

	// PROTECTED

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFinalMessage() {
		String finalMsg = super.getFinalMessage()
				.replaceAll("(:min)", this.validatorValues[0].toString());

		return finalMsg;
	}
}
