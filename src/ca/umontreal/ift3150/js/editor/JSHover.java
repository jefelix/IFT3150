package ca.umontreal.ift3150.js.editor;

import org.eclipse.wst.jsdt.internal.ui.text.html.HTMLTextPresenter;
import org.eclipse.wst.jsdt.ui.text.java.hover.IJavaEditorTextHover;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.EditorsUI;

import ca.umontreal.ift3150.js.parser.AnalysisFileParser;

@SuppressWarnings("restriction")
public class JSHover implements ITextHover, IJavaEditorTextHover, ITextHoverExtension{

	private IEditorPart editor;
	
	public String getHoverInfo(ITextViewer textViewer, IRegion region) {
		//String info = parser.findRegion(region.getOffset(), region.getOffset() + region.getLength());
		//System.out.println(info);
		/*IDocument doc = textViewer.getDocument();
		String txt;
		try {
			txt = doc.get(region.getOffset(), region.getLength());
			System.out.println("texte: "+txt);
			if(txt.equals("gege")){
				return null;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}*/
		return "gege";
	}

	public IRegion getHoverRegion(ITextViewer arg0, int arg1) {
		return null;
	}

	public void setEditor(IEditorPart ed) {
		editor = ed;
	}
	
	public IEditorPart getEditor(){
		return editor;
	}

	public IInformationControlCreator getHoverControlCreator() {
		return new IInformationControlCreator() {
            public IInformationControl createInformationControl(Shell parent) {
                return new DefaultInformationControl(parent, EditorsUI.getTooltipAffordanceString(),new HTMLTextPresenter(false));
            }
        };
	}
}
