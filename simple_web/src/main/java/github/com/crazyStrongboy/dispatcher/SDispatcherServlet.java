package github.com.crazyStrongboy.dispatcher;

import github.com.crazyStrongboy.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;

public class SDispatcherServlet extends HttpServlet {
    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
    private Properties properties = new Properties();
    private Set<String> classNames = new HashSet<>();
    private Map<String, Object> ioc = new HashMap<>();
    private Map<String, Method> handlerMap = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        requestURI = getDistinctUri(requestURI);
        try {
            if (handlerMap.containsKey(requestURI)) {
                Method method = handlerMap.get(requestURI);
                Parameter[] parameters = method.getParameters();
                Map<String, String[]> parameterMap = req.getParameterMap();

                List<Object> params = new ArrayList<>();
                for (Parameter parameter : parameters) {
                    boolean match = false;
                    if (parameter.isAnnotationPresent(SRequestParam.class)) {
                        SRequestParam sRequestParam = parameter.getAnnotation(SRequestParam.class);
                        for (Map.Entry<String, String[]> paramMap : parameterMap.entrySet()) {
                            if (paramMap.getKey().equals(sRequestParam.value())) {
                                String param = paramMap.getValue()[0];
                                params.add(param);
                                match = true;
                            }
                        }
                    }
                    if (!match) {
                        params.add("");
                    }
                }
                String className = method.getDeclaringClass().getSimpleName();
                Object result = method.invoke(ioc.get(lowerCaseFirstLetter(className)), params.toArray());
                resp.getWriter().write(result.toString());
            } else {
                resp.getWriter().write("404 NOT FOUND!!!");
            }
        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
        }
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        System.err.println("contextConfigLocation : ... " + contextConfigLocation);
        //1.解析配置文件
        parseConfig(contextConfigLocation);
        //2.扫描包
        String scanPackages = properties.getProperty("scanPackages");
        scanPackages(scanPackages);
        //3.创建对象
        createBeans();
        //4.注入属性
        autowireProperty();
        //5.配置mapping映射
        configHandlerMapping();

    }

    private void configHandlerMapping() {
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            String url = "";
            if (!entry.getValue().getClass().isAnnotationPresent(SController.class)) {
                continue;
            }
            if (entry.getValue().getClass().isAnnotationPresent(SRequestMapping.class)) {
                SRequestMapping sRequestMapping = entry.getValue().getClass().getAnnotation(SRequestMapping.class);
                if (!"".equals(sRequestMapping.value()))
                    url += sRequestMapping.value();
            }

            Method[] methods = entry.getValue().getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(SRequestMapping.class)) {
                    SRequestMapping methodAnnotation = method.getAnnotation(SRequestMapping.class);
                    if ("".equals(methodAnnotation.value())) {
                        continue;
                    }
                    url += methodAnnotation.value();
                    url = getDistinctUri(url);
                    handlerMap.put(url, method);
                }
            }
        }
    }

    private String getDistinctUri(String url) {
        return url.replaceAll("/+", "/");
    }

    private void autowireProperty() {
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields)
                if (field.isAnnotationPresent(SAutowired.class)) {
                    SAutowired sAutowired = field.getAnnotation(SAutowired.class);
                    String simpleName = "";
                    if ("".equals(sAutowired.value())) {
                        simpleName = field.getType().getSimpleName();
                        simpleName = lowerCaseFirstLetter(simpleName);
                    } else {
                        simpleName = sAutowired.value();
                    }
                    field.setAccessible(true);
                    try {
                        field.set(entry.getValue(), ioc.get(simpleName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void createBeans() {
        try {
            for (String className : classNames) {
                className = className.replace(".class", "").trim();
                if (this.getClass().forName(className).isAnnotationPresent(SController.class)) {
                    Object instance = this.getClass().forName(className).newInstance();
                    String name = instance.getClass().getSimpleName();
                    addIoc(lowerCaseFirstLetter(name), instance);
                }
                if (this.getClass().forName(className).isAnnotationPresent(SService.class)) {
                    Object instance = this.getClass().forName(className).newInstance();
                    Class<?>[] interfaces = instance.getClass().getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        String simpleName = anInterface.getSimpleName();
                        addIoc(lowerCaseFirstLetter(simpleName), instance);
                    }
                    SService sService = instance.getClass().getAnnotation(SService.class);
                    if (!"".equals(sService.value())) {
                        addIoc(sService.value(), instance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addIoc(String name, Object instance) {
        if (ioc.containsKey(name)) {
            throw new RuntimeException("can,t register name ," + name + ", old instance :" + instance.getClass().getName());
        }
        ioc.put(name, instance);
    }

    private String lowerCaseFirstLetter(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void scanPackages(String scanPackages) {
        try {
            URL url = this.getClass().getClassLoader().getResource(scanPackages.replaceAll("\\.", "/"));
            String path = url.getFile();
            File file = new File(path);
            for (File listFile : file.listFiles()) {
                if (listFile.isDirectory()) {
                    System.out.println(listFile);
                    scanPackages(scanPackages + "." + listFile.getName());
                    continue;
                }
                if (listFile.getName().endsWith(".class")) {
                    classNames.add(scanPackages + "." + listFile.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseConfig(String contextConfigLocation) {
        if (contextConfigLocation.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
            contextConfigLocation = contextConfigLocation.substring(CLASSPATH_ALL_URL_PREFIX.length());
        }
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
