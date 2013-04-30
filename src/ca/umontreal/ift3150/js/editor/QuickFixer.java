package ca.umontreal.ift3150.js.editor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class QuickFixer implements IMarkerResolutionGenerator {
	public IMarkerResolution[] getResolutions(IMarker mk) {
		return new IMarkerResolution[] {
				new QuickFix("Information"),
		};
	}
}