package github.com.mars_jun.utils;

public class WebAppUtils {
    private static final Class<WebAppUtils> CURR_CLASS = WebAppUtils.class;

    private static final String PACKAGE_NAME = CURR_CLASS.getPackage().getName().replaceAll("\\.", "/");

    private static final String BASE_PATH = CURR_CLASS.getResource("").getPath().replaceAll("%20", " ");

    public static final String WEBAPP_PATH = BASE_PATH.substring(0, BASE_PATH.indexOf("WEB-INF"));

    public static final String CLASS_PATH = BASE_PATH.replaceAll("/" + PACKAGE_NAME, "");
}