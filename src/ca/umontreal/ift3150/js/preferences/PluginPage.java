package ca.umontreal.ift3150.js.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ca.umontreal.ift3150.js.Activator;

public class PluginPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

	public PluginPage() {
		super(GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Find your analysis results directly in the Eclipse JavaScript editor.");
	}
	
	public void createFieldEditors() {
		
	}
}
