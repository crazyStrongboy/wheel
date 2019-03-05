package github.com.mars_jun.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class IpUtil {
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }

        }

        if ((ipAddress != null) && (ipAddress.length() > 15) &&
                (ipAddress.indexOf(",") > 0)) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }

    public static String getLocalIp()
            throws SocketException {
        Enumeration e1 = NetworkInterface.getNetworkInterfaces();
        String ip = "";
        while (e1.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) e1.nextElement();
            Enumeration e2 = ni.getInetAddresses();
            while (e2.hasMoreElements()) {
                InetAddress ia = (InetAddress) e2.nextElement();
                if ((ia instanceof Inet6Address))
                    continue;
                if ((ia.isLoopbackAddress()) ||
                        (ia.getHostAddress().indexOf(":") != -1)) continue;
                ip = ia.getHostAddress();
            }

        }

        return ip;
    }

    public static boolean isSuperIp(String userIp, List<String> superIPs) {
        boolean isSuperIP = false;
        if ((userIp == null) || (userIp.equals(""))) {
            return isSuperIP;
        }

        if ((superIPs == null) || (superIPs.equals(""))) {
            return isSuperIP;
        }

        int position = userIp.lastIndexOf(".") + 1;
        String strGroupUserIP = userIp.substring(0, position) + "*";
        for (Iterator iter = superIPs.iterator(); iter.hasNext(); ) {
            String strSuperIP = (String) iter.next();

            if (strSuperIP.indexOf("-") > 0) {
                String[] intervalIP = strSuperIP.split("-");
                int index = Integer.parseInt(intervalIP[0].substring(intervalIP[0].lastIndexOf(".") + 1));
                int end = Integer.parseInt(intervalIP[1].substring(intervalIP[1].lastIndexOf(".") + 1));
                String topIp = intervalIP[0].substring(0, intervalIP[0].lastIndexOf("."));
                for (int i = index; i <= end; i++) {
                    if (userIp.equals(topIp + "." + i)) {
                        isSuperIP = true;
                        break;
                    }
                }

            }

            if ((userIp.equals(strSuperIP)) || (strGroupUserIP.equals(strSuperIP))) {
                isSuperIP = true;
                break;
            }
        }
        return isSuperIP;
    }
}