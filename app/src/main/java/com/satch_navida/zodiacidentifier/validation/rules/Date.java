package com.satch_navida.zodiacidentifier.validation.rules;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.MessageFormat;
import java.util.HashMap;

/**
 * Tests whether the {@code value} provided is a valid date using one of the format provided below:<br>
 * <ul>
 *     <li>MM-dd-yyyy</li>
 *     <li>MM-dd-yy</li>
 *     <li>dd-MM-yyyy</li>
 *     <li>dd-MM-yy</li>
 *     <li>MM/dd/yyyy</li>
 *     <li>MM/dd/yy</li>
 *     <li>dd/MM/yyyy</li>
 *     <li>dd/MM/yy</li>
 * </ul>
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Date extends Rule implements RuleInterface {

	// OVERRIDE VARIABLES
	protected String message = "The :key must be between a valid date";

	// PUBLIC VARIABLES
	/**
	 * Contains all the formats currently supported by the {@link Date} rule.
	 */
	public static final HashMap<String, Character> FORMATS = new HashMap<String, Character>() {
		{
			this.put("MM-dd-yyyy", '-');
			this.put("MM-dd-yy", '-');
			this.put("dd-MM-yyyy", '-');
			this.put("dd-MM-yy", '-');
			this.put("MM/dd/yyyy", '/');
			this.put("MM/dd/yy", '/');
			this.put("dd/MM/yyyy", '/');
			this.put("dd/MM/yy", '/');
		}
	};

	/**
	 * Creates an instance of {@link Rule}, containing all the necessary parameters: the
	 * {@code key} and {@code value}, and optional parameters: {@code message} and {@code validatorValues}.<br>
	 * <br>
	 * <b>NOTE:</b> A format must be provided to the {@code validatorValues} in order to validate the date. This
	 * due to Java's limitations.
	 *
	 * @param key A unique identifier (ID) of value being tested.
	 * @param value The value that will be tested.
	 * @param message A message that will be displayed when the test fails.
	 * @param validatorValues An array of {@link Object}s that will be used to test against the {@code value}.
	 */
	public Date(@NonNull String key, @NonNull Object value, @Nullable String message, @Nullable Object[] validatorValues) {
		super(key, value, message, validatorValues);
	}

	// PUBLIC METHOD

	@Override
	/**
	 * {@inheritDoc}
	 */
	public HashMap<String, Object> validate() throws IllegalArgumentException {
		// Checks whether the values passed (including the validator value) are numeric. If not, then throws an error
		if (this.validatorValues.length < 1)
			throw new IllegalArgumentException(MessageFormat.format(
					"Not enough validator value passed: Needs 1, only {0} passed.",
					this.validatorValues.length
			));

		// Split the date into their respective slices
		String[] date = ((String) this.value).split(Date.FORMATS.get((String) validatorValues[0]).toString());

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
				.replaceAll("(:format)", this.validatorValues[0].toString());

		return finalMsg;
	}
}
