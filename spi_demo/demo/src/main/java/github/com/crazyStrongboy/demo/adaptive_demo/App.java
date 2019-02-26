package github.com.crazyStrongboy.demo.adaptive_demo;

import github.com.crazyStrongboy.inter.Driver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mars_jun
 * @version 2019/2/26 10:31
 */
public class App {

    private static final String MARS_JUN_DIR = "META-INF/mars_jun/";

    private static Map<String, Class<?>> extensionClasses = new HashMap<>();

    public static void main(String[] args) {
        // 加载Driver的实现类进入到缓存中
        loadDirectory(MARS_JUN_DIR, Driver.class);

//        print();
        String name = "";
        if (args.length == 1){
            name = args[0];
        }else {
            name = "mysql_driver";
        }

        Driver mysql_driver = getDriver(name);
        mysql_driver.connect("root");
    }

    /**
     * 获取某一个实现
     *
     * @param name
     * @return
     */
    private static Driver getDriver(String name) {
        Class<?> mysql_driver = extensionClasses.get(name);
        try {
            Object instance = mysql_driver.newInstance();
            if (instance instanceof Driver) {
                Driver driver = (Driver) instance;
                return driver;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void print() {
        extensionClasses.forEach(
                (key, value) -> {
                    try {
                        Object instance = value.newInstance();
                        if (instance instanceof Driver) {
                            Driver driver = (Driver) instance;
                            driver.connect("aa");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private static void loadDirectory(String dir, Class tClass) {
        String fileName = dir + tClass.getName();
        ClassLoader classLoader = App.class.getClassLoader();
        try {

            Enumeration<URL> resources = classLoader.getResources(fileName);
            if (resources != null) {
                while (resources.hasMoreElements()) {
                    URL url = resources.nextElement();
                    loadResource(classLoader, url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadResource(ClassLoader classLoader, URL resourceURL) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "utf-8"));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    final int ci = line.indexOf('#');
                    if (ci >= 0) {
                        line = line.substring(0, ci);
                    }
                    line = line.trim();
                    if (line.length() > 0) {
                        try {
                            String name = null;
                            int i = line.indexOf('=');
                            if (i > 0) {
                                name = line.substring(0, i).trim();
                                line = line.substring(i + 1).trim();
                            }
                            if (line.length() > 0) {
                                extensionClasses.put(name, Class.forName(line, true, classLoader));
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            } finally {
                reader.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}
