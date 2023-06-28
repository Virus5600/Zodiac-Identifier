package com.satch_navida.zodiacidentifier.validation.rules;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Tests whether the {@code value} provided is a valid date using one of the format from {@link #FORMATS}
 * constant.
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Date extends Rule implements RuleInterface {

	// OVERRIDE VARIABLES
	protected String message = "The :key must be between a valid date";

	// PUBLIC VARIABLES
	/**
	 *  Contains all the currently supported formats for the {@link Date} rule.<br>
	 *  <br>
	 *  All currently supported formats are:<br>
	 *  <table border="1" align="center">
	 *      <thead>
	 *          <tr>
	 *              <th>Index</th>
	 *              <th>&nbsp;&nbsp;&nbsp;&nbsp;</th>
	 *              <th>Format</th>
	 *          </tr>
	 *      </thead>
	 *
	 *      <tbody>
	 *          <tr>
	 *              <td align="right">0</td>
	 *              <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 *              <td align="left">MM-dd-yyyy</td>
	 *          </tr>
	 *
	 *           <tr>
	 *              <td align="right">1</td>
	 *              <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 *              <td align="left">MM-dd-yy</td>
	 *          </tr>
	 *
	 *          <tr>
	 *              <td align="right">2</td>
	 *              <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 *              <td align="left">dd-MM-yyyy</td>
	 *          </tr>
	 *
	 *          <tr>
	 *              <td align="right">3</td>
	 *              <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 *              <td align="left">dd-MM-yy</td>
	 *          </tr>
	 *
	 *          <tr>
	 *              <td align="right">4</td>
	 *              <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 *              <td align="left">MM/dd/yyyy</td>
	 *          </tr>
	 *
	 *          <tr>
	 *              <td align="right">5</td>
	 *              <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 *              <td align="left">MM/dd/yy</td>
	 *          </tr>
	 *
	 *          <tr>
	 *              <td align="right">6</td>
	 *              <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 *              <td align="left">dd/MM/yyyy</td>
	 *          </tr>
	 *
	 *          <tr>
	 *              <td align="right">7</td>
	 *              <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 *              <td align="left">dd/MM/yy</td>
	 *          </tr>
	 *      </tbody>
	 *  </table>
	 */
	public static final String[] FORMATS = new String[] {
			"MM-dd-yyyy",
			"MM-dd-yy",
			"dd-MM-yyyy",
			"dd-MM-yy",
			"MM/dd/yyyy",
			"MM/dd/yy",
			"dd/MM/yyyy",
			"dd/MM/yy"
	};

	// PRIVATE VARIABLES
	/**
	 * Contains all the format splitters for the currently supported formats.
	 */
	private static final HashMap<String, String> FORMAT_SPLITTER = new HashMap<String, String>() {
		{
			this.put("MM-dd-yyyy", "-");
			this.put("MM-dd-yy", "-");
			this.put("dd-MM-yyyy", "-");
			this.put("dd-MM-yy", "-");
			this.put("MM/dd/yyyy", "/");
			this.put("MM/dd/yy", "/");
			this.put("dd/MM/yyyy", "/");
			this.put("dd/MM/yy", "/");
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
	public HashMap<String, Object> validate() throws Exception {
		// Checks whether the values passed (including the validator value) are numeric. If not, then throws an error
		if (this.validatorValues.length < 1)
			throw new IllegalArgumentException(MessageFormat.format(
					"Not enough validator value passed: Needs 1, only {0} passed.",
					this.validatorValues.length
			));

		// Split the date into their respective slices
		final String splitter = Date.FORMAT_SPLITTER.get((String) this.validatorValues[0]);
		final String[] date = ((String) this.value).split(splitter);
		final String[] dateFormat = ((String) this.validatorValues[0]).split(splitter);

		if (date.length < 3) {
			Log.e("Validator", "[VALIDATION@DATE]", new Exception("Date format does not match the expected format."));
		}
		else {
			HashMap<String, String> valueOf = new HashMap<String, String>() {
				{
					this.put(dateFormat[0], date[0]);
					this.put(dateFormat[1], date[1]);
					this.put(dateFormat[2], date[2]);
				}
			};

			// Re-structure the date to match the static format used for validating the date
			String reDate = valueOf.get("MM") + "/" + valueOf.get("dd") + "/" + (valueOf.containsKey("yyyy") ? valueOf.get("yyyy") : valueOf.get("yy"));

			DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
			java.util.Date d = null;
			try {
				d = new SimpleDateFormat("MM/dd/yyyy").parse(reDate);
			} catch (ParseException e) {
				Log.e("DATE", "[Format: " + this.validatorValues[0] + ", Value: " + this.value);
			}

			// If date is a valid date (which results to a non-null value), then proceed to pass the rule.
			if (d != null) {
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
				.replaceAll("(:format)", this.validatorValues[0].toString());

		return finalMsg;
	}
}
