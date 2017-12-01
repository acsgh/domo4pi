package com.domo4pi.utils.selector;


/**
 * This enum represent the selector options.
 * 
 * @author Alberto Crespo Sánchez <albertocresposanchez@gmail.com>
 * @version 1.0 <18-01-2011>
 */
public enum SelectorOptions {
	NO_OPTIONS, NO_MOVE, OPTIONAL, INCLUDE_DELIMITERS, IGNORE_CASE;

	public boolean isInclude(SelectorOptions... options) {
		for (SelectorOptions selectorOptions : options) {
			if (selectorOptions == this) {
				return true;
			}
		}
		return false;
	}
}
