package cn.com.mustache.plugins.restful.finder.uitl;

import org.jetbrains.annotations.NotNull;

/**
 * @author steven
 */
public abstract class StringUtil {

    @NotNull
    public static String removeRedundancyMarkup(String pattern) {
        if (pattern == null) {
            return "";
        }

        String localhostRegex = "(http(s?)://)?(localhost)(:\\d+)?";
        String hostAndPortRegex = "(http(s?)://)?(([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}|((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?))";

        pattern = pattern.replaceAll(localhostRegex, "");
        pattern = pattern.replaceAll(hostAndPortRegex, "");

        if (!pattern.contains("?")) {
            return pattern;
        }
        return pattern.substring(0, pattern.indexOf("?"));
    }
}
