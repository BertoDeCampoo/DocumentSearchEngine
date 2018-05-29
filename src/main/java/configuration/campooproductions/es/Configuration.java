package configuration.campooproductions.es;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalTime;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Class to load and retrieve application properties
 * @author Alberto Gutiérrez
 *
 */
public class Configuration {
	static Logger log = Logger.getLogger(Configuration.class.getName());
	private static String configFile = "src/main/resources/config.properties";
	private static Properties properties = null;
	
	private static void initializeProperties(){
		try{
			properties = new Properties();
			properties.load(new FileInputStream(configFile));
		}catch(Exception e){
			log.error(e.getMessage());
		}		
	}
	
	private static void saveProperties(){
		try{
			properties.store(new FileOutputStream(configFile), "Last update: " + LocalTime.now());
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
	
	public static void setBackupFile(String absolutePath) {
		if (properties == null)
			initializeProperties();
		properties.setProperty("BACKUP", absolutePath);
		saveProperties();
	}
	public static String getBackupFile(){
		if (properties == null)
			initializeProperties();
		return properties.getProperty("BACKUP");
	}
	
	public static void setAutoSave(boolean flag)
	{
		if (properties == null)
			initializeProperties();
		if(flag)
			properties.setProperty("AUTOSAVE", "true");
		else
			properties.setProperty("AUTOSAVE", "false");
		saveProperties();
	}
	
	public static boolean getAutoSave(){
		if (properties == null)
			initializeProperties();
		String status = properties.getProperty("AUTOSAVE");
		if(status.equalsIgnoreCase("true"))
			return true;
		else if(status.equalsIgnoreCase("false"))
			return false;
		return false;
	}
	
	public static void setAutoLoad(boolean flag)
	{
		if (properties == null)
			initializeProperties();
		if(flag)
			properties.setProperty("AUTOLOAD", "true");
		else
			properties.setProperty("AUTOLOAD", "false");
		saveProperties();
	}
	
	public static boolean getAutoLoad(){
		if (properties == null)
			initializeProperties();
		String status = properties.getProperty("AUTOLOAD");
		if(status.equalsIgnoreCase("true"))
			return true;
		else if(status.equalsIgnoreCase("false"))
			return false;
		return false;
	}
	
	public static void setTheme(String theme)
	{
		if (properties == null)
			initializeProperties();
		properties.setProperty("THEME", theme);
		saveProperties();
	}
	
	public static String getTheme()
	{
		if (properties == null)
			initializeProperties();
		return properties.getProperty("THEME");
	}
	
	public static void setDecorated(boolean flag)
	{
		if (properties == null)
			initializeProperties();
		if(flag)
			properties.setProperty("DECORATED", "true");
		else
			properties.setProperty("DECORATED", "false");
		saveProperties();
	}
	
	public static boolean getDecorated(){
		if (properties == null)
			initializeProperties();
		String status = properties.getProperty("DECORATED");
		if(status.equalsIgnoreCase("true"))
			return true;
		else if(status.equalsIgnoreCase("false"))
			return false;
		return false;
	}
}
