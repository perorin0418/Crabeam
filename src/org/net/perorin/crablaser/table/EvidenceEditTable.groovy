package org.net.perorin.crablaser.table

import java.awt.Component
import java.awt.Cursor
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.dnd.DragSource
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.activation.ActivationDataFlavor
import javax.activation.DataHandler
import javax.swing.BorderFactory
import javax.swing.DropMode
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.SwingUtilities
import javax.swing.TransferHandler
import javax.swing.event.CellEditorListener
import javax.swing.event.ChangeEvent
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

public class EvidenceEditTable extends JTable {

	public EvidenceEditTable(EvidenceEditModel model) {
		super(model)
		this.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
		this.setRowSelectionAllowed(true)
		this.setTransferHandler(new TableRowTransferHandler())
		this.setDropMode(DropMode.INSERT_ROWS)
		this.setDragEnabled(true)
		this.setAutoResizeMode(AUTO_RESIZE_OFF)
		this.getColumn(EvidenceEditModel.COLUMN_HEADER[0]).setPreferredWidth(20)
		this.getColumn(EvidenceEditModel.COLUMN_HEADER[1]).setPreferredWidth(50)
		this.getColumn(EvidenceEditModel.COLUMN_HEADER[2]).setPreferredWidth(200)
		this.getColumn(EvidenceEditModel.COLUMN_HEADER[3]).setPreferredWidth(20)
		this.removeColumn(this.getColumn(EvidenceEditModel.COLUMN_HEADER[4]))
		this.removeColumn(this.getColumn(EvidenceEditModel.COLUMN_HEADER[5]))
		this.removeColumn(this.getColumn(EvidenceEditModel.COLUMN_HEADER[6]))
		this.getColumn(EvidenceEditModel.COLUMN_HEADER[3]).setCellRenderer(new DeleteButtonRenderer())
		this.getColumn(EvidenceEditModel.COLUMN_HEADER[3]).setCellEditor(new DeleteButtonEditor() {

					@Override
					public void beforeRemove() {
						EvidenceEditTable.this.beforeRowRemove()
					}
				})
	}

	public void beforeRowRemove() {
	}
}

class TableRowTransferHandler extends TransferHandler {
	private final DataFlavor localObjectFlavor
	private int[] indices
	private int addIndex = -1 //Location where items were added
	private int addCount //Number of items added.

	protected TableRowTransferHandler() {
		super()
		localObjectFlavor = new ActivationDataFlavor(Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items")
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		JTable table = (JTable) c
		DefaultTableModel model = (DefaultTableModel) table.getModel()
		List<Object> list = new ArrayList<Object>()
		indices = table.getSelectedRows()
		for (int i : indices) {
			list.add(model.getDataVector().get(i))
		}
		Object[] transferedObjects = list.toArray()
		return new DataHandler(transferedObjects, localObjectFlavor.getMimeType())
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		JTable table = (JTable) info.getComponent()
		boolean isDroppable = info.isDrop() && info.isDataFlavorSupported(localObjectFlavor)
		//XXX bug?
		table.setCursor(isDroppable ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop)
		return isDroppable
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.MOVE //TransferHandler.COPY_OR_MOVE
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		if (!canImport(info)) {
			return false
		}
		TransferHandler.DropLocation tdl = info.getDropLocation()
		if (!(tdl instanceof JTable.DropLocation)) {
			return false
		}
		JTable.DropLocation dl = (JTable.DropLocation) tdl
		JTable target = (JTable) info.getComponent()
		DefaultTableModel model = (DefaultTableModel) target.getModel()
		int index = dl.getRow()
		//boolean insert = dl.isInsert()
		int max = model.getRowCount()
		if (index < 0 || index > max) {
			index = max
		}
		addIndex = index
		target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
		try {
			Object[] values = (Object[]) info.getTransferable().getTransferData(localObjectFlavor)
			addCount = values.length
			for (int i = 0; i < values.length; i++) {
				int idx = index++
				model.insertRow(idx, (Vector<?>) values[i])
				target.getSelectionModel().addSelectionInterval(idx, idx)
			}
			return true
		} catch (UnsupportedFlavorException | IOException ex) {
			ex.printStackTrace()
		}
		return false
	}

	@Override
	protected void exportDone(JComponent c, Transferable data, int action) {
		cleanup(c, action == TransferHandler.MOVE)
	}

	private void cleanup(JComponent c, boolean remove) {
		if (remove && indices != null) {
			c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
			DefaultTableModel model = (DefaultTableModel) ((JTable) c).getModel()
			if (addCount > 0) {
				for (int i = 0; i < indices.length; i++) {
					if (indices[i] >= addIndex) {
						indices[i] += addCount
					}
				}
			}
			for (int i = indices.length - 1; i >= 0; i--) {
				model.removeRow(indices[i])
			}
		}
		indices = null
		addCount = 0
		addIndex = -1
	}
}

class DeleteButton extends JButton {
	@Override
	public void updateUI() {
		super.updateUI()
		setBorder(BorderFactory.createEmptyBorder())
		setFocusable(false)
		setRolloverEnabled(false)
		setText("X")
	}
}

class DeleteButtonRenderer extends DeleteButton implements TableCellRenderer {
	@Override
	public void updateUI() {
		super.updateUI()
		setName("Table.cellRenderer")
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return this
	}
}

class DeleteButtonEditor extends DeleteButton implements TableCellEditor {
	private transient ActionListener listener

	@Override
	public void updateUI() {
		removeActionListener(listener)
		super.updateUI()

		listener = new ActionListener() {
					public void actionPerformed(ActionEvent e){
						Object o = SwingUtilities.getAncestorOfClass(JTable.class, this)
						if (o instanceof JTable) {
							DeleteButtonEditor.this.beforeRemove()
							JTable table = (JTable) o
							int row = table.convertRowIndexToModel(table.getEditingRow())
							DeleteButtonEditor.this.fireEditingStopped()
							((DefaultTableModel) table.getModel()).removeRow(row)
						}
					}
				}
		addActionListener(listener)
	}

	public void beforeRemove() {
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return this
	}

	@Override
	public Object getCellEditorValue() {
		return ""
	}

	@Override
	public boolean isCellEditable(EventObject e) {
		return true
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true
	}

	@Override
	public boolean stopCellEditing() {
		fireEditingStopped()
		return true
	}

	@Override
	public void cancelCellEditing() {
		fireEditingCanceled()
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l)
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l)
	}

	public CellEditorListener[] getCellEditorListeners() {
		return listenerList.getListeners(CellEditorListener.class)
	}

	protected void fireEditingStopped() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList()
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (Objects.isNull(changeEvent)) {
					changeEvent = new ChangeEvent(this)
				}
				((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent)
			}
		}
	}

	protected void fireEditingCanceled() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList()
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (Objects.isNull(changeEvent)) {
					changeEvent = new ChangeEvent(this)
				}
				((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent)
			}
		}
	}
}