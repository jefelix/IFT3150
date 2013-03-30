package ca.umontreal.ift3150.js.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ca.umontreal.ift3150.js.editor.JSMarker;
import ca.umontreal.ift3150.js.util.ColorUtils;

public class AnalysisFileParser {

	private JSONParser parser;
	private String filePath;
	private IProject project;
	
	private HashMap<String, JSONObject> declarations;
	private HashMap<String, String> maps;
	private HashMap<String, String> references;

	private static String STRING_DECLARATIONS = "declarations";
	private static String STRING_RESULTS = "results";
	private static String STRING_MAPS = "maps";
	
	public AnalysisFileParser(String filePath, IProject project){
		this.parser = new JSONParser();
		this.filePath = filePath;
		this.project = project;
	}
	
	public void initiliazeParser(){
		this.declarations = parseDeclarations();
		this.maps = parseMaps();
		this.references = printTextInfo();
	}
	
	public HashMap<String, JSONObject> parseDeclarations(){
		HashMap<String, JSONObject> declarations = new HashMap<String, JSONObject>();
		try {
			Object obj = parser.parse(new FileReader(this.filePath));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject metriques = (JSONObject) jsonObject.get(STRING_DECLARATIONS);
			for(Object metrique : metriques.keySet()){
				JSONObject attributsMetrique = (JSONObject) metriques.get(metrique);
				declarations.put((String)metrique, attributsMetrique);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return declarations;
	}
	
	public HashMap<String, String> printTextInfo(){
		HashMap<String, String> references = new HashMap<String, String>();
		try {
			Object obj = parser.parse(new FileReader(this.filePath));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject results = (JSONObject) jsonObject.get(STRING_RESULTS);
			for(Object ref : results.keySet()){
				JSONObject metriques = (JSONObject) results.get(ref);
				String info = "";
				for(Object metrique : metriques.keySet()){
					String title = (String) declarations.get(metrique).get("title");
					String type = (String) declarations.get(metrique).get("type");
					info += title+": ";
					if(type.equals("absval")){
						if(metriques.get(metrique) instanceof JSONArray){
							JSONArray array = (JSONArray) metriques.get(metrique);
							Iterator<JSONObject> iterator = array.iterator();
							while (iterator.hasNext()) {
								JSONObject attributs = iterator.next();
								for(Object attribut : attributs.keySet()){
									if(maps.containsKey(attributs.get(attribut))){
										info += attribut+": "+maps.get(attributs.get(attribut))+" ";
									}
									else {
										info += attribut+": "+attributs.get(attribut)+" ";
									}
								}
							}
						}
						else{
							info += metriques.get(metrique)+" ";
						}
					}
					else{
						info += metriques.get(metrique)+" ";
					}
					
				}
				//System.out.println(info);
				info = info.substring(0,Math.min(50,info.length()));
				IPath path = new Path(getFileNameFromReference(ref.toString()));
			    IFile file = project.getFile(path);
			    openFile(file);
			    IDocumentProvider dp = new TextFileDocumentProvider();
			    try {
			    	dp.connect(file);
			    	IDocument doc = dp.getDocument(file);
			    	int char_start = doc.getLineOffset(getLine1(ref.toString()) - 1) + getChar1(ref.toString()) - 1;
			    	int char_end = doc.getLineOffset(getLine2(ref.toString()) - 1) + getChar2(ref.toString()) - 1;
			    	String range = char_start+":"+char_end;
			    	references.put(range, info);
			    	//JSMarker.addMarker(file, getLine1(ref.toString()), char_start, char_end, info);
			    } catch (CoreException e) {
			    	e.printStackTrace();
			    } catch (BadLocationException e) {
			    	e.printStackTrace();
			    }
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (PartInitException e1) {
			e1.printStackTrace();
		} 
		return references;
	}
	
	public void printNumericInfo(){
		//metriques a colorer
		ArrayList<String> listesMetriques = new ArrayList<String>();
		listesMetriques.add("count");
		Color start = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		Color end = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		float minValue = 0;
		float maxValue = 20;
		
		try {
			Object obj = parser.parse(new FileReader(this.filePath));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject results = (JSONObject) jsonObject.get(STRING_RESULTS);
			for(Object ref : results.keySet()){
				JSONObject metriques = (JSONObject) results.get(ref);
				//String info = "";
				for(Object metrique : metriques.keySet()){
					if(listesMetriques.contains(metrique)){
						String title = (String) declarations.get(metrique).get("title");
						String type = (String) declarations.get(metrique).get("type");
						//info += title+": ";
						Float value = ((Number)metriques.get(metrique)).floatValue();
						Color color = ColorUtils.interpolate(start, end, minValue, maxValue, value);
						IPath path = new Path(getFileNameFromReference(ref.toString()));
					    IFile file = project.getFile(path);
					    //openFile(file);
					    IDocumentProvider dp = new TextFileDocumentProvider();
					    try {
					    	dp.connect(file);
					    	IDocument doc = dp.getDocument(file);
					    	int char_start = doc.getLineOffset(getLine1(ref.toString()) - 1) + getChar1(ref.toString()) - 1;
					    	int char_end = doc.getLineOffset(getLine2(ref.toString()) - 1) + getChar2(ref.toString()) - 1;
					    	IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(new FileEditorInput(file));
					    	if (editorPart != null) {
					    	    ITextOperationTarget target = (ITextOperationTarget)editorPart.getAdapter(ITextOperationTarget.class);
					    	    if (target instanceof ITextViewer) {
					    	        ITextViewer textViewer = (ITextViewer)target;
					    	        System.out.println(file.getName());
					    	        ColorUtils.colore(textViewer, char_start, char_end, color);
					    	    } 
					    	}
					    } catch (CoreException e) {
					    	e.printStackTrace();
					    } catch (BadLocationException e) {
					    	e.printStackTrace();
					    }
					}
										
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> parseMaps(){
		HashMap<String, String> maps = new HashMap<String, String>();
		try {
			Object obj = parser.parse(new FileReader(this.filePath));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject jsonMaps = (JSONObject) jsonObject.get(STRING_MAPS);
			for(Object key : jsonMaps.keySet()){
				maps.put((String)key, (String) jsonMaps.get(key));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return maps;
	}
	
	public void openFile(IFile file) throws PartInitException{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		if(page.findEditor(new FileEditorInput(file)) == null){
			page.openEditor(new FileEditorInput(file), desc.getId());
		}
	}
	
	public String findRegion(int start, int end){
		System.out.println("vedvb");
		String range = start+":"+end;
		String hover = references.get(range);
		return hover;
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
	public HashMap<String, JSONObject> getDeclarations() {
		return declarations;
	}

	/**
	 * @param declarations the declarations to set
	 */
	public void setDeclarations(HashMap<String, JSONObject> declarations) {
		this.declarations = declarations;
	}
	
	/**
	 * @return the maps
	 */
	public HashMap<String, String> getMaps() {
		return maps;
	}

	/**
	 * @param maps the maps to set
	 */
	public void setMaps(HashMap<String, String> maps) {
		this.maps = maps;
	}
	
	/**
	 * @return the references
	 */
	public HashMap<String, String> getReferences() {
		return references;
	}

	/**
	 * @param references the references to set
	 */
	public void setReferences(HashMap<String, String> references) {
		this.references = references;
	}
}
