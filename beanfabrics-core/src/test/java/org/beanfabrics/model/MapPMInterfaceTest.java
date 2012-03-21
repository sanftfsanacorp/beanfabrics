/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

/**
 * @author Michael Karneim
 */
public class MapPMInterfaceTest extends IListPMInterfaceAbstractTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MapPMInterfaceTest.class);
    }

    protected IListPM<RowPM> create(Collection<RowPM> elements, int[] selectedIndexes)
        throws Exception {
        MapPM<Integer, RowPM> newMapCell = new MapPM<Integer, RowPM>();
        // add the initial elements
        for (RowPM row : elements) {
            newMapCell.put(row.id.getInteger(), row);
        }
        // create the inital selection
        for (int index : selectedIndexes) {
            newMapCell.getSelection().addInterval(index, index);
        }
        // assign the listCell
        return newMapCell;
    }
}