package ca.umontreal.ift3150.js.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.graphics.RGB;

/**
 * Le modèle de la vue PluginView.
 *
 */
public class Data {

	private String metricName;
	private String metricTitle;
	private String metricType;
	private String minValue;
	private RGB minColor;
	private String maxValue;
	private RGB maxColor;
	private boolean isTextChecked;
	private boolean isColorChecked;
	private boolean supportColor;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public Data() {
	}

	public Data(String metricName, String metricTitle, String metricType, String minValue, RGB minColor, String maxValue, RGB maxColor) {
		super();
		setMetricName(metricName);
		setMetricTitle(metricTitle);
		setMetricType(metricType);
		setMinValue(minValue);
		setMinColor(minColor);
		setMaxValue(maxValue);
		setMaxColor(maxColor);
		setIsTextChecked(true);
		if(this.metricType.equals("number") || this.metricType.equals("percent")){
			supportColor = true;
			setIsColorChecked(true);
		}
		else{
			supportColor = false;
		}
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public String getMetricName() {
		return metricName;
	}
	
	public String getMetricTitle() {
		return metricTitle;
	}
	
	public String getMetricType() {
		return metricType;
	}

	public String getMinValue() {
		return minValue;
	}

	public RGB getMinColor() {
		return minColor;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public RGB getMaxColor() {
		return maxColor;
	}
	
	public boolean getIsTextChecked(){
		return isTextChecked;
	}
	
	public boolean getIsColorChecked(){
		return isColorChecked;
	}
	
	public boolean getSupportColor(){
		return supportColor;
	}

	public void setMetricName(String metricName) {
		propertyChangeSupport.firePropertyChange("metricName", this.metricName,
				this.metricName = metricName);
	}
	
	public void setMetricTitle(String metricTitle) {
		propertyChangeSupport.firePropertyChange("metricTitle", this.metricTitle,
				this.metricTitle = metricTitle);
	}
	
	public void setMetricType(String metricType) {
		propertyChangeSupport.firePropertyChange("metricType", this.metricType,
				this.metricType = metricType);
	}

	public void setMinColor(RGB minColor) {
		propertyChangeSupport.firePropertyChange("minColor", this.minColor,
				this.minColor = minColor);
	}

	public void setMinValue(String minValue) {
		propertyChangeSupport.firePropertyChange("minValue", this.minValue,
				this.minValue = minValue);
	}

	public void setMaxValue(String maxValue) {
		propertyChangeSupport.firePropertyChange("maxValue", this.maxValue,
				this.maxValue = maxValue);
	}

	public void setMaxColor(RGB maxColor) {
		propertyChangeSupport.firePropertyChange("maxColor", this.maxColor,
				this.maxColor = maxColor);
	}
	
	public void setIsTextChecked(boolean isTextChecked) {
		propertyChangeSupport.firePropertyChange("isTextChecked", this.isTextChecked,
				this.isTextChecked = isTextChecked);
	}
	
	public void setIsColorChecked(boolean isColorChecked) {
		propertyChangeSupport.firePropertyChange("isColorChecked", this.isColorChecked,
				this.isColorChecked = isColorChecked);
	}

} 
