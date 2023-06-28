package com.satch_navida.zodiacidentifier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.satch_navida.zodiacidentifier.validation.Validator;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

	//PUBLIC OBJECTS
	public DatePicker birthDateInput;
	public Button selectDateBtn, submitBtn;
	public Map<String, TextView> validationErrMsg;

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
		DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this);

		this.birthDateInput = datePickerDialog.getDatePicker();

		datePickerDialog.setTitle(R.string.date_label);
		datePickerDialog.create();
		datePickerDialog.show();
	}

	/**
	 * Actions to be done when the submit button is clicked.
	 */
	private void onSubmit() {
		int day = birthDateInput.getDayOfMonth();
		int month = birthDateInput.getMonth() + 1;
		int year = birthDateInput.getYear();
		String date = String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + year;

		String[] ids = new String[] {
				getElementId(birthDateInput)
		};

		/**
		 * The values from the form. They'll be identified using their respective IDs.
		 */
		Map<String, Object> values = new HashMap<String, Object>() {
			{
				this.put(ids[0], date);
			}
		};

		/**
		 * Validation rules for the weight and height input fields.
		 */
		Map<String, String[]> rules = new HashMap<String, String[]>() {
			{
				this.put(ids[0], new String[] {"Required", "Date:MM/dd/yyyy"});
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
		else {
			for (String field : validator.validFields())
				this.validationErrMsg.get(field + "_error_msg").setText("");
		}

		// If the validation succeeded, proceed to fetch the zodiac sign and their qualities.
		double weight = Double.parseDouble(validatedFields.get(ids[0]).toString()),
				height = Double.parseDouble(validatedFields.get(ids[1]).toString());
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