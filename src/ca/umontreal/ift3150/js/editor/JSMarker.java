package ca.umontreal.ift3150.js.editor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class JSMarker {

	public static final String ID = "ca.umontreal.ift3150.js.marker";
	
	public static void addMarker(IResource resource, int line, int start, int end, String msg) {
		IMarker m;
		try {
			m = resource.createMarker(ID);
			m.setAttribute(IMarker.LINE_NUMBER, line);
			m.setAttribute(IMarker.CHAR_START, start);
			m.setAttribute(IMarker.CHAR_END, end);
			m.setAttribute(IMarker.MESSAGE, msg);
		} catch (CoreException e) {
			e.printStackTrace();
		} 
	}
	
	public static void deleteMarkers(IResource resource){
		int depth = IResource.DEPTH_INFINITE;
		try {
			resource.deleteMarkers(ID, true, depth);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
