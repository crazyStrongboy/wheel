package github.com.mars_jun.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpClientUtils {
    public static HttpClient getHttpClient(String host, int port, String protocol, int connectionTimeout, int soTimeout) {
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(host, port, protocol);
        if (connectionTimeout != 0)
            client.getHttpConnectionManager().getParams()
                    .setConnectionTimeout(connectionTimeout);
        if (soTimeout != 0)
            client.getHttpConnectionManager().getParams()
                    .setSoTimeout(soTimeout);
        return client;
    }

    public static GetMethod getGetMethod(String url) {
        if ((url == null) || ("".equals(url))) {
            return new GetMethod();
        }
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter("http.method.retry-handler",
                new DefaultHttpMethodRetryHandler());
        getMethod.getParams().setParameter(
                "http.protocol.single-cookie-header", Boolean.valueOf(true));
        getMethod.getParams().setParameter("http.protocol.cookie-policy",
                "compatibility");
        return getMethod;
    }

    public static String getHttpClientResponseString(HttpClient client, GetMethod getMethod, String charsetName)
            throws HttpException, IOException {
        StringBuffer resBuffer = new StringBuffer();
        int sttatusCode = client.executeMethod(getMethod);
        if (sttatusCode == 200) {
            InputStream resStream = getMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(resStream, charsetName));
            String resTemp = "";
            while ((resTemp = br.readLine()) != null) {
                resBuffer.append(resTemp);
            }
        }
        return resBuffer.toString();
    }
}
