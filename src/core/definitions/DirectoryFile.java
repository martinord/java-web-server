package core.definitions;


import core.server.Server;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class that implements allow option, creates an html directory file.
 * @author martinord
 */
public abstract class DirectoryFile {
    
    private static String PATH = Server.OPTIONS.getDirectory() + File.separator;
    
    
    private static String getHeader(){
        return "<html> \n " +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<head>	\n" +
                "  <title> Directory </title> \n " +
                "</head> \n" +
                " <body text=\"#000000\" bgcolor=\"#ffffff\">\n";
    }
    
    
    private static String getCoda(){
        return "</body>\n" +
                "\n" +
                "</html>";
    }
    public static synchronized String getIndexHTML(String dir){
        // The file must be a directory!
        File mydir = new File(PATH+dir);
        
        
        if(!mydir.isDirectory())
            return null;
        
        String htmlFileName = null;
        
        try {
            if(!(new File(PATH+"temp/")).exists())
                (new File(PATH+"temp/")).mkdir();
            File html = new File(PATH+"temp/directory.html");
            htmlFileName = "temp/directory.html";
            html.createNewFile();
            
            PrintWriter writer = new PrintWriter(html);
            
            writer.println(getHeader());
            
            File files[] = mydir.listFiles();
            if(files == null){
                writer.println("<p>\n" +
                                "	Directory empty!\n" +
                                "</p>");
            }
            else{
                for(File f : files){
                    writer.println("<p>\n" +
                                    "  File: <a href=\"/"+ f.getName() +"\"/>"+ PATH+f.getName() +"</a>.\n" +
                                    "</p>");  
                }
            }
            
            writer.println();
            writer.println(getCoda());
            writer.flush();
            writer.close();
            return htmlFileName;
            
            
        } catch (IOException ex) {
            System.err.println("Error creating html index file: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return htmlFileName;
    }
    
}
