package com.domo4pi.utils.selector;

import com.domo4pi.utils.CheckUtils;
import com.domo4pi.utils.selector.exceptions.IllegalTextDeselectionException;
import com.domo4pi.utils.selector.exceptions.IllegalTextSelectionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class can parser a String.
 * 
 * @author Alberto Crespo Sánchez <albertocresposanchez@gmail.com>
 * @version 1.0 <18-01-2011>
 */
public class Selector {
	private final StringBuilder text;
	private final ArrayList<SelectionRange> selections;

	private static StringBuilder loadFromFile(File file) throws FileNotFoundException {
		Scanner scan = new Scanner(file);
		StringBuilder builder = new StringBuilder();

		while (scan.hasNextLine()) {
			if (builder.length() != 0) {
				builder.append("\n");
			}
			builder.append(scan.nextLine());
		}
		scan.close();
		return builder;
	}

	/**
	 * Constructor.
	 * 
	 * @throws java.io.FileNotFoundException
	 * 
	 */
	public Selector(File file) throws FileNotFoundException {
		this(loadFromFile(file));
	}

	/**
	 * Constructor.
	 * 
	 */
	public Selector() {
		this(new StringBuilder());
	}

	/**
	 * Constructor.
	 * 
	 * @param text The text to parser.
	 */
	public Selector(String text) {
		CheckUtils.checkString("text", text);
		
		this.text = new StringBuilder(text);
		selections = new ArrayList<SelectionRange>();
		clearSelection();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param text The text to parser.
	 */
	public Selector(StringBuilder text) {
		CheckUtils.checkNull("text", text);

		this.text = text;
		selections = new ArrayList<SelectionRange>();
		clearSelection();
	}

	/**
	 * Append a string to the main text selection.
	 * 
	 * @param string The string to add.
	 * @return This to concatenate calls.
	 */
	public Selector append(String string) {
		text.append(string);

		int end = text.length();

		SelectionRange main = selections.get(0);
		main.setEnd(end);
		main.setEffectiveEnd(end);

		return this;
	}

	/**
	 * Return the main string.
	 * 
	 * @return The main string
	 */
	public String getMainString() {
		return text.toString();
	}

	/**
	 * Select the text from the given start string to the selection end. Without no SelectorOptions.
	 * 
	 * @param startString The start string.
	 * @return True if the text was selected, False if wan't. Only available with Optional option.
	 * @throws IllegalTextSelectionException if the startString text doesn't exist in the selection.
	 */
	public boolean selectTextToEnd(String startString) {
		CheckUtils.checkNull("startString", startString);

        return setSelection(startString, null, SelectorOptions.NO_OPTIONS);
	}

	/**
	 * Select the text from the given start string to the selection end.
	 * 
	 * @param startString The start string.
	 * @param option The selection option.
	 * @return True if the text was selected, False if wan't. Only available with Optional option.
	 * @throws IllegalTextSelectionException if the startString text doesn't exist in the selection.
	 */
	public boolean selectTextToEnd(String startString, SelectorOptions... option) {
		CheckUtils.checkNull("startString", startString);

		return setSelection(startString, null, option);
	}

	/**
	 * Select the text from the selection start to the given end string. Without SelectorOptions.
	 * 
	 * @param endString The end string.
	 * @return True if the text was selected, False if wan't. Only available with Optional option.
	 * @throws IllegalTextSelectionException if the endString text doesn't exist in the selection.
	 */
	public boolean selectTextFromStart(String endString) {
		CheckUtils.checkNull("endString", endString);

		return setSelection(null, endString, SelectorOptions.NO_OPTIONS);
	}

	/**
	 * Select the text from the selection start to the given end string.
	 * 
	 * @param endString The end string.
	 * @param option The selection option.
	 * @return True if the text was selected, False if wan't. Only available with Optional option.
	 * @throws IllegalTextSelectionException if the endString text doesn't exist in the selection.
	 */
	public boolean selectTextFromStart(String endString, SelectorOptions... option) {
		CheckUtils.checkNull("endString", endString);

		return setSelection(null, endString, option);
	}

	/**
	 * Select the text between the given text. Without SelectorOptions.
	 * 
	 * @param startString The start string.
	 * @param endString The end string.
	 * @return True if the text was selected, False if wan't. Only available with Optional option.
	 * @throws IllegalTextSelectionException if the boundaries texts don't exist in the selection.
	 */
	public boolean selectText(String startString, String endString) {
		CheckUtils.checkNull("startString", startString);
		CheckUtils.checkNull("endString", endString);

		return setSelection(startString, endString, SelectorOptions.NO_OPTIONS);
	}

	/**
	 * Select the text between the given text.
	 * 
	 * @param startString The start string.
	 * @param endString The end string.
	 * @param option The selection option.
	 * @return True if the text was selected, False if wan't. Only available with Optional option.
	 * @throws IllegalTextSelectionException if the boundaries texts don't exist in the selection.
	 */
	public boolean selectText(String startString, String endString, SelectorOptions... option) {
		CheckUtils.checkNull("startString", startString);
		CheckUtils.checkNull("endString", endString);

		return setSelection(startString, endString, option);
	}

	/**
	 * Deselect the last selected text .
	 * 
	 * @throws IllegalTextDeselectionException if there isn't any selected text
	 */
	public void deselectText() {
		if (selections.size() < 2) {
			throw new IllegalTextDeselectionException("There isn't any selected text");
		}
		selections.remove(selections.size() - 1);
	}

	/**
	 * Clear all selected text and select the whole text.
	 */
	public void clearSelection() {
		selections.clear();
		selections.add(new SelectionRange(0, text.length(), 0, text.length()));
	}

	/**
	 * Check if our selection contains the text given. Ignoring case.
	 * 
	 * @param string The string to check
	 * @return True if the selection contains the string, false if doesn't.
	 */
	public boolean containsTextIgnoringCase(String string) {
		if (selectTextFromStart(string, SelectorOptions.NO_MOVE, SelectorOptions.OPTIONAL, SelectorOptions.IGNORE_CASE)) {
			deselectText();
			return true;
		}
		return false;
	}

	/**
	 * Check if our selection contains the text between two strings given. Ignoring case.
	 * 
	 * @param startString The startString to check
	 * @param endString The endString to check
	 * @return True if the selection contains the string, false if doesn't.
	 */
	public boolean containsTextIgnoringCase(String startString, String endString) {
		if (selectText(startString, endString, SelectorOptions.NO_MOVE, SelectorOptions.OPTIONAL, SelectorOptions.IGNORE_CASE)) {
			deselectText();
			return true;
		}
		return false;
	}

	/**
	 * Check if our selection contains the text given.
	 * 
	 * @param string The string to check
	 * @return True if the selection contains the string, false if doesn't.
	 */
	public boolean containsText(String string) {
		if (selectTextFromStart(string, SelectorOptions.NO_MOVE, SelectorOptions.OPTIONAL)) {
			deselectText();
			return true;
		}
		return false;
	}

	/**
	 * Check if our selection contains the text between two strings given.
	 * 
	 * @param startString The startString to check
	 * @param endString The endString to check
	 * @return True if the selection contains the string, false if doesn't.
	 */
	public boolean containsText(String startString, String endString) {
		if (selectText(startString, endString, SelectorOptions.NO_MOVE, SelectorOptions.OPTIONAL)) {
			deselectText();
			return true;
		}
		return false;
	}

	/**
	 * Return the text include in the last selection.
	 * 
	 * @return The extracted text.
	 */
	public String extractText() {
		SelectionRange range = getLastSelection();
		String result = new String(text.substring(range.getEffectiveStart(), range.getEffectiveEnd()));
		return result;
	}

	/**
	 * Extract the text from the selection start to the given end string. Without SelectorOptions.
	 * 
	 * @param startString The start string.
	 * @return The extracted text.
	 * @throws IllegalTextSelectionException if the startString text doesn't exist in the selection.
	 */
	public String extractTextToEnd(String startString) {
		selectTextToEnd(startString);
		String selection = extractText();
		deselectText();
		return selection;
	}

	/**
	 * Extract the text from the given start string to the selection end.
	 * 
	 * @param startString The start string.
	 * @param option The extraction option.
	 * @return The extracted text. Null if the text is not found and the OPTIONAL option is enabled.
	 * @throws IllegalTextSelectionException if the startString text doesn't exist in the selection.
	 */
	public String extractTextToEnd(String startString, SelectorOptions option) {
		if (selectTextToEnd(startString, option)) {
			String selection = extractText();
			deselectText();
			return selection;
		}
		return null;
	}

	/**
	 * Extract the text from the given start string to the selection end. Without SelectorOptions.
	 * 
	 * @param endString The end string.
	 * @return The extracted text.
	 * @throws IllegalTextSelectionException if the endString text doesn't exist in the selection.
	 */
	public String extractTextFromStart(String endString) {
		selectTextFromStart(endString);
		String selection = extractText();
		deselectText();
		return selection;
	}

	/**
	 * Extract the text from the selection start to the given end string.
	 * 
	 * @param endString The end string.
	 * @param option The extraction option.
	 * @return The extracted text. Null if the text is not found and the OPTIONAL option is enabled.
	 * @throws IllegalTextSelectionException if the endString text doesn't exist in the selection.
	 */
	public String extractTextFromStart(String endString, SelectorOptions option) {
		if (selectTextFromStart(endString, option)) {
			String selection = extractText();
			deselectText();
			return selection;
		}
		return null;
	}

	/**
	 * Extract the text between the given text. Without SelectorOptions.
	 * 
	 * @param startString The start string.
	 * @param endString The end string.
	 * @return The extracted text.
	 * @throws IllegalTextSelectionException if the boundaries text don't exist in the selection.
	 */
	public String extractText(String startString, String endString) {
		selectText(startString, endString, SelectorOptions.NO_OPTIONS);
		String selection = extractText();
		deselectText();
		return selection;
	}

	/**
	 * Extract the text between the given text.
	 * 
	 * @param startString The start string.
	 * @param endString The end string.
	 * @param option The extraction option.
	 * @return The extracted text. Null if the text is not found and the OPTIONAL option is enabled.
	 * @throws IllegalTextSelectionException if the boundaries text don't exist in the selection.
	 */
	public String extractText(String startString, String endString, SelectorOptions... option) {
		if (selectText(startString, endString, option)) {
			String selection = extractText();
			deselectText();
			return selection;
		}
		return null;
	}

	/**
	 * Select the text between the given text. It's a utilitary method.
	 * 
	 * @param startString The start string.
	 * @param endString The end string.
	 * @param options The selection option.
	 * @return True if the text was selected, False if wan't. Only available with Optional option.
	 */
	private boolean setSelection(String startString, String endString, SelectorOptions... options) {
		CheckUtils.checkNull("options", options);

		String tempText = text.toString();
		if (SelectorOptions.IGNORE_CASE.isInclude(options)) {
			tempText = tempText.toLowerCase();
		}

		SelectionRange lastRange = getLastSelection();

		int start = lastRange.getEffectiveStart();
		if (startString != null) {
			if (SelectorOptions.IGNORE_CASE.isInclude(options)) {
				startString = startString.toLowerCase();
			}
			start = tempText.indexOf(startString, lastRange.getEffectiveStart());
		}

		if (((start < 0) || (start > lastRange.getEffectiveEnd())) && (SelectorOptions.OPTIONAL.isInclude(options))) {
			return false;
		} else if ((start < 0) || (start > lastRange.getEffectiveEnd())) {
			throwTextNotFound("Start text: \"#\" not found.", startString);
		}

		int effectiveStart = start + ((startString != null) ? startString.length() : 0);
		if ((effectiveStart > lastRange.getEffectiveEnd()) && (SelectorOptions.OPTIONAL.isInclude(options))) {
			return false;
		} else if (effectiveStart > lastRange.getEffectiveEnd()) {
			throwTextNotFound("Start text: \"#\" not found.", startString);
		}

		int effectiveEnd = lastRange.getEffectiveEnd();
		if (endString != null) {
			if (SelectorOptions.IGNORE_CASE.isInclude(options)) {
				endString = endString.toLowerCase();
			}
			effectiveEnd = tempText.indexOf(endString, effectiveStart);
		}

		if (((effectiveEnd < 0) || (effectiveEnd > lastRange.getEffectiveEnd())) && (SelectorOptions.OPTIONAL.isInclude(options))) {
			return false;
		} else if ((effectiveEnd < 0) || (effectiveEnd > lastRange.getEffectiveEnd())) {
			throwTextNotFound("End text: \"#\" not found.", endString);
		}

		int end = effectiveEnd + ((endString != null) ? endString.length() : 0);
		if ((end > lastRange.getEffectiveEnd()) && (SelectorOptions.OPTIONAL.isInclude(options))) {
			return false;
		} else if (end > lastRange.getEffectiveEnd()) {
			throwTextNotFound("End text: \"#\" not found.", endString);
		}

		boolean includeDelimiters = SelectorOptions.INCLUDE_DELIMITERS.isInclude(options);
		selections.add(new SelectionRange(start, end, ((includeDelimiters) ? start : effectiveStart), ((includeDelimiters) ? end : effectiveEnd)));

		if (!SelectorOptions.NO_MOVE.isInclude(options)) {
			lastRange.setEffectiveStart(end);
		}
		return true;
	}

	/**
	 * Get the last text selection.
	 * 
	 * @return The last selection if there is a text selected, a whole selection if is not a text selected.
	 */
	private SelectionRange getLastSelection() {
		return selections.get(selections.size() - 1);
	}

	/**
	 * Throw a error message indicating the text value
	 * 
	 * @param errMsg The error message, must contains one # character. It will be replaced for the value.
	 * @param value The value.
	 */
	private void throwTextNotFound(String errMsg, String value) {
		CheckUtils.checkString("errMsg", errMsg);
		CheckUtils.checkNull("value", value);

		throw new IllegalTextSelectionException(errMsg.replace("#", value));
	}
}
