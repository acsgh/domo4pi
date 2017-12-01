package com.domo4pi.utils.selector.exceptions;

import com.domo4pi.utils.CheckUtils;

/**
 * This exception is throw when try to perform a invalid text selection
 * 
 * @author Alberto Crespo Sánchez <albertocresposanchez@gmail.com>
 * @version 1.0 <18-01-2011>
 */
public class IllegalTextDeselectionException extends RuntimeException {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = -423242991955395284L;

	/**
	 * Constructs an IllegalTextDeselectionException with no detail message.
	 */
	public IllegalTextDeselectionException() {
		super();
	}

	/**
	 * Constructs an IllegalTextDeselectionException with the specified detail message.
	 * 
	 * @param message the detail message.
	 */
	public IllegalTextDeselectionException(String message) {
		super(message);
		CheckUtils.checkString("message", message);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of (cause==null ? null :
	 * cause.toString()) (which typically contains the class and detail message of cause). This constructor is
	 * useful for exceptions that are little more than wrappers for other throwables (for example,
	 * PrivilegedActionException).
	 * 
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null
	 *            value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalTextDeselectionException(Throwable cause) {
		super(cause);
		CheckUtils.checkNull("cause", cause);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause. Note that the detail message
	 * associated with cause is not automatically incorporated in this exception's detail message.
	 * 
	 * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage()
	 *            method).
	 * @param causethe cause (which is saved for later retrieval by the Throwable.getCause() method). (A null
	 *            value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalTextDeselectionException(String message, Throwable cause) {
		super(message, cause);
		CheckUtils.checkNull("cause", cause);
		CheckUtils.checkString("message", message);
	}
}
