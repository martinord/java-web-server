package external;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Class to manage the read of the options file of the web server
 * @author martinord
 */
public class OptionReader {
    
    private static String DEFAULT_FILE = "settings.conf";
    private static String PATH = "server_files/";
    private String file;
    private Properties reader;
    
    public OptionReader(String file){
        this.reader = new Properties();
        this.file = file;
        try {
            reader.load(new FileInputStream(PATH+file));
        } catch (FileNotFoundException ex) {
            System.err.println("Configuration file not valid!!!");
        } catch (IOException ex) {
            System.err.println("Error when reading configuration file: " + ex.getMessage());
        }
    }
    
    public OptionReader(){
        this(DEFAULT_FILE);  // Default file
    }
 
    public String getPort(){
        return reader.getProperty("PORT", "8080");
    }
    
    public String getDirectoryIndex(){
        return reader.getProperty("DIRECTORY_INDEX","/index.html");
    }
    
    public String getDirectory(){
        return reader.getProperty("DIRECTORY","server_files");
    }
    
    public Boolean getAllow(){
        return (reader.getProperty("ALLOW", "off").equals("on"));
    }
}
