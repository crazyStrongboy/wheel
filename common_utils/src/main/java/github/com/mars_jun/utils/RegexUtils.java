package github.com.mars_jun.utils;

import java.util.regex.Pattern;

public class RegexUtils {
    public static final String intege = "^-?[1-9]\\d*$/";
    public static final String intege1 = "^[1-9]\\d*$/";
    public static final String intege2 = "^-[1-9]\\d*$/";
    public static final String num = "^([+-]?)\\d*\\.?\\d+$/";
    public static final String num1 = "^[1-9]\\d*|0$/";
    public static final String num2 = "^-[1-9]\\d*|0$/";
    public static final String decmal = "^([+-]?)\\d*\\.\\d+$/";
    public static final String decmal1 = "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$";
    public static final String decmal2 = "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$";
    public static final String decmal3 = "^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$";
    public static final String decmal4 = "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$";
    public static final String decmal5 = "^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$";
    public static final String email = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    public static final String color = "^[a-fA-F0-9]{6}$";
    public static final String url = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-.\\/?%&=]*)?$";
    public static final String chinese = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";
    public static final String ascii = "^[\\x00-\\xFF]+$";
    public static final String zipcode = "^\\d{6}$";
    public static final String mobile = "^(13|15|16|18)[0-9]{9}$";
    public static final String ip4 = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";
    public static final String notempty = "^\\S+$";
    public static final String picture = "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga|JPG|BMP|GIF|ICO|PCX|JPEG|TIF|PNG|RAW|TGA)$";
    public static final String audio = "(.*)\\.(mp3|wma|mid|midi|wav|vqf|MP3|WMA|MID|MIDI|WAV|VQF)$";
    public static final String video = "(.*)\\.(rm|3gp|mp4|rmvb|avi|wmv|flv|vob|exe|mkv|swf|RM|3GP|MP4|RMVB|AVI|WMV|FLV|VOB|EXE|MKV|SWF)$";
    public static final String rar = "(.*)\\.(rar|zip|7zip|tgz|RAR|ZIP|7ZIP|TGZ)$";
    public static final String date = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";
    public static final String datetime = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}(\\s\\d{2}:)?(\\d{2}:)?(\\d{2})?$";
    public static final String qq = "^[1-9]*[1-9][0-9]*$";
    public static final String tel = "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$";
    public static final String username = "^[A-Za-z]\\w{5,}$";
    public static final String allstring = "^\\w+$";
    public static final String letter = "^[A-Za-z]+$";
    public static final String letter_u = "^[A-Z]+$";
    public static final String letter_l = "^[a-z]+$";
    public static final String idcard = "^[1-9]([0-9]{14}|[0-9]{17})$";
    public static final String numOrStr = "^[A-Za-z0-9]+$";

    public static boolean test(String input, String regx) {
        return Pattern.matches(regx, input);
    }
}
