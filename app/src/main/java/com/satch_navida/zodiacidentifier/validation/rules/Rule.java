package com.satch_navida.zodiacidentifier.validation.rules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;

/**
 * The very base of any rules that will be used in the {@link com.satch_navida.myapplication.validation.Validator Validator} class.
 * This class is an abstract class as each instance of the {@code Rule} class' children has
 * their own unique conditions and thus, the abstractness of the design implemented.
 *
 * @author Virus5600
 * @version 1.0.0
 */
public abstract class Rule implements RuleInterface {
	// PRIVATE VARIABLES
	/**
	 * Identifies whether the validation is a success or not.
	 */
	private boolean valid = false;

	// PROTECTED VARIABLES
	/**
	 * Container for the name of the field that will be tested.
	 */
	protected String key;
	/**
	 * Container for the value that will be tested.
	 */
	protected Object value;
	/**
	 * Container for the message that will be displayed when the validation fails.
	 */
	protected String message = "The :key field is incorrect.";
	/**
	 * Contains values that will have a use for validation testing down the line.
	 */
	protected Object[] validatorValues;
	/**
	 * Identifies whether to run the other validations or stop at this specific one.
	 */
	protected boolean runOtherValidations = true;

	// PUBLIC VARIABLES
	/**
	 * A constant array of strings containing the keys of what the {@link #validate()} method should return.
	 */
	public static final String[] VALIDATED_KEYS = new String[]{"valid", "message", "runOtherValidation"};

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
	public Rule(@NotNull String key, @NotNull Object value, @Nullable String message, @Nullable Object[] validatorValues) {
		if (key == null)
			throw new NullPointerException("\"key\" should not be null.");
		if (value == null)
			throw new NullPointerException("\"value\" should not be null.");

		this.key = key;
		this.value = value;
		this.message = message == null ? this.message : message;
		this.validatorValues = validatorValues;
	}

	// PUBLIC METHODS

	/**
	 * {@inheritDoc}
	 *
	 * @throws AbstractMethodError When the {@code validate()} method isn't implemented
	 * within its child class, this error is thrown.
	 */
	@Override
	public HashMap<String, Object> validate() throws Exception {
		throw new AbstractMethodError("Unimplemented method: validate()");
	}

	/**
	 * Identifies whether the validator test is valid or invalid.
	 *
	 * @return boolean Returns {@code true} if the test passes; {@code false} otherwise, and if validation
	 * isn't run yet.
	 */
	public boolean isValid() {
		return this.valid;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString() {
		return String.format(
				"[%1$s]: {key: \"%2$s\", value: \"%3$s\", message: \"%4$s\", validatorValues: \"%5$s\"}",
				this.getClass().getSimpleName(),
				this.key,
				this.value,
				this.message,
				Arrays.toString(this.validatorValues)
		);
	}

	// PROTECTED METHODS

	/**
	 * Sets the validity value of this rule.<br>
	 * <ul>
	 *     <li><b>{@code true}</b> - If the validation succeeded.</li>
	 *     <li><b>{@code false}</b> - If the validation failed.</li>
	 * </ul>
	 *
	 * @param isValid
	 */
	protected void setValid(boolean isValid) {
		this.valid = isValid;
	}

	/**
	 * Injects the values for {@code :key}, {@code :val}, and other values like those at {@link #validatorValues}
	 * to the message provided, which is then returned as a {@link String}.<br>
	 * <br>
	 * <i><b>NOTE:</b> Could be overridden to include custom pseudo-attributes such as {@code :min} and {@code :max}.
	 * Just be sure to call the superclass' method as well to prevent redoing the code for its base injection.</i>
	 *
	 * @return String The transformed message, containing the injected values.
	 */
	protected String getFinalMessage() {
		String finalMsg = message.replaceAll("(:key)", key)
				.replaceAll("(:value)", value.toString());

		return finalMsg;
	}

	/**
	 * Sets the value for the {@link #runOtherValidations} variable.
	 *
	 * @param shouldRun A boolean value which dictates whether to continue the validation or skip all the others.
	 */
	protected void setRunOtherValidations(boolean shouldRun) {
		this.runOtherValidations = shouldRun;
	}

	/**
	 * Fetches the value of the {@link Required#runOtherValidations}. The default value of the said variable
	 * is set to {@code true}.
	 *
	 * @return boolean The value of the said variable.
	 */
	protected boolean getRunOtherValidations() {
		return this.runOtherValidations;
	}
}
