package github.com.mars_jun.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertieUtils {
    private Properties props = new Properties();
    private InputStream jndiInput = null;

    public PropertieUtils(String fileName) {
        try {
            this.jndiInput = PropertieUtils.class.getClassLoader()
                    .getResourceAsStream(fileName);
            this.props.load(this.jndiInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String name) {
        return this.props.getProperty(name);
    }

    public String getString(String name) {
        return this.props.getProperty(name);
    }

    public String getString(String name, String df) {
        return this.props.getProperty(name, df);
    }

    public Boolean getBoolean(String name) {
        String val = this.props.getProperty(name);
        return val == null ? null : Boolean.valueOf(val);
    }

    public Boolean getBoolean(String name, Boolean df) {
        String val = this.props.getProperty(name);
        return val == null ? df : Boolean.valueOf(val);
    }

    public Integer getInteger(String name) {
        String val = this.props.getProperty(name);
        return val == null ? null : Integer.valueOf(Integer.parseInt(val));
    }

    public Integer getInteger(String name, Integer df) {
        String val = this.props.getProperty(name);
        return Integer.valueOf(val == null ? df.intValue() : Integer.parseInt(val));
    }

    public Object set(String key, String value) {
        return this.props.setProperty(key, value);
    }

    public void close() {
        try {
            this.jndiInput.close();
        } catch (IOException localIOException) {
        }
    }

    public Set stringPropertyNames() {
        return this.props.stringPropertyNames();
    }
}