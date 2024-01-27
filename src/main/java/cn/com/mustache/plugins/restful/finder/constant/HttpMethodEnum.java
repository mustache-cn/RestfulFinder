package cn.com.mustache.plugins.restful.finder.constant;

import java.util.Arrays;
import javax.swing.*;

/**
 * @author steven
 */
public enum HttpMethodEnum {

    /**
     * GET
     */
    GET("GET", Icons.GET),
    POST("POST", Icons.POST),
    PUT("PUT", Icons.PUT),
    DELETE("DELETE", Icons.DELETE),
    PATCH("PATCH", Icons.PATCH),
    UNKNOWN("UNKNOWN", Icons.UNKNOWN);

    private final String code;
    private final Icon icon;

    HttpMethodEnum(String code, Icon icon) {
        this.code = code;
        this.icon = icon;
    }

    public String getCode() {
        return code;
    }

    public Icon getIcon() {
        return icon;
    }

    public static HttpMethodEnum parse(String code) {
        return Arrays.stream(values()).filter(x -> x.getCode().equals(code)).findFirst().orElse(null);
    }
}
