package ca.umontreal.ift3150.js.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.RGB;
import org.json.simple.JSONObject;

import ca.umontreal.ift3150.js.parser.ProfileFileParser;

/**
 * Remplit le tableau à partir des
 * informations dans le fichier de profils
 *
 */
public class ModelProvider {
	
	public static List<Data> data;
	public static ProfileFileParser parser;
	
	public ModelProvider(ProfileFileParser parser) {
		this.parser = parser;
		data = new ArrayList<Data>();
		for(Object metric : parser.getDeclarations().keySet()) {
			JSONObject metricInfo = (JSONObject) parser.getDeclarations().get(metric);
		    String metricName = (String) metric;
		    String metricTitle = (String) metricInfo.get("title");
			String metricType = (String) metricInfo.get("type");
			if(metricType.equals("number") || metricType.equals("percent")){
				data.add(new Data(metricName, metricTitle, metricType,  String.valueOf(parser.getMin(metricName)), new RGB(0,255,0), String.valueOf(parser.getMax(metricName)), new RGB(255, 0, 0)));
			}
			else{
				data.add(new Data(metricName, metricTitle, metricType, "", null, "", null));
			}
		}		
	}

	public List<Data> getData() {
		return data;
	}

} 