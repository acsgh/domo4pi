package com.domo4pi.utils.selector;

import com.domo4pi.utils.CheckUtils;

/**
 * This class define a selection range.
 * 
 * @author Alberto Crespo Sánchez <albertocresposanchez@gmail.com>
 * @version 1.0 <18-01-2011>
 */
class SelectionRange {
	private final int start;
	private int end;
	private int effectiveStart;
	private int effectiveEnd;

	/**
	 * Constructor
	 * 
	 * @param start The start position.
	 * @param end The end position.
	 */
	SelectionRange(int start, int end, int effectiveStart, int effectiveEnd) {
		CheckUtils.checkIntBigger("start", start, 0);
		CheckUtils.checkIntBigger("end", end, 0);

		this.start = start;
		this.end = end;
		setEffectiveEnd(effectiveEnd);
		setEffectiveStart(effectiveStart);
	}

	/**
	 * Setter.
	 * 
	 * @return The start position.
	 */
	int getStart() {
		return start;
	}

	/**
	 * Setter.
	 * 
	 * @return The end position.
	 */
	int getEnd() {
		return end;
	}

	/**
	 * Getter.
	 * 
	 * @return The effectiveStart position.
	 */
	int getEffectiveStart() {
		return effectiveStart;
	}

	/**
	 * Getter.
	 * 
	 * @return The effectiveEnd position.
	 */
	int getEffectiveEnd() {
		return effectiveEnd;
	}

	/**
	 * Setter.
	 * 
	 * @param effectiveStart The effectiveStart position.
	 */
	void setEffectiveStart(int effectiveStart) {
		CheckUtils.checkIntBigger("effectiveStart", effectiveStart, 0);
		CheckUtils.checkIntBigger("effectiveStart", end, "start", start);
		CheckUtils.checkIntSmaller("effectiveStart", effectiveStart, "effectiveEnd", effectiveEnd);
		CheckUtils.checkIntSmaller("effectiveStart", effectiveStart, "end", end);

		this.effectiveStart = effectiveStart;
	}

	/**
	 * Setter.
	 * 
	 * @param effectiveEnd The effectiveEnd position.
	 */
	void setEffectiveEnd(int effectiveEnd) {
		CheckUtils.checkIntBigger("effectiveEnd", effectiveEnd, 0);
		CheckUtils.checkIntBigger("effectiveEnd", effectiveEnd, "start", start);
		CheckUtils.checkIntBigger("effectiveEnd", effectiveEnd, "effectiveStart", effectiveStart);
		CheckUtils.checkIntSmaller("effectiveEnd", effectiveEnd, "end", end);

		this.effectiveEnd = effectiveEnd;
	}

	/**
	 * Setter.
	 * 
	 * @param end The effectiveEnd position.
	 */
	void setEnd(int end) {
		CheckUtils.checkIntBigger("end", end, 0);
		CheckUtils.checkIntBigger("end", end, "start", start);
		CheckUtils.checkIntBigger("end", end, "effectiveStart", effectiveStart);
		CheckUtils.checkIntBigger("end", end, "effectiveEnd", effectiveEnd);

		this.end = end;
	}

	/**
	 * Check is some selection is contains in this selection.
	 * 
	 * @param selection The selection to check.
	 * @return True if it's contains, False if isn't.
	 */
	boolean contains(SelectionRange selection) {
		CheckUtils.checkNull("selection", selection);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SelectionRange [effectiveEnd=");
		builder.append(effectiveEnd);
		builder.append(", effectiveStart=");
		builder.append(effectiveStart);
		builder.append(", end=");
		builder.append(end);
		builder.append(", start=");
		builder.append(start);
		builder.append("]");
		return builder.toString();
	}
}
