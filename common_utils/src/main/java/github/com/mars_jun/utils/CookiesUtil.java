package github.com.mars_jun.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookiesUtil {
    public static final int MAXAGE = 86400;

    public static String loadCookie(String key, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(key)) return cookie.getValue();
            }
        }
        return null;
    }

    public static void write(String domain, String name, String value, String path, int maxage, HttpServletResponse response, HttpServletRequest request) {
        if (loadCookie(name, request) != null) remove(name, null, response, request);
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxage);
        response.addCookie(cookie);
    }

    public static void write(String domain, String name, String value, String path, HttpServletResponse response, HttpServletRequest request) {
        if (loadCookie(name, request) != null) remove(name, null, response, request);
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void write(String name, String value, int maxage, HttpServletResponse response, HttpServletRequest request) {
        if (loadCookie(name, request) != null) remove(name, null, response, request);
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(getDomain(request));
        cookie.setPath("/");
        cookie.setMaxAge(maxage);
        response.addCookie(cookie);
    }

    public static void write(String domain, String name, String value, HttpServletResponse response, HttpServletRequest request) {
        if (loadCookie(name, request) != null) remove(name, null, response, request);
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void write(String name, String value, HttpServletResponse response, HttpServletRequest request) {
        if (loadCookie(name, request) != null) remove(name, null, response, request);
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(getDomain(request));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void remove(String key, String domain, HttpServletResponse resp, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(key)) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    resp.addCookie(cookie);
                }
            }
    }

    public static void removeAll(String domain, HttpServletResponse resp, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie != null) {
                    cookie.setDomain(domain);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    resp.addCookie(cookie);
                }
            }
    }

    public static void removeAll(HttpServletResponse resp, HttpServletRequest req) {
        removeAll(getDomain(req), resp, req);
    }

    public static String getDomain(HttpServletRequest req) {
        return req.getServerName().replaceAll("^([^.]+\\.)+", "$1");
    }
}
