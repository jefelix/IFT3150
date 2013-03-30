package ca.umontreal.ift3150.js.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import ca.umontreal.ift3150.js.Activator;
import ca.umontreal.ift3150.js.editor.JSHover;
import ca.umontreal.ift3150.js.parser.AnalysisFileParser;

public class SelectAnalysisWindow extends Dialog {
	
	private Text fieldFilePath;
	private String filePath;
	private IProject project;

	public SelectAnalysisWindow(Shell parentShell, IProject project) {
		super(parentShell);
		this.project = project;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Import analysis results");
	}
	
	@Override
	protected Control createDialogArea(final Composite parent) {
	    
		final Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(2, false);
		gl_container.marginRight = 5;
		gl_container.marginLeft = 10;
		container.setLayout(gl_container);

		fieldFilePath = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		fieldFilePath.setText(getPref("filePath"));
		fieldFilePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button buttonBrowse = new Button(container, SWT.PUSH);
		buttonBrowse.setText("Browse");
		buttonBrowse.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e){
				String path;
				FileDialog dialog = new FileDialog( container.getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.profile", "*.*" });
				path = dialog.open();
				if ((path != null) && (path.length() != 0)){       
					fieldFilePath.setText(path);
					System.out.println("FilePath: "+path);
				}
			}
		});
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 150);
	}

	@Override
	protected void okPressed() {
		savePref("filePath", fieldFilePath.getText());
		AnalysisFileParser afp = new AnalysisFileParser(fieldFilePath.getText(), project);
		afp.initiliazeParser();
		JSHover jsh = new JSHover(afp);
		//afp.printTextInfo();
		//afp.printNumericInfo();
		super.okPressed();
	}
	
	public void savePref(String key, String value){
		System.out.println("pathPrefs: "+ConfigurationScope.INSTANCE.getLocation());
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences sub1 = preferences.node("analysisPreferences");
		sub1.put(key, value);
		try {
			preferences.flush();
			System.out.println("Sauvegarder!");
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public String getPref(String key){
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences sub1 = preferences.node("analysisPreferences");
		String value = sub1.get(key, "");
		return value;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

} 