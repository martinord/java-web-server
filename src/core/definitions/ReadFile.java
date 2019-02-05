package core.definitions;


import core.server.Server;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract class with methods that provide an interface for sending files
 * through a socket
 * @author martinord
 */
public abstract class ReadFile {
    
    private static int MAX_BUFFER = 8000;  // 8k bytes of buffer
    private static String PATH = Server.OPTIONS.getDirectory() + File.separator;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    
    public static Boolean isAvailable(String file){
        return (new File(PATH+file)).exists();
    }
    
    public static String getDate(String file){
        File f = new File(PATH+file);
        
        return (String) dateFormat.format(new Date(f.lastModified()));
    }
    
    public static long getSize(String file){
        File f = new File(PATH+file);
        
        return f.length();
    }
    
    public static void sendBytes(String file, DataOutputStream out) throws java.lang.IllegalArgumentException{
       FileInputStream source = null;
       
       try{
           source = new FileInputStream(PATH+file);
           
           int count = 0;       // Counter of the buffer
           
           byte buff[] = new byte[MAX_BUFFER];

           while((count = source.read(buff)) != -1){   // -1 is EOF
               out.write(buff, 0, count);           // Buffer optimize the read of the file
           }            
       }catch(IOException ex){
           throw new IllegalArgumentException(ex);
       }finally{      
           if(source != null)
               try {
                   source.close();
                } catch (IOException ex) {
                    System.err.println("Error reading file requested: " + ex.getMessage());
                    ex.printStackTrace();
                }
       }

    }
    
}
