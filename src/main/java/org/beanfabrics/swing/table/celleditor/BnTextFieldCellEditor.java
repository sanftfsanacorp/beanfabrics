/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documentedpackage org.beanfabrics.swing.table.celleditor;
package org.beanfabrics.swing.table.celleditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.beanfabrics.model.ITextPM;
import org.beanfabrics.swing.BnTextField;
/**
 *
 * @author Michael Karneim
 *
 */
@SuppressWarnings("serial")
public class BnTextFieldCellEditor extends AbstractCellEditor implements TableCellEditor {
    private transient ActionListener stopAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
        }
    };

    public BnTextFieldCellEditor() {
    	//
    }

    private BnTextField createBnTextField() {
        BnTextField textField = new BnTextField();
        textField.setSelectAllOnFocusGainedEnabled(false);
        //textField.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        textField.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
        textField.addActionListener(this.stopAction);
        return textField;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof ITextPM) {
            BnTextField textField = createBnTextField();
            textField.setPresentationModel((ITextPM)value);
            textField.setSelectAllOnFocusGainedEnabled(!isSelected);
            textField.selectAll();
            return textField;
        }
        return null;
    }

    public Object getCellEditorValue() {
        // in Beanfabrics we don't need to return a value
        return null;
    }

}