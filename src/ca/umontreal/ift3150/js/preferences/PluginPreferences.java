package ca.umontreal.ift3150.js.preferences;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import ca.umontreal.ift3150.js.Activator;

/**
 * Permet de gérer (sauvegarder ou récupérer) des préférences pour le plugin
 *
 */
public class PluginPreferences {

	public static void savePref(String key, String value){
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences sub1 = preferences.node("analysisPreferences");
		sub1.put(key, value);
		try {
			preferences.flush();
			System.out.println("Sauvegarder!");
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public static String getPref(String key){
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences sub1 = preferences.node("analysisPreferences");
		String value = sub1.get(key, "");
		return value;
	}
}
