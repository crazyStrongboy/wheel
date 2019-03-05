package github.com.mars_jun.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class ToolKit {
    public static long getLong(HttpServletRequest request, String key) {
        return getLong(request, key, 0L);
    }

    public static long getLong(HttpServletRequest request, String key, long defaultValue) {
        long res = defaultValue;
        String p = request.getParameter(key);
        try {
            if (StringUtils.isNotBlank(p))
                res = Long.parseLong(p);
        } catch (NumberFormatException localNumberFormatException) {
        }
        return res;
    }

    public static String getString(HttpServletRequest request, String key) {
        return getString(request, key, "");
    }

    public static String getString(HttpServletRequest request, String key, String defaultValue) {
        String res = defaultValue;
        String p = request.getParameter(key);
        try {
            if (StringUtils.isNotBlank(p))
                res = p;
        } catch (NumberFormatException localNumberFormatException) {
        }
        return res;
    }

    public static int getInt(HttpServletRequest request, String key) {
        return getInt(request, key, 0);
    }

    public static int getInt(HttpServletRequest request, String key, int defaultValue) {
        int res = defaultValue;
        String p = request.getParameter(key);
        try {
            if (StringUtils.isNotBlank(p))
                res = Integer.parseInt(p);
        } catch (NumberFormatException localNumberFormatException) {
        }
        return res;
    }
}