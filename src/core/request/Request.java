package core.request;


import core.server.Server;
import core.definitions.DirectoryFile;
import core.definitions.ReadFile;
import java.io.DataOutputStream;
import java.net.Socket;


/**
 * Class to process a request of HTML in a server
 * @author martinord
 */
public class Request {
    
    private Socket socket;    // Client socket
    private String[] request; // Request from the client
    private DataOutputStream out;   // Stream out of the socket
    private String[] requestLine;   // Request line of the whole request
    private static String DIRECTORY_FILE = Server.OPTIONS.getDirectoryIndex();   // Get default file when requesting directory
    
    public Request(String[] request, Socket socket){
        this.socket = socket;
        this.request = request;
        this.requestLine = request[0].split(" "); // we split the request line in words
    }
    
    // GETTERS
    public String[] getRequest(){
        return request;
    }
    
    public DataOutputStream getDataOutputStream(){
        return out;
    }
      
    private String getIfModified(){
        String s;
        
        for(int i = 1; i<request.length; i++){  // Read the headers
            s = request[i];
            if(s == null)
                break;
            if(s.startsWith("If-Modified-Since"))
                return s.split("\\: ")[1].trim();
        }
        return "";
    }
    
    private Boolean hasChanged(String file){
        
        String ifmod = getIfModified();
        
        return (requestLine[0].equals("GET")) &&
                (ifmod.equals(ReadFile.getDate(file)));

    }
    
    private Reply getReply(){
  
        String file = requestLine[1];   // Get the file requested
        
        if(file.equals("/")){
            file = DIRECTORY_FILE;
            // return processDirectoryFile(file); // IMPLEMENTS ALLOW
        }
            
        
        if(!ReadFile.isAvailable(file)){ // Check the file does not exists (NotFound error)
            Server.logger.logError(request[0], socket.getInetAddress().toString(), 404);
            return new Reply(404, file);
        }
        
        if(hasChanged(file)){  // If the file has not been modified
            Server.logger.logAccess(request[0], socket.getInetAddress().toString(), 
                                    304, ReadFile.getSize(file));
            return new Reply(304, file);
        }
        
        Server.logger.logAccess(request[0], socket.getInetAddress().toString(), 
                                    200, ReadFile.getSize(file));
        return new Reply(200, file);    // The request is standard and OK
    }
    
    private void processBadRequest(){
        Reply reply = new Reply(400, "");
        Server.logger.logError(request[0], socket.getInetAddress().toString(), 400);
        reply.send(out);
    }
    
    public void process(){
        
        Reply reply;
        
        try {
            this.out = new DataOutputStream(socket.getOutputStream());
            
            if(requestLine.length<3){
                processBadRequest();
                return;
            }
            
            reply = getReply();
            
            switch(requestLine[0]){
                case "GET":  reply.send(out); break;
                case "HEAD": reply.sendHeaders(out); break;

                default: processBadRequest(); // Bad request
            }
            
        } catch (Exception e){
            System.err.println("Error in request: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
    // NOT AVAILABLE
    public Reply processDirectoryFile(String file){
        
        if(!file.endsWith("/"))
            return null;            // The file is not a directory

        if(ReadFile.isAvailable(DIRECTORY_FILE)){
            Server.logger.logAccess(request[0], socket.getInetAddress().toString(), 
                                    200, ReadFile.getSize(DIRECTORY_FILE));
            return new Reply(200, DIRECTORY_FILE);
            
        }
        
        // If the file is not available
        
        if(Server.OPTIONS.getAllow()){
            String f = DirectoryFile.getIndexHTML(file);
            Server.logger.logAccess(request[0], socket.getInetAddress().toString(), 
                                    200, ReadFile.getSize(f));
            return new Reply(200, f);
        }
        
        Server.logger.logError(request[0], socket.getInetAddress().toString(), 403);
        return new Reply(403, "");
    }
      
    
}

