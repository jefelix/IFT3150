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
	public static ProfileFileParser afp;
	
	public ModelProvider(ProfileFileParser afp) {
		this.afp = afp;
		data = new ArrayList<Data>();
		for(Object metric : afp.getDeclarations().keySet()) {
			JSONObject metricInfo = (JSONObject) afp.getDeclarations().get(metric);
		    String metricName = (String) metric;
		    String metricTitle = (String) metricInfo.get("title");
			String metricType = (String) metricInfo.get("type");
			if(metricType.equals("number") || metricType.equals("percent")){
				data.add(new Data(metricName, metricTitle, metricType,  "0", new RGB(0,255,0), String.valueOf(afp.getMax(metricName)), new RGB(255, 0, 0)));
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