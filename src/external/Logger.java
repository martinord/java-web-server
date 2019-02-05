package external;


import core.definitions.ReadFile;
import core.definitions.ReplyType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 *
 * @author martinord
 */
public class Logger {
    
    private static String DEFAULT_PATH = "server_files/LOG/";
    private static String DEFAULT_ERROR = "errors.log";
    private static String DEFAULT_ACCESS = "accesses.log";
    private String PATH;
    private File accfile;
    private File errfile;
    
    private PrintWriter error;
    private PrintWriter access;
    
    // Constructors
    
    public Logger(String path, String accfile, String errfile){
        this.PATH = path;
        start(accfile, errfile);
    }
    
    public Logger(String path){
        this.PATH = path;
        start(DEFAULT_ACCESS, DEFAULT_ERROR);
    }
    
    public Logger(){
        this(DEFAULT_PATH);
    }
    
    // Getters
    
    public String getLogFile(){
        return accfile.getName();
    }
    
    public String getErrFile(){
        return errfile.getName();
    }
    
    
    // Other methods
    
    private void start(String afile, String efile) {
        
        if(!(new File(PATH)).exists())
            (new File(PATH)).mkdir();
        
        this.accfile = new File(PATH+afile);
        this.errfile = new File(PATH+efile);
        
        try {
            errfile.createNewFile();
            accfile.createNewFile();
            this.access = new PrintWriter(accfile);
            access.println("****ACCESSES FILE ****");
            access.println();
            this.error = new PrintWriter(errfile);
            error.println("****ERRORS FILE ****");
            error.println();
            
            access.flush();
            error.flush();
        } catch(IOException e){
            System.err.println("Error creating files: " + e.getMessage());
        }
        
    }
    
    public synchronized void logAccess(String reqLine, String ip, int statuscode, long size){
        access.println("Request line: " + reqLine);
        access.println("Client IP address: " + ip);
        access.println("Date: " + ReadFile.dateFormat.format(Calendar.getInstance().getTime()));
        access.println("Status code: " + statuscode);
        access.println("Size: " + size);
        access.println("--------------------------------------------------------");
        access.flush();
    }
    
    public synchronized void logError(String reqLine, String ip, int statuscode){
        error.println("Request line: " + reqLine);
        error.println("Client IP address: " + ip);
        error.println("Date: " + ReadFile.dateFormat.format(Calendar.getInstance().getTime()));
        error.println("Error message: " + statuscode + " - " + ReplyType.getByCode(statuscode).description());
        error.println("----------------------------------------------------------");
        error.flush();
    }
    
    public void close(){
        error.flush();
        error.close();
        access.flush();
        access.close();
    }
}
