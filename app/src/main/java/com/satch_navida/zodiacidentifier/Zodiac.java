package com.satch_navida.zodiacidentifier;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that congregates all the needed functions to identify a specified date's Zodiac sign and
 * its qualities which are fetch from a 
 *
 * @author Virus5600
 * @version 1.0.0
 */
public class Zodiac {
	// PRIVATE VARIABLES
	private String date;
	private String sign;
	private Context ctx;

	// CONSTRUCTOR

	/**
	 * Creates an instance of {@link Zodiac} using the given date.
	 *
	 * @param date A valid date using the format {@link MainActivity#DATE_FORMAT}.
	 */
	public Zodiac(Context ctx, String date) {
		boolean isParsed = false;
		try {
			this.date = new SimpleDateFormat(MainActivity.DATE_FORMAT).format(new SimpleDateFormat(MainActivity.DATE_FORMAT).parse(date));
			isParsed = true;
		} catch (ParseException e) {
			Log.e("ZODIAC", "Date Parsing", e);
		}

		if (!isParsed) {
			this.date = this.date = DateTimeFormatter.ofPattern(MainActivity.DATE_FORMAT).format(LocalDate.now());
		}

		this.ctx = ctx;
		this.getZodiac();
	}

	/**
	 * Creates an instance of {@link Zodiac} using the current date.
	 */
	public Zodiac(Context ctx) {
		new Zodiac(
				ctx,
				DateTimeFormatter
						.ofPattern(MainActivity.DATE_FORMAT)
						.format(LocalDate.now())
		);
	}

	// PUBLIC METHODS

	/**
	 * Fetches the trait of this zodiac based on the date provided.
	 *
	 * @return A string array containing the traits.
	 */
	public String[] getTraits() {
		String[] toRet = {};

		try {
			 toRet = ctx
					 .getResources()
					 .getStringArray(R.array.class.getField(this.sign)
							 .getInt(null)
					 );
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return toRet;
	}

	/**
	 * Get the sign attributed to this zodiac instance based on the date provided.
	 *
	 * @return A string value, which is the zodiac sign of the given date.
	 */
	public String getSign() {
		return this.sign;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format(
				"[Sign: \"%1$s\"; Date: \"%2$s\"]",
				this.sign,
				this.date
		);
	}

	// PRIVATE METHODS
	private void getZodiac() {
		String splitter = String.valueOf(MainActivity.DATE_FORMAT
				.replaceAll("[a-zA-Z]", "")
				.charAt(0));
		String[] keys = MainActivity.DATE_FORMAT.split(splitter);
		String[] values = this.date.split(splitter);
		Map<String, Integer> valueOf = new HashMap<String, Integer>();

		for (int i = 0; i < Math.min(keys.length, values.length); i++) {
			valueOf.put(keys[i], Integer.parseInt(values[i]));
		}

		// January
		if (valueOf.get("MM") == 1) {
			if (valueOf.get("dd") >= 20)
				this.sign = "Aquarius";
			else
				this.sign = "Capricorn";
		}
		// February
		else if (valueOf.get("MM") == 2) {
			if (valueOf.get("dd") >= 18)
				this.sign = "Pisces";
			else
				this.sign = "Aquarius";
		}
		// March
		else if (valueOf.get("MM") == 3) {
			if (valueOf.get("dd") >= 20)
				this.sign = "Aries";
			else
				this.sign = "Pisces";
		}
		// April
		else if (valueOf.get("MM") == 4) {
			if (valueOf.get("dd") >= 20)
				this.sign = "Taurus";
			else
				this.sign = "Aries";
		}
		// May
		else if (valueOf.get("MM") == 5) {
			if (valueOf.get("dd") >= 21)
				this.sign = "Gemini";
			else
				this.sign = "Taurus";
		}
		// June
		else if (valueOf.get("MM") == 6) {
			if (valueOf.get("dd") >= 21)
				this.sign = "Cancer";
			else
				this.sign = "Gemini";
		}
		// July
		else if (valueOf.get("MM") == 7) {
			if (valueOf.get("dd") >= 22)
				this.sign = "Leo";
			else
				this.sign = "Cancer";
		}
		// August
		else if (valueOf.get("MM") == 8) {
			if (valueOf.get("dd") >= 23)
				this.sign = "Virgo";
			else
				this.sign = "Leo";
		}
		// September
		else if (valueOf.get("MM") == 9) {
			if (valueOf.get("dd") >= 23)
				this.sign = "Libra";
			else
				this.sign = "Virgo";
		}
		// October
		else if (valueOf.get("MM") == 10) {
			if (valueOf.get("dd") >= 23)
				this.sign = "Scorpio";
			else
				this.sign = "Libra";
		}
		// November
		else if (valueOf.get("MM") == 11) {
			if (valueOf.get("dd") >= 22)
				this.sign = "Sagittarius";
			else
				this.sign = "Scorpio";
		}
		// December
		else if (valueOf.get("MM") == 12) {
			if (valueOf.get("dd") >= 21)
				this.sign = "Capricorn";
			else
				this.sign = "Sagittarius";
		}
	}
}
