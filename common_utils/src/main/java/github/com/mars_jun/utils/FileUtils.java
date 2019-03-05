package github.com.mars_jun.utils;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    private static final File POOL_FILE = getUniqueFile(FileUtils.class,
            ".deletefiles");
    private static ArrayList<File> deleteFilesPool;

    static {
        try {
            initPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initPool() {
        if ((POOL_FILE.exists()) && (POOL_FILE.canRead())) {
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(POOL_FILE));
                deleteFilesPool = (ArrayList) in.readObject();
            } catch (Exception e) {
                deleteFilesPool = new ArrayList();

                if (in == null) return;
                try {
                    in.close();
                } catch (Exception localException1) {
                }
            } finally {
                if (in != null)
                    try {
                        in.close();
                    } catch (Exception localException2) {
                    }
            }
            try {
                in.close();
            } catch (Exception localException3) {
            }
        } else {
            deleteFilesPool = new ArrayList();
        }
    }

    public static String getShortFileName(String fileName) {
        String shortFileName = "";
        int pos = fileName.lastIndexOf('\\');
        if (pos == -1) {
            pos = fileName.lastIndexOf('/');
        }
        if (pos > -1)
            shortFileName = fileName.substring(pos + 1);
        else {
            shortFileName = fileName;
        }
        return shortFileName;
    }

    public static String getShortFileNameWithoutExt(String fileName) {
        String shortFileName = getShortFileName(fileName);
        shortFileName = getFileNameWithoutExt(shortFileName);
        return shortFileName;
    }

    public static String read(String fileName)
            throws Exception {
        return read(new File(fileName));
    }

    public static String read(File file)
            throws Exception {
        String fileContent = "";
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            fileContent = read(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return fileContent;
    }

    public static String read(InputStream is)
            throws Exception {
        byte[] result = readBytes(is);
        return new String(result);
    }

    public static byte[] readBytes(String fileName)
            throws Exception {
        return readBytes(new FileInputStream(fileName));
    }

    public static byte[] readBytes(File file)
            throws Exception {
        return readBytes(new FileInputStream(file));
    }

    public static byte[] readBytes(InputStream is)
            throws Exception {
        if ((is == null) || (is.available() < 1)) {
            return new byte[0];
        }
        byte[] buff = new byte[8192];
        byte[] result = new byte[is.available()];

        BufferedInputStream in = null;
        int nch;
        try {
            in = new BufferedInputStream(is);
            int pos = 0;
            while ((nch = in.read(buff, 0, buff.length)) != -1) {
                System.arraycopy(buff, 0, result, pos, nch);
                pos += nch;
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return result;
    }

    public static void write(String content, File file)
            throws IOException {
        write(content.getBytes(), file);
    }

    public static void write(String content, String file)
            throws IOException {
        write(content, new File(file));
    }

    public static void write(byte[] bytes, String file)
            throws IOException {
        write(bytes, new File(file));
    }

    public static void write(byte[] bytes, File file)
            throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
        } finally {
            if (out != null)
                out.close();
        }
    }

    public static String getFileNameWithoutExt(String fileName) {
        String shortFileName = fileName;
        if (fileName.indexOf('.') > -1) {
            shortFileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return shortFileName;
    }

    public static String getFileNameExt(String fileName) {
        String fileExt = "";
        if (fileName.indexOf('.') > -1) {
            fileExt = fileName.substring(fileName.lastIndexOf('.'));
        }
        return fileExt;
    }

    public static synchronized File getUniqueFile(File repository, String fileName) {
        String shortFileName = getShortFileName(fileName);
        String tempFileName = getFileNameWithoutExt(shortFileName);
        File file = new File(repository, shortFileName);
        String fileExt = getFileNameExt(shortFileName);
        while (file.exists()) {
            file = new File(repository, tempFileName + "-" +
                    Math.abs(Math.random() * 1000000.0D) + fileExt);
        }
        return file;
    }

    public static void deleteFile(File file) {
        file.delete();
        if (file.exists()) {
            deleteFilesPool.add(file);
        }
        checkDeletePool();
    }

    private static void checkDeletePool() {
        for (int i = deleteFilesPool.size() - 1; i >= 0; i--) {
            File file = (File) deleteFilesPool.get(i);
            file.delete();
            if (!file.exists()) {
                deleteFilesPool.remove(i);
            }
        }
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(POOL_FILE));
            out.writeObject(deleteFilesPool);
        } catch (Exception e) {
            e.printStackTrace();

            if (out != null)
                try {
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public static File getUniqueFile(Class cl, String extension) {
        int key = 0;
        URL url = cl.getResource(getClassNameWithoutPackage(cl) + ".class");
        if (url != null) {
            key = url.getPath().hashCode();
        }
        File propFile = new File(System.getProperty("java.io.tmpdir"),
                getClassNameWithoutPackage(cl) + key + extension);
        return propFile;
    }

    private static String getClassNameWithoutPackage(Class cl) {
        String className = cl.getName();
        int pos = className.lastIndexOf('.') + 1;
        if (pos == -1)
            pos = 0;
        String name = className.substring(pos);
        return name;
    }

    public static boolean deleteFolder(File delFolder) {
        boolean hasDeleted = true;

        File[] allFiles = delFolder.listFiles();

        for (int i = 0; i < allFiles.length; i++) {
            if (!hasDeleted) break;
            if (allFiles[i].isDirectory()) {
                hasDeleted = deleteFolder(allFiles[i]);
            } else {
                if (!allFiles[i].isFile()) continue;
                try {
                    if (allFiles[i].delete())
                        continue;
                    hasDeleted = false;
                } catch (Exception e) {
                    hasDeleted = false;
                }

            }

        }

        if (hasDeleted) {
            delFolder.delete();
        }
        return hasDeleted;
    }

    public static String getRealPathName(Class cl) {
        URL url = cl.getResource(getClassNameWithoutPackage(cl) + ".class");
        if (url != null) {
            return url.getPath();
        }
        return null;
    }

    public static List<String> getDiskPath() {
        List disks = new ArrayList();
        File[] fs = File.listRoots();
        for (int i = 0; i < fs.length; i++) {
            disks.add(fs[i].getPath());
        }
        return disks;
    }

    public static List<File> listFiles(String filePath) {
        return listFiles(filePath, null);
    }

    public static List<File> listFiles(String filePath, FileFilter filter) {
        List fileList = new ArrayList();
        try {
            File file = new File(filePath);
            File[] files = null;
            if (filter != null)
                files = file.listFiles(filter);
            else {
                files = file.listFiles();
            }
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];
                if (tempFile.isDirectory()) {
                    String subDirName = tempFile.getPath();
                    List list = null;
                    list = listFiles(subDirName, filter);
                    for (int j = 0; j < list.size(); j++)
                        fileList.add((File) list.get(j));
                } else {
                    if (!tempFile.isFile()) {
                        continue;
                    }
                    fileList.add(tempFile);
                }
                if (i == files.length - 1)
                    return fileList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileList;
    }

    public static boolean zip(String sourceFilePath, String targetFileName) {
        boolean flag = false;
        try {
            File f = new File(sourceFilePath);
            ZipOutputStream out = new ZipOutputStream(
                    new FileOutputStream(targetFileName));

            flag = zip(out, f, "");
            out.close();
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean zip(ZipOutputStream out, File f, String base) {
        try {
            if (f.isDirectory()) {
                File[] fl = f.listFiles();
                out.putNextEntry(new ZipEntry(base + "/"));
                base = base + "/";
                for (int i = 0; i < fl.length; i++)
                    zip(out, fl[i], base + fl[i].getName());
            } else {
                out.putNextEntry(new ZipEntry(base));
                FileInputStream in = new FileInputStream(f);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf, 0, 1024)) != -1) {
                    out.write(buf, 0, len);
                }
                in.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean unZip(String zipFilePath, String outputFileName) {
        return unZip(zipFilePath, outputFileName, false);
    }

    public static boolean unZip(String zipFilePath, String outputFileName, boolean deleteFile) {
        try {
            File file = new File(zipFilePath);
            if (!file.exists()) {
                return false;
            }
            ZipFile zipFile = new ZipFile(file);
            Enumeration e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) e.nextElement();
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputFileName + name);
                    f.mkdirs();
                } else {
                    File f = new File(outputFileName + zipEntry.getName());
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                    InputStream is = zipFile.getInputStream(zipEntry);
                    FileOutputStream fos = new FileOutputStream(f);
                    int length = 0;
                    byte[] b = new byte[1024];
                    while ((length = is.read(b, 0, 1024)) != -1) {
                        fos.write(b, 0, length);
                    }
                    is.close();
                    fos.close();
                }
            }

            if (zipFile != null) {
                zipFile.close();
            }

            if (deleteFile) {
                file.deleteOnExit();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean copyDirectiory(String sourceDirPath, String targetDirPath, String sourceDestCoding, String targetDestCoding) {
        boolean flag = false;
        try {
            new File(targetDirPath).mkdirs();

            File[] file = new File(sourceDirPath).listFiles();
            for (int i = 0; i < file.length; i++) {
                if (file[i].isFile()) {
                    String type = file[i].getName().substring(
                            file[i].getName().lastIndexOf(".") + 1);

                    if (type.equalsIgnoreCase("txt"))
                        copyFile(file[i].getAbsolutePath(), targetDirPath +
                                        file[i].getName(), sourceDestCoding,
                                targetDestCoding);
                    else
                        copyFile(file[i].getAbsolutePath(), targetDirPath +
                                file[i].getName());
                }
                if (!file[i].isDirectory())
                    continue;
                String sourceDir = sourceDirPath + File.separator +
                        file[i].getName();
                String targetDir = targetDirPath + File.separator +
                        file[i].getName();
                copyDirectiory(sourceDir, targetDir);
            }

            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean cutDirectiory(String sourceFilePath, String targetFilePath) {
        boolean flag = false;
        if (copyDirectiory(sourceFilePath, targetFilePath)) {
            flag = deleteDirectory(sourceFilePath);
        }
        return flag;
    }

    public static boolean cutFile(String sourceFilePath, String targetFilePath) {
        boolean flag = false;
        if (copyFile(sourceFilePath, targetFilePath)) {
            flag = deleteFile(sourceFilePath);
        }
        return flag;
    }

    public static boolean copyFile(String sourceFilePath, String targetFilePath) {
        boolean flag = false;
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(
                    new FileInputStream(sourceFilePath));

            outBuff = new BufferedOutputStream(
                    new FileOutputStream(targetFilePath));

            byte[] b = new byte[5120];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }

            outBuff.flush();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static boolean copyDirectiory(String sourceFilePath, String targetFilePath) {
        boolean flag = false;
        try {
            new File(sourceFilePath).mkdirs();

            File[] file = new File(sourceFilePath).listFiles();
            for (int i = 0; i < file.length; i++) {
                if (file[i].isFile()) {
                    File sourceFile = file[i];

                    File targetFile = new File(new File(targetFilePath)
                            .getAbsolutePath() +
                            File.separator + file[i].getName());
                    copyFile(sourceFile.getAbsolutePath(), targetFile
                            .getAbsolutePath());
                }
                if (!file[i].isDirectory())
                    continue;
                String dir1 = sourceFilePath + "/" + file[i].getName();

                String dir2 = targetFilePath + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean copyFile(String srcFilePath, String destFilePath, String srcCoding, String destCoding) {
        boolean flag = false;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(srcFilePath), srcCoding));
            bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(destFilePath), destCoding));
            char[] cbuf = new char[5120];
            int len = cbuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = br.read(cbuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            bw.write(cbuf, 0, off);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (br != null)
                    br.close();
                if (bw != null)
                    bw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (br != null)
                    br.close();
                if (bw != null)
                    bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);

        if (!file.exists()) {
            return flag;
        }

        if (file.isFile()) {
            return deleteFile(sPath);
        }
        return deleteDirectory(sPath);
    }

    public static boolean deleteFile(String sourceFilePath) {
        boolean flag = false;
        File file = new File(sourceFilePath);

        if ((file.isFile()) && (file.exists())) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    public static boolean deleteDirectory(String sourceFilePath) {
        if (!sourceFilePath.endsWith(File.separator)) {
            sourceFilePath = sourceFilePath + File.separator;
        }
        File dirFile = new File(sourceFilePath);

        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;

        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }

        return dirFile.delete();
    }

    public static String readFileToString(String path, String destCoding)
            throws IOException {
        String resultStr = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte[] inBuf = new byte[2000];
            int len = inBuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = fis.read(inBuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            resultStr = new String(new String(inBuf, 0, off, destCoding)
                    .getBytes());
        } finally {
            if (fis != null)
                fis.close();
        }
        return resultStr;
    }

    public static byte[] readFileToBytes(String path)
            throws IOException {
        byte[] b = null;
        InputStream is = null;
        File f = new File(path);
        try {
            is = new FileInputStream(f);
            b = new byte[(int) f.length()];
            is.read(b);
        } finally {
            if (is != null)
                is.close();
        }
        return b;
    }

    public static boolean saveByteToFile(byte[] fileByte, String filePath) {
        boolean flag = false;
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(filePath));
            os.write(fileByte);
            os.flush();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();

            if (os != null)
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return flag;
    }

    public static boolean saveStringToFile(String fileString, String filePath) {
        return saveStringToFile(fileString, filePath, "GBK");
    }

    public static boolean saveStringToFile(String fileString, String filePath, String encoding) {
        boolean flag = false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filePath), encoding));
            bw.append(fileString);
            bw.flush();
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();

            if (bw != null)
                try {
                    bw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public static boolean runUnFinished(String srcFilePath, String destFilePath) {
        boolean flag = false;
        File destFile = new File(destFilePath);
        if (destFile.exists()) {
            FileChannel fin = null;
            FileChannel fout = null;
            try {
                RandomAccessFile randomSrcFile = new RandomAccessFile(srcFilePath, "rw");
                long destFileSize = destFile.length();

                RandomAccessFile randomDestFile = new RandomAccessFile(destFile, "rw");
                randomSrcFile.seek(destFileSize - 2048L);
                randomDestFile.seek(destFileSize - 2048L);
                fin = randomSrcFile.getChannel();
                fout = randomDestFile.getChannel();
                while (true) {
                    int length = 2097152;

                    if (fin.position() == fin.size()) {
                        fin.close();
                        fout.close();
                        break;
                    }

                    if (fin.size() - fin.position() < 20971520L)
                        length = (int) (fin.size() - fin.position());
                    else {
                        length = 20971520;
                    }
                    fin.transferTo(fin.position(), length, fout);
                    fin.position(fin.position() + length);
                }
                flag = true;
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
                try {
                    if (fin != null) fin.close();
                    if (fout != null) fout.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    if (fin != null) fin.close();
                    if (fout != null) fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public static String getFileExtName(File file) {
        String fileName = file.getName();
        String extName = fileName.substring(fileName.lastIndexOf('.') + 1);
        return extName;
    }
}