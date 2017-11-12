package org.net.perorin.crabeam.window;

//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class Test extends JPanel {
	private final String[] columnNames = { "String", "Boolean" };
	private final Object[][] data = {
			{ "AAA", true }, { "bbb", false },
			{ "CCC", true }, { "ddd", false },
			{ "EEE", true }, { "fff", false },
	};
	private final TableModel model = new DefaultTableModel(data, columnNames) {
		@Override
		public Class<?> getColumnClass(int column) {
			return column == 1 ? Boolean.class : super.getColumnClass(column);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column == 1;
		}
	};
	private final JTable table = new JTable(model) {
		@Override
		public void updateUI() {
			setDefaultEditor(Boolean.class, null);
			super.updateUI();
			setDefaultEditor(Boolean.class, new CheckBoxPanelEditor());
		}
	};

	public Test() {
		super(new BorderLayout());
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table.setRowHeight(24);
		table.setRowSelectionAllowed(true);
		table.setIntercellSpacing(new Dimension(0, 1));
		table.setFocusable(false);
		add(new JScrollPane(table));
		setPreferredSize(new Dimension(320, 240));
	}

	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}
		JFrame frame = new JFrame("@title@");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Test());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
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
