package ca.umontreal.ift3150.js.ui;

import org.eclipse.core.resources.IProject;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ca.umontreal.ift3150.js.parser.ProfileFileParser;
import ca.umontreal.ift3150.js.preferences.PluginPreferences;

/**
 * Fenêtre de sélection d'un fichier de profils
 *
 */
public class SelectProfileFileWindow extends Dialog {
	
	private Text fieldFilePath;
	private String filePath;
	private IProject project;

	public SelectProfileFileWindow(Shell parentShell, IProject project) {
		super(parentShell);
		this.project = project;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Select profile file");
	}
	
	@Override
	protected Control createDialogArea(final Composite parent) {
	    
		final Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(2, false);
		gl_container.marginRight = 5;
		gl_container.marginLeft = 10;
		container.setLayout(gl_container);

		fieldFilePath = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		fieldFilePath.setText(PluginPreferences.getPref("filePath"));
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
		PluginPreferences.savePref("filePath", fieldFilePath.getText());
		ProfileFileParser afp = new ProfileFileParser(fieldFilePath.getText(), project);
		afp.initiliazeParser();
				
		ModelProvider model = new ModelProvider(afp);
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(PluginView.ID) == null){
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PluginView.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		else{
			PluginView.viewer.setInput(ModelProvider.data);
			PluginView.viewer.refresh();
		}
		
		super.okPressed();
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

} 