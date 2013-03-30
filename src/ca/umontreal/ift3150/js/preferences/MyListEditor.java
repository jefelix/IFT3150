package ca.umontreal.ift3150.js.preferences;

import java.util.HashMap;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class MyListEditor extends ListEditor{
	
	private StringFieldEditor rule;
	private HashMap<String,String> map;
	
	public MyListEditor(String name, String labelText, Composite parent){
		super(name, labelText, parent);
	}
	
	@Override
	protected String createList(String[] items) {
		String result = "";
		for(int i=0; i<items.length; i++){
			result += items[i] + "\n";
		}
		return result;
	}

	@Override
	protected String getNewInputObject() {
		map.put(rule.getStringValue(), ((ColorPreferencesPage)getPage()).getMinValue());
		return rule.getStringValue();
	}

	@Override
	protected String[] parseString(String string) {
		String[] list = string.split("\\n");
		return list;
	}
	
	@Override
	protected void selectionChanged() {
		int index = getList().getSelectionIndex();
		System.out.println(index);
		if(index != -1){
			rule.setStringValue(getList().getItem(index));
		}
        /*int size = list.getItemCount();
        removeButton.setEnabled(index >= 0);
        upButton.setEnabled(size > 1 && index > 0);
        downButton.setEnabled(size > 1 && index >= 0 && index < size - 1);*/
	}
	
	public void setRule(StringFieldEditor rule){
		this.rule = rule;
	}

}
