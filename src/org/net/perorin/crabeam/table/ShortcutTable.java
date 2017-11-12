package org.net.perorin.crabeam.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.Optional;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;

public class ShortcutTable extends JTable {

	public ShortcutTable(ShortcutModel model) {
		super(model);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.setAutoResizeMode(AUTO_RESIZE_OFF);
		this.getColumn(ShortcutModel.COLUMN_HEADER[0]).setPreferredWidth(130);
		this.getColumn(ShortcutModel.COLUMN_HEADER[1]).setPreferredWidth(60);
		this.getColumn(ShortcutModel.COLUMN_HEADER[2]).setPreferredWidth(60);
		this.getTableHeader().setBackground(SystemColor.inactiveCaptionBorder);
	}

	@Override
	public void updateUI() {
		setDefaultEditor(Boolean.class, null);
		super.updateUI();
		setDefaultEditor(Boolean.class, new CheckBoxPanelEditor());
	}

}

class CheckBoxPanelEditor extends AbstractCellEditor implements TableCellEditor {
	protected final JComponent renderer = new JPanel(new GridBagLayout()) {
		protected transient MouseListener listener;

		@Override
		public void updateUI() {
			removeMouseListener(listener);
			super.updateUI();
			setBorder(UIManager.getBorder("Table.noFocusBorder"));
			listener = new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					fireEditingStopped();
				}
			};
			addMouseListener(listener);
		}
	};
	protected final JCheckBox checkBox = new JCheckBox() {
		protected transient Handler handler;

		@Override
		public void updateUI() {
			removeActionListener(handler);
			removeMouseListener(handler);
			super.updateUI();
			setOpaque(false);
			setFocusable(false);
			setRolloverEnabled(false);
			handler = new Handler();
			addActionListener(handler);
			addMouseListener(handler);
		}
	};

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		checkBox.setSelected(Objects.equals(value, Boolean.TRUE));
		renderer.add(checkBox);
		return renderer;
	}

	@Override
	public Object getCellEditorValue() {
		return checkBox.isSelected();
	}

	private class Handler extends MouseAdapter implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			fireEditingStopped();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Container c = SwingUtilities.getAncestorOfClass(JTable.class, e.getComponent());
			if (c instanceof JTable) {
				JTable table = (JTable) c;
				if (checkBox.getModel().isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
					renderer.setBackground(table.getBackground());
				} else {
					renderer.setBackground(table.getSelectionBackground());
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Class<JTable> clz = JTable.class;
			Optional.ofNullable(SwingUtilities.getAncestorOfClass(clz, e.getComponent()))
					.filter(clz::isInstance).map(clz::cast)
					.filter(table -> table.isEditing() && !table.getCellEditor().stopCellEditing())
					.ifPresent(table -> table.getCellEditor().cancelCellEditing());
		}
	}
}