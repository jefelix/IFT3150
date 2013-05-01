package ca.umontreal.ift3150.js.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Fenêtre de sélection des valeurs min et max des métriques numériques 
 * et des couleurs correspondantes
 *
 */
public class ColorSettingsWindow extends Dialog{

	private RGB minColor;
	private RGB maxColor;
	private String minValue;
	private String maxValue;
	private Data data;
	private Text textMinValue;
	private Text textMaxValue;
	
	protected ColorSettingsWindow(Shell parentShell, Data data) {
		super(parentShell);
		this.data = data;
		this.minColor = this.data.getMinColor();
		this.maxColor = this.data.getMaxColor();
		this.minValue = this.data.getMinValue();
		this.maxValue = this.data.getMaxValue();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Color settings");
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(2, true);
		gl_container.marginRight = 5;
		gl_container.marginLeft = 10;
		container.setLayout(gl_container);

		Label labelMinValue = new Label(container, SWT.CENTER);
		labelMinValue.setText("Min value:");
		textMinValue = new Text(container, SWT.BORDER | SWT.MULTI);
		textMinValue.setText(minValue);
		
		Button buttonMin = new Button(container, SWT.PUSH | SWT.BORDER);
		buttonMin.setText("Min color");
		final Text textMin = new Text(container, SWT.BORDER | SWT.MULTI);
		textMin.setBackground(new Color(Display.getCurrent(), minColor));
		
		buttonMin.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(container.getShell());
				cd.setText("Choose min color");
				cd.setRGB(new RGB(255, 255, 255));
				RGB newColor = cd.open();
				if (newColor == null) {
					return;
				}
				textMin.setBackground(new Color(Display.getCurrent(), newColor));
				minColor = newColor;
			}
		});
		
		Label labelMaxValue = new Label(container, SWT.CENTER);
		labelMaxValue.setText("Max value:");
		textMaxValue = new Text(container, SWT.BORDER | SWT.MULTI);
		textMaxValue.setText(maxValue);
		
		Button buttonMax = new Button(container, SWT.PUSH | SWT.BORDER);
		buttonMax.setText("Max color");
		final Text textMax = new Text(container, SWT.BORDER | SWT.MULTI);
		textMax.setBackground(new Color(Display.getCurrent(), maxColor));
		
		buttonMax.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(container.getShell());
				cd.setText("Choose max color");
				cd.setRGB(new RGB(255, 255, 255));
				RGB newColor = cd.open();
				if (newColor == null) {
					return;
				}
				textMax.setBackground(new Color(Display.getCurrent(), newColor));
				maxColor = newColor;
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
		return new Point(270, 210);
	}

	@Override
	protected void okPressed() {
		data.setMinValue(textMinValue.getText());
		data.setMinColor(minColor);
		data.setMaxValue(textMaxValue.getText());
		data.setMaxColor(maxColor);
		PluginView.viewer.setInput(ModelProvider.data);
		PluginView.viewer.refresh();
		super.okPressed();
	}

}
