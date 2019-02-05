package core.server;


import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author martinord
 */
public abstract class FileReader {
    
    public static String path = "../server_files";
    
    public static void sendBytes(String file, DataOutputStream out) throws java.lang.IllegalArgumentException{
       FileInputStream source = null;
       
       try{
           source = new FileInputStream(path+file);
           
           int b = 0;

           while((b = source.read()) != -1){   // -1 is EOF
               out.write(b);
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
