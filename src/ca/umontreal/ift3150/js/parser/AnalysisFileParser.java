package ca.umontreal.ift3150.js.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ca.umontreal.ift3150.js.editor.JSMarker;
import ca.umontreal.ift3150.js.editor.LineStyle;
import ca.umontreal.ift3150.js.ui.Data;
import ca.umontreal.ift3150.js.util.ColorUtils;

public class AnalysisFileParser {

	private String filePath;
	private JSONObject fileJsonObject;
	private IProject project;

	private JSONObject declarations;
	private JSONObject maps;
	private JSONObject references;

	private static String STRING_DECLARATIONS = "declarations";
	private static String STRING_RESULTS = "results";
	private static String STRING_MAPS = "maps";

	private HashMap<IFile, ITextViewer> textViewers;
	private ArrayList<String> files;
	private HashMap<IFile, StyleRange[]> oldStyles;
	private HashMap<IFile, LineStyleListener> listeners;

	public AnalysisFileParser(String filePath, IProject project){
		this.filePath = filePath;
		this.project = project;
		this.textViewers = new HashMap<IFile, ITextViewer>();
		this.files = new ArrayList<String>();
		this.oldStyles = new HashMap<IFile, StyleRange[]>();
		this.listeners = new HashMap<IFile, LineStyleListener>();
		try {
			this.fileJsonObject = (JSONObject)(new JSONParser().parse(new FileReader(this.filePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void initiliazeParser(){
		// Parsage du fichiers
		this.declarations = parseDeclarations();
		this.maps = parseMaps();
		this.references = parseReferences();
		// Récupération des fichiers
		for(Object ref : references.keySet()){
			String filename = getFileNameFromReference(ref.toString());
			if(!files.contains(filename)){
				files.add(filename);
			}
		}
		// Récupération des textViewers et des styles
		for(String filename : files){
			IPath path = new Path(filename);
			IFile file = project.getFile(path);
			if(!textViewers.containsKey(file)){
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(new FileEditorInput(file));
				if (editorPart != null) {
					ITextOperationTarget target = (ITextOperationTarget)editorPart.getAdapter(ITextOperationTarget.class);
					if (target instanceof ITextViewer) {
						ITextViewer textViewer = (ITextViewer)target;
						textViewers.put(file, textViewer);
						oldStyles.put(file, textViewer.getTextWidget().getStyleRanges());
					}
				}
			}
		}
	}

	public JSONObject parseDeclarations() {
		return (JSONObject) fileJsonObject.get(STRING_DECLARATIONS);
	}

	public JSONObject parseMaps(){		
		return (JSONObject) fileJsonObject.get(STRING_MAPS);
	}

	public JSONObject parseReferences(){
		return (JSONObject) fileJsonObject.get(STRING_RESULTS);
	}

	public void printTextInfo(List<Data> metricsToPrint){
		if(metricsToPrint.size() == 0){
			return;
		}
		for(Object ref : references.keySet()){
			JSONObject metrics = (JSONObject) references.get(ref);
			String info = "";
			for(Object metricName : metrics.keySet()){
				for(Data data : metricsToPrint){
					if(data.getMetricName().equals(metricName)){
						String title = getTitle((String)metricName);
						String type = getType((String)metricName);
						info += "<br/><b>"+title+"</b>: ";
						if(type.equals("absval")){
							if(metrics.get(metricName) instanceof JSONArray){
								JSONArray array = (JSONArray) metrics.get(metricName);
								Iterator<JSONObject> iterator = array.iterator();
								while (iterator.hasNext()) {
									JSONObject attributs = iterator.next();
									for(Object attribut : attributs.keySet()){
										if(maps.containsKey(attributs.get(attribut))){
											info += "<br/>"+attribut+": <i>"+maps.get(attributs.get(attribut))+"</i> ";
										}
										else {
											info += "<br/>"+attribut+": <i>"+attributs.get(attribut)+"<i/> ";
										}
									}
								}
							}
							else{
								info += metrics.get(metricName)+" ";
							}
						}
						else{
							info += "<i>"+metrics.get(metricName)+"</i> ";
						}
					}
				}
			}
			if(!info.equals("")){
				info = info.substring(0,Math.min(80,info.length()));
				IPath path = new Path(getFileNameFromReference(ref.toString()));
				IFile file = project.getFile(path);
				//openFile(file);
				ITextViewer textViewer = textViewers.get(file);
				IDocument doc = textViewer.getDocument();
				try {
					int char_start = doc.getLineOffset(getLine1(ref.toString()) - 1) + getChar1(ref.toString()) - 1;
					int char_end = doc.getLineOffset(getLine2(ref.toString()) - 1) + getChar2(ref.toString()) - 1;
					JSMarker.addMarker(file, getLine1(ref.toString()), char_start, char_end, info);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void printNumericInfo(List<Data> metricsToColor){
		if(metricsToColor.size() == 0){
			return;
		}
		HashMap<IFile,ArrayList<Object[]>> sortRegions = new HashMap<IFile,ArrayList<Object[]>>();
		for(Object ref : references.keySet()){
			JSONObject metrics = (JSONObject) references.get(ref);
			for(Object metricName : metrics.keySet()){
				for(Data data : metricsToColor){
					if(data.getMetricName().equals(metricName)){
						Float value = ((Number)metrics.get(metricName)).floatValue();
						Color color = ColorUtils.interpolate(new Color(Display.getDefault(), data.getMinColor()), new Color(Display.getDefault(),data.getMaxColor()), Float.parseFloat(data.getMinValue()), Float.parseFloat(data.getMaxValue()), value);
						IPath path = new Path(getFileNameFromReference(ref.toString()));
						IFile file = project.getFile(path);
						ITextViewer textViewer = textViewers.get(file);
						IDocument doc = textViewer.getDocument();
						try {
							int char_start = doc.getLineOffset(getLine1(ref.toString()) - 1) + getChar1(ref.toString()) - 1;
							int char_end = doc.getLineOffset(getLine2(ref.toString()) - 1) + getChar2(ref.toString()) - 1;
							Object[] offsets = new Object[3];
							offsets[0] = char_start;
							offsets[1] = char_end;
							offsets[2] = color;
							ArrayList<Object[]> regions = sortRegions.get(file);
							if(regions == null){
								regions = new ArrayList<Object[]>();
							}
							regions.add(offsets);
							sortRegions.put(file, regions);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					} 
				}	
			}
		}

		for(Entry<IFile, ArrayList<Object[]>> entry : sortRegions.entrySet()) {
			IFile file = entry.getKey();
			ArrayList<Object[]> regions = entry.getValue();
			ITextViewer textViewer = textViewers.get(file);
			StyleRange[] old = oldStyles.get(file);
			
			LineStyle linestyle = new LineStyle();
			TextPresentation tp = new TextPresentation();
			tp.mergeStyleRanges(old);
			linestyle.setTextPresentation(tp);
			linestyle.setOffsets(regions);
			listeners.put(file, linestyle);
			textViewer.getTextWidget().addLineStyleListener(linestyle);

			//StyleRange[] newStyles = linestyle.getStyles().toArray(new StyleRange[linestyle.getStyles().size()]);
			//oldStyles.put(file, newStyles);
			
			textViewer.getTextWidget().redraw();
		}
	}
	
	public void clear(){
		for(String filename : files){
			IPath path = new Path(filename);
			IFile file = project.getFile(path);
			
			JSMarker.deleteMarkers(file);
			
			/*ITextViewer textViewer = textViewers.get(file);
			textViewer.getTextWidget().removeLineStyleListener(listeners.get(file));
			
			StyleRange[] old = oldStyles.get(file);*/
			/*for(int i=0;i<old.length;i++){
				System.out.println(old[i]);
			}*/
			/*LineStyle linestyle = new LineStyle();
			TextPresentation tp = new TextPresentation();
			tp.mergeStyleRanges(old);
			linestyle.setTextPresentation(tp);
			textViewer.getTextWidget().addLineStyleListener(linestyle);
			textViewer.getTextWidget().redraw();*/
			
		}
	}

	public String getTitle(String metricName){
		return (String)((JSONObject)declarations.get(metricName)).get("title");
	}

	public String getType(String metricName){
		return (String)((JSONObject)declarations.get(metricName)).get("type");
	}

	public float getMax(String metricName){
		JSONObject metricDeclarations = (JSONObject) declarations.get(metricName);
		if(metricDeclarations.get("type").equals("number") || metricDeclarations.get("type").equals("percent")){
			float max = 0;
			for(Object ref : references.keySet()){
				JSONObject metrics = (JSONObject) references.get(ref);
				for(Object metric : metrics.keySet()){
					if(((String)metric).equals(metricName)){
						Float value = ((Number)metrics.get(metric)).floatValue();
						if(value > max){
							max = value;
						}
					}
				}
			}
			return max;
		}
		else{
			return 0;
		}
	}

	public void openFile(IFile file) throws PartInitException{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		if(page.findEditor(new FileEditorInput(file)) == null){
			page.openEditor(new FileEditorInput(file), desc.getId());
		}
	}

	private String getFileNameFromReference(String reference){
		String fileName = reference.split("@")[0].replaceAll("\"", "");
		return fileName;
	}

	private String getRangeFromReference(String reference){
		return reference.split("@")[1];
	}

	private int getLine1(String reference){
		return Integer.parseInt(getRangeFromReference(reference).split("-")[0].split("\\.")[0]);
	}

	private int getChar1(String reference){
		return Integer.parseInt(getRangeFromReference(reference).split("-")[0].split("\\.")[1]);
	}

	private int getLine2(String reference){
		return Integer.parseInt(getRangeFromReference(reference).split("-")[1].split("\\.")[0]);
	}

	private int getChar2(String reference){
		return Integer.parseInt(getRangeFromReference(reference).split("-")[1].split("\\.")[1]);
	}

	/**
	 * @return the declarations
	 */
	public JSONObject getDeclarations() {
		return declarations;
	}

	/**
	 * @param declarations the declarations to set
	 */
	public void setDeclarations(JSONObject declarations) {
		this.declarations = declarations;
	}

	/**
	 * @return the maps
	 */
	public JSONObject getMaps() {
		return maps;
	}

	/**
	 * @param maps the maps to set
	 */
	public void setMaps(JSONObject maps) {
		this.maps = maps;
	}

	/**
	 * @return the references
	 */
	public JSONObject getReferences() {
		return references;
	}

	/**
	 * @param references the references to set
	 */
	public void setReferences(JSONObject references) {
		this.references = references;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the project
	 */
	public IProject getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(IProject project) {
		this.project = project;
	}
}
