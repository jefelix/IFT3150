package ca.umontreal.ift3150.js.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

public class CheckboxTextEditingSupport extends EditingSupport {

	private final TableViewer viewer;

	public CheckboxTextEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);

	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		Data data = (Data) element;
		return data.getIsTextChecked();
	}

	@Override
	protected void setValue(Object element, Object value) {
		Data data = (Data) element;
		data.setIsTextChecked((Boolean) value);
		viewer.update(element, null);
	}
} 