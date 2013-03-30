package ca.umontreal.ift3150.js.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ca.umontreal.ift3150.js.Activator;

public class ColorPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{
	
	StringFieldEditor minvalue;
	
	public ColorPreferencesPage() {
		super(GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Rules for color interpolation:");
	}
	
	public void createFieldEditors() {
		// all the fields
		MyListEditor listRules = new MyListEditor("LIST", "Rules:", getFieldEditorParent());
		StringFieldEditor rule = new StringFieldEditor("RULE", "Rule name:", getFieldEditorParent());
		listRules.setRule(rule);
		minvalue = new StringFieldEditor("MIN_VALUE", "Min value:", getFieldEditorParent());
		ColorFieldEditor mincolor = new ColorFieldEditor("MIN_COLOR", "Min color:", getFieldEditorParent());
		StringFieldEditor maxvalue = new StringFieldEditor("MAX_VALUE", "Max value:", getFieldEditorParent());
		ColorFieldEditor maxcolor = new ColorFieldEditor("MAX_COLOR", "Max color:", getFieldEditorParent());
		// add the fields
		addField(listRules);
		addField(rule);
		addField(minvalue);
		addField(mincolor);
		addField(maxvalue);
		addField(maxcolor);
	}
	
	//TODO: return stringfield plutot
	public String getMinValue(){
		return minvalue.getStringValue();
	}

}
