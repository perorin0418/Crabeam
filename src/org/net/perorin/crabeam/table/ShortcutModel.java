package org.net.perorin.crabeam.table;

import javax.swing.table.DefaultTableModel;

public class ShortcutModel extends DefaultTableModel {

	public static final String[] COLUMN_HEADER = { "ショートカット", "キーコード", "モディファイ" };
	private static final Object[][] CLUMN_INIT = { { "", "", "" } };
	boolean[] columnEditables = new boolean[] { false, true, true };

	public ShortcutModel() {
		super(CLUMN_INIT, COLUMN_HEADER);
		this.setRowCount(0);
	}

	public boolean isCellEditable(int row, int column) {
		return columnEditables[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}
}
