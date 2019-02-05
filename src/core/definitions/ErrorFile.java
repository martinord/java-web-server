package core.definitions;

/**
 *
 * @author martinord
 */
public enum ErrorFile {
    BR(400, "Bad Request", "BadRequest400.html"),
    NF(404, "Not Found", "NotFound404.html");

    ErrorFile(int code, String description, String filepath){
        this.code = code;
        this.description = description;
        this.file = filepath;

    }
    
    private int code;
    private String description;
    private String file;
    
    public int code(){return code;}
    public String description(){return description;}
    public String file(){return file;};
    
    public static ErrorFile getByCode(int code){
        ErrorFile errf = null;
        
        switch(code){
            case 400: errf = ErrorFile.BR; break;
            case 404: errf = ErrorFile.NF; break;                
        }
        
        return errf;
    }
}
