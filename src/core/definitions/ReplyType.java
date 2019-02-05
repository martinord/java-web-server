package core.definitions;

/**
 * Reply type and error files.
 * @author martinord
 */
public enum ReplyType {
    OK(200, "OK", ""),
    BadRequest(400, "Bad Request", "BadRequest400.html"),
    NotFound(404, "Not Found", "NotFound404.html"),
    NotModified(304, "Not Modified", ""),
    Forbidden(403, "Access Forbidden", "Forbidden403.html");
    // NOTE: when adding new codes, it must be added to getByCode method

    ReplyType(int code, String description, String filepath){
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
    
    public static ReplyType getByCode(int code){
        ReplyType errf = null;
        
        switch(code){
            case 200: errf = ReplyType.OK; break;
            case 400: errf = ReplyType.BadRequest; break;
            case 404: errf = ReplyType.NotFound; break;
            case 304: errf = ReplyType.NotModified; break;
            case 403: errf = ReplyType.Forbidden; break;
        }
        
        return errf;
    }
}
