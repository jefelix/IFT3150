package ca.umontreal.ift3150.js.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Gère l'ajout d'un menu sur un projet Eclipse permettant d'ouvrir un fichier de profil
 *
 */
public class Menu implements IObjectActionDelegate{

	ISelection selection;
	
	@Override
	public void run(IAction arg0) {
		if(!(selection instanceof IStructuredSelection)){
			return;
		}
		IStructuredSelection structured = (IStructuredSelection) selection;
		IProject selectedProject = (IProject) structured.getFirstElement();
		
		Display display = Display.getCurrent();
		Shell parent = new Shell(display);

		SelectProfileFileWindow window = new SelectProfileFileWindow(parent, selectedProject);
		window.setBlockOnOpen(true);
		window.open();	
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		
	}

}
