package com.satch_navida.zodiacidentifier.validation.rules;

import java.util.HashMap;

public interface RuleInterface {
	/**
	 *  Runs the validation of the rule.
	 *
	 * @return HashMap<String, Object> A map containing three keys: <b>{@code valid}</b>, <b>{@code message}</b>, and <b>{@code runOtherValidation}</b>.
	 * <ul>
	 *     <li><b>{@code valid}</b> - Identifies whether the test passes or not.</li>
	 *     <li><b>{@code message}</b> - Provides the error message when the test failed. Otherwise, returns an empty string.</li>
	 *     <li><b>{@code runOtherValidation}</b> - Identifies whether to continue running other validations or stop at that last rule.</li>
	 * </ul>
	 */
	public HashMap<String, Object> validate() throws Exception;
}
