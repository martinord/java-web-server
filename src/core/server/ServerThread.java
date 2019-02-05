package core.server;

import core.request.Request;
import java.net.*;
import java.io.*;

/**
 * Thread attending a connection of the TCP server
 * @author martinord
 */
public class ServerThread extends Thread{
    
    // ATTRIBUTES
    private static int MAX_REQ_LINES = 20;
    private Socket socket;
    
    
    public Socket getSocket(){
        return socket;
    }
    
    
    public ServerThread(Socket s){
        socket = s;
    }
    
    public void run(){
        
        BufferedReader in = null;
        OutputStream out = null;
        
        try{
            // Set input channel           
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));  
            out = socket.getOutputStream();
            
            // Set the time to wait to process the requests 
            socket.setSoTimeout(60*1000);
            
            // Receive the client messages
//            while(true){  // NOT AVAILABLE: when the client is closed, an infinite loop starts
                
                int i = 0;

                String requestString[] = new String[MAX_REQ_LINES];
                while((requestString[i]=(in.readLine()))!=null){
                    if(requestString[i].equals(""))
                        break;
                    else
                        i++;
                }
                if(requestString[0] != null && !requestString[0].equals("")){
                    Request request = new Request(requestString, socket);
                    request.process();
                }
//            }       
                
        } catch (SocketTimeoutException e){
            System.err.println("Nothing received in 60 secs ");
        } catch (NullPointerException e){
            System.err.println("Conection broken with client: " + e.getMessage());
        } catch (Exception e){
            System.err.println("Error in thread: " + e.getMessage());
        }finally{
            try {
                // Close the streams
                if(in != null && out != null)
                    in.close();
                    out.close();
                // Close socket
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
