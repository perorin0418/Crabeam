package org.net.perorin.crabeam.table;

import javax.swing.table.DefaultTableModel;

public class EvidenceEditModel extends DefaultTableModel {

	public static final String[] COLUMN_HEADER = { " ", "BDD", "エビデンス名", "", "INFOファイル", "INFOパス", "画像パス" };
	private static final String[][] CLUMN_INIT = { { "", "", "", "", "", "", "" } };
	boolean[] columnEditables = new boolean[] { false, true, true, true, false, false, false };

	public EvidenceEditModel() {
		super(CLUMN_INIT, COLUMN_HEADER);
		this.setRowCount(0);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return columnEditables[column];
	}
}
