package ca.umontreal.ift3150.js.util;

import org.eclipse.swt.graphics.Color;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;

public class ColorUtils {

	public static void colore(ITextViewer textViewer, int start, int end, Color color){
		Device device = Display.getCurrent();
		Color black = device.getSystemColor(SWT.COLOR_BLACK);
		System.out.println(start+" ==> "+end+" "+color);
		TextPresentation presentation = new TextPresentation();
		//StyleRange old = textViewer.getTextWidget().getStyleRangeAtOffset(start+1);
		//System.out.println(old);
		StyleRange sr = new StyleRange(start, end - start, black, color);
		presentation.addStyleRange(sr);
		textViewer.changeTextPresentation(presentation, true);
		//textViewer.getTextWidget().setStyleRange(sr);
	}
	
	/**
	 * Source: http://fr.softuses.com/41317
	 */
	public static Color interpolate(Color start, Color end, float minvalue, float maxvalue, float value) {
		float[] startHSB = java.awt.Color.RGBtoHSB(start.getRed(), start.getGreen(), start.getBlue(), null);
		float[] endHSB = java.awt.Color.RGBtoHSB(end.getRed(), end.getGreen(), end.getBlue(), null);

		float brightness = (startHSB[2] + endHSB[2]) / 2;
		float saturation = (startHSB[1] + endHSB[1]) / 2;

		float hueMax = 0;
		float hueMin = 0;
		float ratio = 0;
		if (startHSB[0] > endHSB[0]) {
			hueMax = startHSB[0];
			hueMin = endHSB[0];
			ratio = 1 - (value / (maxvalue - minvalue));
		} else {
			hueMin = startHSB[0];
			hueMax = endHSB[0];
			ratio = (value / (maxvalue - minvalue));
		} 
		
		float hue = ((hueMax - hueMin) * ratio) + hueMin;

		java.awt.Color res = java.awt.Color.getHSBColor(hue, saturation, brightness);
		return new Color(Display.getCurrent(), res.getRed(), res.getGreen(), res.getBlue());
	}

}
