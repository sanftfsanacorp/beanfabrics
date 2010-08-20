package org.beanfabrics.swt;

import org.eclipse.swt.widgets.Control;

/**
 * Abstract superclass of all SWT decorators.
 *
 * @author Max Gensthaler
 */
public class AbstractDecorator<T extends Control> {
	private T control;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param control
	 *            the decorated {@link Control}
	 */
	public AbstractDecorator(T control) {
		this.control = control;
	}

	public T getControl() {
		return control;
	}
}