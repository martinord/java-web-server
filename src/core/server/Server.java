package core.server;


import java.net.*;
import java.io.*;
import external.Logger;
import external.OptionReader;

/**
 * Multithread TCP echo server
 * 
 * @author martinord
 */
public class Server {
    
    public static OptionReader OPTIONS;
    public static Logger logger;           // Package accessible
    
    public static void main(String argv[]) {
       /* if (argv.length != 1){
            System.err.println("Format: ServerTCP <port>");
            System.exit(-1);
        }
        */
        Server.OPTIONS = new OptionReader();    // DEFAULT FILE
        Server.logger = new Logger();           // DEFAULT FILE
        
        ServerSocket socket = null;
        
        try{
            
            // Get port number    //int portNumber = Integer.parseInt(argv[0]);
            int portNumber = Integer.parseInt(OPTIONS.getPort());
            // Create a server socket and set timeout 300 sec
            socket = new ServerSocket(portNumber);
            socket.setSoTimeout(300*1000);
            
            while(true){
                // Wait for connections
                Socket clientSocket = socket.accept();
                ServerThread thr = new ServerThread(clientSocket);
                // Start the thread
                thr.start();
            }
        } catch (SocketTimeoutException e){
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            try {
                // Close socket
                socket.close();
                // Close logger
                logger.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
