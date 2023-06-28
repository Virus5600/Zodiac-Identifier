package com.satch_navida.zodiacidentifier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.satch_navida.zodiacidentifier.validation.Validator;
import com.satch_navida.zodiacidentifier.validation.rules.Date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

	// PUBLIC OBJECTS
	public DatePicker birthDateInput;
	public Button selectDateBtn, submitBtn;
	public Map<String, TextView> validationErrMsg;
	public TextView zodiacSign, zodiacTraits;

	/**
	 * The accepted date format for the Date validation. This is used to set a constant and reusable
	 * format for the entire application.
	 */
	static final public String DATE_FORMAT = Date.FORMATS[4];

	// PROTECTED OBJECTS
	protected DatePickerDialog datePickerDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// CODE START
		this.initializeObjects();
		this.initializeValues();
	}

	// PRIVATE METHODS

	/**
	 * Initializes class variables as needed.
	 */
	private void initializeObjects() {
		this.selectDateBtn = findViewById(R.id.dateSelect);
		this.submitBtn = findViewById(R.id.submit_btn);

		this.zodiacSign = findViewById(R.id.zodiacSign);
		this.zodiacTraits = findViewById(R.id.zodiacTraits);

		validationErrMsg = new HashMap<String, TextView>() {
			{
				this.put(getElementId(findViewById(R.id.birth_date_value_error_msg)), findViewById(R.id.birth_date_value_error_msg));
			}
		};
	}

	/**
	 * Initializes the values of some objects within the context.
	 */
	private void initializeValues() {
		this.selectDateBtn.setOnClickListener((e) -> {
			selectDate();
		});

		this.submitBtn.setOnClickListener((e) -> {
			onSubmit();
		});

		// Sets the validation error message texts' content to none and its color similar to Bootstrap's danger color.
		this.validationErrMsg.forEach((String key, TextView val) -> {
			val.setText("");
			val.setTextColor(0xffdc6545);
		});
	}

	/**
	 * Opens the date selection picker to allow users to select their birth dates.
	 */
	private void selectDate() {
		if (this.datePickerDialog == null) {
			this.datePickerDialog = new DatePickerDialog(MainActivity.this);

			this.birthDateInput = datePickerDialog.getDatePicker();
			this.birthDateInput.setId(R.id.birth_date_value);

			this.datePickerDialog.setTitle(R.string.date_label);
			this.datePickerDialog.create();
		}
		else if (this.birthDateInput == null) {
			this.birthDateInput = datePickerDialog.getDatePicker();
		}
		else {
			int day = this.birthDateInput.getDayOfMonth();
			int month = this.birthDateInput.getMonth();
			int year = this.birthDateInput.getYear();

			this.datePickerDialog.onDateChanged(this.birthDateInput, year, month, day);
		}

		this.birthDateInput.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int month, int day) {
				String date = String.format("%02d", month + 1) + "/" + String.format("%02d", day) + "/" + year;

				DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
				java.util.Date d = null;
				try {
					d = new SimpleDateFormat("MM/dd/yyyy").parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (d == null)
					selectDateBtn.setText(date);
				else
					selectDateBtn.setText(df.format(d));
			}
		});

		this.datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_NEGATIVE) {
					birthDateInput = null;
					selectDateBtn.setText(R.string.date_placeholder);
				}
			}
		});

		datePickerDialog.show();
	}

	/**
	 * Actions to be done when the submit button is clicked.
	 */
	private void onSubmit() {
		String date = this.selectDateBtn.getText().toString();
		String bdiID = getResources().getResourceEntryName(R.id.birth_date_value);

		if (this.birthDateInput != null) {
			int day = this.birthDateInput.getDayOfMonth();
			int month = this.birthDateInput.getMonth() + 1;
			int year = this.birthDateInput.getYear();

			date = String.format(Locale.US, "%02d", month) + "/" + String.format(Locale.US, "%02d", day) + "/" + year;
			bdiID = getElementId(birthDateInput);
		}

		String[] ids = new String[] {
				bdiID
		};

		// If the date is between 1950 to 2999, then use the current date value. Otherwise use the null to fail the "Required" rule.
		final String fDate = date.matches("^([0-3]\\d)([-/])([0-3]\\d)([-/])(19\\d{2}|2\\d{3})$") ? date : "";
		/**
		 * The values from the form. They'll be identified using their respective IDs.
		 */
		Map<String, Object> values = new HashMap<String, Object>() {
			{
				this.put(ids[0], fDate);
			}
		};

		/**
		 * Validation rules for the weight and height input fields.
		 */
		Map<String, String[]> rules = new HashMap<String, String[]>() {
			{
				this.put(ids[0], new String[] {"Required", "Date:" + MainActivity.DATE_FORMAT});
			}
		};

		/**
		 * Custom validation messages for each rule. This will only be shown if the rules failed.
		 */
		Map<String, String> messages = new HashMap<String, String>() {
			{
				// Birth Date Validation Messages
				this.put(String.format("%1$s.%2$s", ids[0], "Required"), "The birth date is required.");
				this.put(String.format("%1$s.%2$s", ids[0], "Date"), "Birth date should be a date.");
			}
		};

		Validator validator = new Validator(values, rules, messages);

		// If the validation failed...
		Map<String, Object> validatedFields = validator.validate();

		if (validator.fails()) {
			// Iterate through the invalid fields...
			for (String field : validator.invalidFields())
				this.validationErrMsg.get(field + "_error_msg").setText(validator.errors().first(field));

			// Iterate through the valid fields and remove their error message
			for (String field : validator.validFields())
				this.validationErrMsg.get(field + "_error_msg").setText("");

			// Then stop all process from this function.
			return;
		}
		// Iterate through the valid fields and remove their error message
		for (String field : validator.validFields())
			this.validationErrMsg.get(field + "_error_msg").setText("");

		// If the validation succeeded, proceed to fetch the zodiac sign and their qualities.
		Zodiac z = new Zodiac(this, date);
		String traits = "";

		// Displays them...
		this.zodiacSign.setText(String.format("%1$s (%2$s)", z.getSign(), date));
		for (String trait : z.getTraits())
			traits += String.format("\n◉ %1$s", trait);
		this.zodiacTraits.setText(traits.trim());
		return;
	}

	/**
	 * Fetches the provided string ID of an element.
	 *
	 * @param element The element in question.
	 *
	 * @return String The string ID of the provided {@link View} element.
	 */
	private String getElementId(View element) {
		return getResources().getResourceEntryName(element.getId());
	}

	/**
	 * Fetches the provided string of an element ID.
	 *
	 * @param id The element ID in question.
	 *
	 * @return String The string value of the provided element ID.
	 */
	private String getResourceString(int id) {
		return getResources().getString(id);
	}
}