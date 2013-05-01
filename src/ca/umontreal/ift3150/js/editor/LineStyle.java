package ca.umontreal.ift3150.js.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jface.text.TextPresentation;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

/**
 * Redéfinit le style d'une ligne lors de l'affichage dans l'éditeur
 * @see org.eclipse.swt.custom.LineStyleListener
 *
 */
public class LineStyle implements LineStyleListener{

	private ArrayList<Object[]> offsetsRegions = new ArrayList<Object[]>();
	private TextPresentation textPresentation;
	private Vector<StyleRange> styles;

	public void lineGetStyle(LineStyleEvent event) {
		
		styles = new Vector<StyleRange>();
		
		int lineStart = event.lineOffset;
		int lineLength = event.lineText.length();
		int lineEnd = lineStart + lineLength;
				
		for (int i = 0, n = offsetsRegions.size(); i < n; i++) {
			Object[] offsets = (Object[]) offsetsRegions.get(i);
			int debut = (Integer) offsets[0];
			int fin = (Integer) offsets[1];
			Color color = (Color) offsets[2];
			if(lineStart <= debut && fin <= lineEnd){
				textPresentation.mergeStyleRange(new StyleRange(debut, fin - debut, null, color));
			}
			
			if (debut <= lineEnd && fin >= lineStart) {
				int start = Math.max(debut, event.lineOffset);
		        int len = Math.min(fin, event.lineOffset + lineLength) - start + 1;
		        textPresentation.mergeStyleRange(new StyleRange(start, len, null, color));
			}
		}
		
		Iterator<StyleRange> it = textPresentation.getAllStyleRangeIterator();
		while(it.hasNext()){
			styles.add(it.next());
		}
		
		event.styles = new StyleRange[styles.size()];
	    styles.copyInto(event.styles);
	}
	
	/**
	 * @return the offsets
	 */
	public ArrayList<Object[]> getOffsets() {
		return offsetsRegions;
	}

	
	public void setOffsets(ArrayList<Object[]> offsets) {
		this.offsetsRegions = offsets;
	}
	
	public TextPresentation getTextPresentation(){
		return textPresentation;
	}
	
	public void setTextPresentation(TextPresentation textPresentation){
		this.textPresentation = textPresentation;
	}
	
	/**
	 * @return the oldStyles
	 */
	public Vector<StyleRange> getStyles() {
		return styles;
	}

	/**
	 * @param oldStyles the oldStyles to set
	 */
	public void setStyles(Vector<StyleRange> styles) {
		this.styles = styles;
	}
	
}
