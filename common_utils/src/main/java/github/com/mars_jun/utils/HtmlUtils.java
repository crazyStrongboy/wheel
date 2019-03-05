package github.com.mars_jun.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class HtmlUtils {
    public static String htmlEncode(String txt) {
        txt = txt.replaceAll("&", "&amp;");
        txt = txt.replaceAll("&amp;amp;", "&amp;");
        txt = txt.replaceAll("&amp;quot;", "&quot;");
        txt = txt.replaceAll("\"", "&quot;");
        txt = txt.replaceAll("&amp;lt;", "&lt;");
        txt = txt.replaceAll("<", "&lt;");
        txt = txt.replaceAll("&amp;gt;", "&gt;");
        txt = txt.replaceAll(">", "&gt;");
        txt = txt.replaceAll("&amp;nbsp;", "&nbsp;");

        return txt;
    }

    public static String HtmlUnicode(String txt) {
        txt = txt.replaceAll("&nbsp;", "&#160;");
        txt = txt.replaceAll("&quot;", "&#34;");
        txt = txt.replaceAll("&lt;", "&#60;");
        txt = txt.replaceAll("&gt;", "&#62;");

        return txt;
    }

    public static String unHtmlEncode(String txt) {
        txt = txt.replaceAll("&amp;", "&");
        txt = txt.replaceAll("&quot;", "\"");
        txt = txt.replaceAll("&lt;", "<");
        txt = txt.replaceAll("&gt;", ">");
        txt = txt.replaceAll("&nbsp;", " ");

        return txt;
    }

    public static String unHtmlUnicode(String txt) {
        txt = txt.replaceAll("&#160;", " ");
        txt = txt.replaceAll("&#34;", "\"");
        txt = txt.replaceAll("&60;", "<");
        txt = txt.replaceAll("&62;", ">");

        return txt;
    }

    public static String setHtmlTag(String input) {
        input = StringUtils.trim(input);
        input = input.replaceAll("<", "&lt;");
        input = input.replaceAll(">", "&gt;");
        input = input.replaceAll("\r", "");
        input = input.replaceAll("\n", "<br>");
        return input;
    }

    public static String getHtmlTag(String input) {
        input = StringUtils.trim(input);
        input = input.replaceAll("<br>", "\r\n");
        input = input.replaceAll("&lt;", "<");
        input = input.replaceAll("&gt;", ">");
        return input;
    }

    public static String markColor(String sw, String sTemp, String sColor) {
        String sReturn = "";
        int i = 0;
        int j = 0;
        int iTempLength = sTemp.length();
        int iLengthS1 = sw.length();
        String sTemp1 = sw.toLowerCase();
        String sTemp2 = sTemp.toLowerCase();
        while (true) {
            i = sTemp2.indexOf(sTemp1, j);
            if (i == -1) {
                sReturn = sReturn + sTemp.substring(j, iTempLength);
                break;
            }
            sReturn = sReturn + sTemp.substring(j, i) + "<font color=\"" + sColor + "\">" + sTemp.substring(i, i + iLengthS1) + "</font>";

            j = i + iLengthS1;
            if (j > iTempLength)
                j = iTempLength;
        }
        return sReturn;
    }

    public static String removeHtml(String content) {
        if (content == null) return "";

        try {
            Pattern p_html = Pattern.compile("<[^>]+>", 2);
            Matcher m_html = p_html.matcher(content);
            content = m_html.replaceAll("");
        } catch (Exception e) {
            return "";
        }
        Matcher m_html;
        Pattern p_html;
        return content;
    }

    public static String removeIframe(String content) {
        if (content == null) return "";

        try {
            Pattern p_html = Pattern.compile("<iframe[^>]+>", 2);
            Matcher m_html = p_html.matcher(content);
            content = m_html.replaceAll("");
        } catch (Exception e) {
            return "";
        }
        Matcher m_html;
        Pattern p_html;
        return content;
    }

    public static String removeStyle(String content) {
        if (content == null) return "";

        try {
            Pattern p_html = Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", 2);
            Matcher m_html = p_html.matcher(content);
            content = m_html.replaceAll("");
        } catch (Exception e) {
            return "";
        }
        Matcher m_html;
        Pattern p_html;
        return content;
    }

    public static String removeScript(String content) {
        if (content == null) return "";

        try {
            Pattern p_html = Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", 2);
            Matcher m_html = p_html.matcher(content);
            content = m_html.replaceAll("");
        } catch (Exception e) {
            return "";
        }
        Matcher m_html;
        Pattern p_html;
        return content;
    }

    public static String removeSpace(String content) {
        if (content == null) return "";
        return content.replaceAll("\\s*(\\r\\n)\\s*", "").replaceAll(">(\\s+)", ">").replaceAll("(\\s+)<", "<");
    }

    public static String regStr(String str, String pageUrl) {
        try {
            str = str.replaceFirst("name=filename", "name=\"filename\"");
            String patternString = "(<param\\s*name=\"[^\"]*\"\\s+value\\s*=\\s*\"?)(\\w{1,20}(.swf|.mp3|.wav|.avi)\"?)";
            Pattern pattern = Pattern.compile(patternString,
                    2);
            Matcher matcher = pattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, matcher.group(1) + pageUrl + matcher.group(2));
            }
            matcher.appendTail(sb);
            str = sb.toString();
            str = str.replaceAll("SRC=\"", "SRC=\"" + pageUrl);
            str = str.replaceAll("src=\"", "src=\"" + pageUrl);

            str = new String(str.getBytes("ISO-8859-1"), "GBK");
        } catch (PatternSyntaxException localPatternSyntaxException) {
        } catch (Exception localException) {
        }
        return str;
    }
}
