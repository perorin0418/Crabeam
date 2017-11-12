package org.net.perorin.crabeam.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.xml.bind.JAXB;

import org.net.perorin.crabeam.config.Meta;
import org.net.perorin.crabeam.font.NicoFont;

public class ImageTable extends JTable {

	public ImageTable(ImageModel model) {
		super(model);
		Meta meta = JAXB.unmarshal(new File(Meta.META_PATH), Meta.class);
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.setAutoResizeMode(AUTO_RESIZE_OFF);
		if (meta != null) {
			this.getColumn(ImageModel.COLUMN_HEADER[0]).setPreferredWidth(meta.getNoWidth());
			this.getColumn(ImageModel.COLUMN_HEADER[1]).setPreferredWidth(meta.getSsWidth());
		} else {
			this.getColumn(ImageModel.COLUMN_HEADER[0]).setPreferredWidth(40);
			this.getColumn(ImageModel.COLUMN_HEADER[1]).setPreferredWidth(200);
		}
		this.setFont(NicoFont.getFont());

		JTableHeader header = this.getTableHeader();
		header.setReorderingAllowed(false);
		header.setFont(NicoFont.getFont());
		header.setBackground(SystemColor.inactiveCaptionBorder);

		TableCellRenderer hr = header.getDefaultRenderer();
		TableColumn col = this.getColumnModel().getColumn(0);
		col.setCellRenderer(new HeaderRenderer(this, hr));
	}

	public int getNoWidth() {
		return this.getColumn(ImageModel.COLUMN_HEADER[0]).getWidth();
	}

	public int getSsWidth() {
		return this.getColumn(ImageModel.COLUMN_HEADER[1]).getWidth();
	}

	class HeaderRenderer implements TableCellRenderer {
		private final TableCellRenderer tcr;

		public HeaderRenderer(JTable table, TableCellRenderer tcr) {
			this.tcr = tcr;
			RollOverListener rol = new RollOverListener();
			table.addMouseListener(rol);
			table.addMouseMotionListener(rol);
		}

		@Override
		public Component getTableCellRendererComponent(
				JTable tbl, Object val, boolean isS,
				boolean hasF, int row, int col) {
			JLabel l;
			boolean flg = row == rollOverRowIndex;
			l = (JLabel) tcr.getTableCellRendererComponent(
					tbl, val, isS, flg ? flg : hasF, row, col);
			l.setBackground(Color.WHITE);
			l.setOpaque(!flg);
			return l;
		}

		private int rollOverRowIndex = -1;

		private class RollOverListener extends MouseInputAdapter {
			@Override
			public void mouseExited(MouseEvent e) {
				rollOverRowIndex = -1;
				JTable table = (JTable) e.getSource();
				table.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				Point pt = e.getPoint();
				int column = table.columnAtPoint(pt);
				rollOverRowIndex = (column == 0) ? table.rowAtPoint(pt) : -1;
				table.repaint();
			}
		}
	}
}
