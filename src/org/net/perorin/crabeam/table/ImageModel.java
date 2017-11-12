package org.net.perorin.crabeam.table;

import javax.swing.table.DefaultTableModel;

public class ImageModel extends DefaultTableModel {

	public static final String[] COLUMN_HEADER = { "No.", "スクリーンショット名" };
	private static final String[][] CLUMN_INIT = { { "", "" } };
	boolean[] columnEditables = new boolean[] { false, true, };

	public ImageModel() {
		super(CLUMN_INIT, COLUMN_HEADER);
		this.setRowCount(0);
	}

	public boolean isCellEditable(int row, int column) {
		return columnEditables[column];
	}
}
