package core.request;


import core.definitions.ReplyType;
import core.definitions.ReadFile;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Types of replies
 * @author martinord
 */
public class Reply {
    
    private int code;
    private String file;
    private String description;
    private Boolean NotSendFile;    // True if the file needs not to be sent
    
    public Reply(int code, String file){
        this.code = code;
        this.NotSendFile = false;       // Assume the file is going to be sent
           
        ReplyType repType = ReplyType.getByCode(code);
        
        if(repType == null){
            this.file = ""; return;
        }
            
        this.description = repType.description();
        
        switch (repType) {
        // The request is OK
        case OK: this.file = file; break;
        // The request has some specified
        case NotModified: this.file = file; NotSendFile = true; break;
        default: this.file = repType.file(); break;
        }    

    }
    
    private String getFileType(){
        if(file.equals(""))
            return "application/octet-stream";
        
        String type;
        String[] name = file.split("\\.");

        if(name.length<2)
           return("application/octet-stream");
        
        switch(name[1]){
            case "html": type = "text/html"; break;
            case "txt": type = "text/plain"; break;
            case "gif": type = "image/gif"; break;
            case "jpg":
            case "jpeg": type = "image/jpeg"; break;
            
            default: type = "application/octet-stream";
        }
        
        return type;
    }
    
    private String headers(){
        String h = "";
        
        // Date
        h = h.concat("Date: " + ReadFile.dateFormat.format(Calendar.getInstance().getTime()) + "\n");
        // Server
        h = h.concat("Server: myServer/0.1 (Java)\n");
        // Content-type
        h = h.concat("Content-Type: "+ getFileType() +"\n");
        // Content-Length
        h = h.concat("Content-Length: " + ReadFile.getSize(file) + "\n");
        // Last-Modified (optional)
        h = h.concat("Last-Modified: " + ReadFile.getDate(file) + "\n");
        
        return h;
    }
    
    public void send(DataOutputStream out){ 
        sendHeaders(out);
        if(!NotSendFile)                // If a file is specified
            ReadFile.sendBytes(file, out);
    }
    
    public void sendHeaders(DataOutputStream out){
        try {
            out.writeBytes("HTTP/1.0 " + code +" " + description + "\n");
            out.writeBytes(headers());
            out.writeBytes("\n"); // blank line
        } catch (IOException ex) {
            System.err.println("Error in sending headers to the socket: " + ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
}
