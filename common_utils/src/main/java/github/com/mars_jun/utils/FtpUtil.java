package github.com.mars_jun.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class FtpUtil {
    private static final Logger log = Logger.getLogger(FtpUtil.class);
    private static FTPClient ftp;
    private static FtpUtil ftpUtil = new FtpUtil();

    public FtpUtil() {
        ftp = new FTPClient();
    }

    public static FtpUtil getInstance() {
        return ftpUtil == null ? new FtpUtil() : ftpUtil;
    }

    public boolean login(String url, int port, String username, String password)
            throws SocketException, IOException {
        ftp.connect(url, port);

        ftp.setControlEncoding("UTF-8");
        FTPClientConfig conf = new FTPClientConfig("WINDOWS");
        conf.setServerLanguageCode("zh");

        ftp.login(username, password);

        int reply = ftp.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            log.info("FTP服务器连接失败!");
            return false;
        }
        log.info("FTP服务器登录成功!");
        return true;
    }

    public void disconnect() {
        if (ftp.isAvailable()) {
            try {
                ftp.logout();
                log.info("FTP登录正常退出。");
            } catch (IOException e) {
                log.error("FTP登录退出异常:" + e.getMessage());
            }
        }
        if (ftp.isConnected())
            try {
                ftp.disconnect();
                log.info("FTP连接正常断开。");
            } catch (IOException e) {
                log.error("FTP断开连接异常:" + e.getMessage());
            }
    }

    public boolean uploadFile(String localAdr, String remoteAdr, InputStream input)
            throws IOException {
        boolean success = false;

        ftp.enterLocalPassiveMode();

        ftp.setFileType(2);
        ftp.setBufferSize(1048576);

        String remoteFileName = remoteAdr;
        if (remoteAdr.contains("/")) {
            remoteFileName = remoteAdr.substring(remoteAdr.lastIndexOf("/") + 1);
            String directory = remoteAdr.substring(0, remoteAdr.lastIndexOf("/") + 1);
            if ((!directory.equalsIgnoreCase("/")) && (!ftp.changeWorkingDirectory(directory))) {
                int start = 0;
                int end = 0;
                if (directory.startsWith("/"))
                    start = 1;
                else {
                    start = 0;
                }
                end = directory.indexOf("/", start);
                do {
                    String subDirectory = remoteAdr.substring(start, end);
                    if (!ftp.changeWorkingDirectory(subDirectory)) {
                        if (ftp.makeDirectory(subDirectory)) {
                            ftp.changeWorkingDirectory(subDirectory);
                        } else {
                            log.info("创建目录失败");
                            return false;
                        }
                    }
                    start = end + 1;
                    end = directory.indexOf("/", start);
                }
                while (end > start);
            }

        }

        try {
            success = ftp.storeFile(new String(remoteFileName.getBytes("UTF-8"), "iso-8859-1"), input);
        } catch (IOException e) {
            log.info("文件上传失败:" + e.getMessage());
        } finally {
            for (int i = 0; i < remoteAdr.indexOf("/"); i++) {
                ftp.changeWorkingDirectory("../");
            }
            if (input != null) input.close();
            log.info("文件[:" + localAdr + (success ? "]上传成功!" : "上传失败!"));
        }
        return success;
    }

    public boolean deleteDirectory(String remoteAdr) {
        boolean success = false;
        try {
            String remoteAdr_ = new String(remoteAdr.getBytes("UTF-8"),
                    "ISO-8859-1");

            ftp.changeWorkingDirectory(remoteAdr);
            FTPFile[] fs = ftp.listFiles();
            if (fs.length > 0)
                success = ftp.removeDirectory(remoteAdr_);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            disconnect();
        }
        return success;
    }

    public boolean deleteFile(String remoteAdr) {
        boolean success = false;
        try {
            String remoteAdr_ = new String(remoteAdr.getBytes("UTF-8"), "ISO-8859-1");

            FTPFile[] fs = ftp.listFiles(remoteAdr);
            if (fs.length > 0) {
                success = ftp.deleteFile(remoteAdr_);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            disconnect();
        }
        return success;
    }

    public boolean downFile(String remoteremoteAdr, String localAdr, OutputStream os) {
        boolean success = false;
        try {
            ftp.changeWorkingDirectory(remoteremoteAdr);

            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ftpFile : fs) {
                if (ftpFile.getName().equals(localAdr)) {
                    ftp.retrieveFile(new String(ftpFile.getName().getBytes("UTF-8"), "ISO-8859-1"), os);
                }
            }
            success = true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return success;
    }

    public boolean isDirExist(String localAdr, FTPFile[] fs) {
        for (FTPFile ftpFile : fs) {
            if (ftpFile.getName().equals(localAdr)) {
                return true;
            }
        }
        return false;
    }

    public String rename(String localAdr, FTPFile[] fs) {
        int n = 0;

        StringBuffer localAdr_ = new StringBuffer("");
        localAdr_ = localAdr_.append(localAdr);

        while (isDirExist(localAdr_.toString(), fs)) {
            n++;
            String a = "[" + n + "]";

            int b = localAdr_.lastIndexOf(".");

            int c = localAdr_.lastIndexOf("[");
            if (c < 0) {
                c = b;
            }

            StringBuffer name = new StringBuffer(localAdr_.substring(0, c));

            StringBuffer suffix = new StringBuffer(localAdr_.substring(b + 1));
            localAdr_ = name.append(a).append(".").append(suffix);
        }
        return localAdr_.toString();
    }

    public static void main(String[] args) {
    }
}