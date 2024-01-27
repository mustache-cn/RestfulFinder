package cn.com.mustache.plugins.restful.finder.constant;

/**
 * @author steven
 */
public class RestfulException extends RuntimeException {
    private int code;

    private String msg;

    public RestfulException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public RestfulException(int code, Exception ex) {
        super(ex);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
