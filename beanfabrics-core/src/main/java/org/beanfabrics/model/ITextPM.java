/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

/**
 * @author Michael Karneim
 */
public interface ITextPM extends IValuePM {
    /**
     * Returns the text value of this model.
     * 
     * @return the text value
     */
    public String getText();

    /**
     * Set the text to be stored in this model. If the argument is
     * <code>null</code>, it will be changed into the empty string
     * <code>""</code>.
     * 
     * @param aText the text to set
     */
    public void setText(String aText);

    /**
     * Returns if this object was modified (means: if the text value equals the
     * default text).
     */
    public boolean isModified();

    /**
     * Sets the default text as text value.
     */
    public void reset();

    /**
     * Sets the text value as default text.
     */
    public void preset();

    public void setOptions(Options options);

    public Options getOptions();

    /**
     * Parses the content and formats it.
     */
    public void reformat();
}