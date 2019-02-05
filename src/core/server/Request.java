package core.server;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to process a request of HTML in a server
 * @author martinord
 */
public class Request {
    
    private String[] request; // Request from the client
    private Socket socket;  // The client socket
    private DataOutputStream out;   // Stream out of the socket
    private String[] requestLine;   // Request line of the whole request
    
    public Request(String[] request){
        this.request = request;
        this.requestLine = request[0].split(" "); // we split the request line in words
    }
    
    public String[] getRequest(){
        return request;
    }
    
    public Socket getSocket(){
        return socket;
    }
    
    private void badRequest() throws IOException{
        out.writeBytes("HTTP/1.0 400 Bad Request\n" + "\n\n");
    }
    
    private void okRequest() throws IOException{
        out.writeBytes("HTTP/1.0 200 OK\n" + "Content-Type: text/html\n" + "\n\n");
    }
    
    private void processGet() throws IOException{
        String file = requestLine[1];   // Get the file requested
        okRequest();
        ReadFile.sendBytes(file, out);
    }
    
    private void processHead(){
        
    }
    
    public void process(Socket socket){
        
        this.socket = socket;
        
        try {
            this.out = new DataOutputStream(this.socket.getOutputStream());

            switch(requestLine[0]){
                case "GET": processGet(); break;
                case "HEAD": processHead(); break;

                default: badRequest();         
            }

            out.close();
        } catch (IOException ex) {
            System.err.println("Error in request: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
