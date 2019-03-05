package github.com.mars_jun.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ImageUtils {
    public static final String GIF = "gif";
    public static final String PNG = "png";
    public static final String JPG = "jpg";
    public static final String BMP = "bmp";

    private static String getImageType(String filePath)
            throws IOException {
        File f = new File(filePath);
        FileInputStream in = null;
        String type = null;
        byte[] bytes = new byte[2];

        in = new FileInputStream(f);

        in.read(bytes, 0, 2);

        if (((bytes[0] & 0xFF) == 71) && ((bytes[1] & 0xFF) == 73))
            type = "gif";
        else if (((bytes[0] & 0xFF) == 137) && ((bytes[1] & 0xFF) == 80))
            type = "png";
        else if (((bytes[0] & 0xFF) == 255) && ((bytes[1] & 0xFF) == 216))
            type = "jpg";
        else if (((bytes[0] & 0xFF) == 66) && ((bytes[1] & 0xFF) == 77))
            type = "bmp";
        else {
            System.out.println("无法识别!");
        }

        in.close();

        return type;
    }

    private static int convertToBlackWhite(int pixel) {
        int result = 0;

        int red = pixel >> 16 & 0xFF;
        int green = pixel >> 8 & 0xFF;
        int blue = pixel & 0xFF;

        result = -16777216;

        int tmp = red * red + green * green + blue * blue;
        if (tmp > 49152) {
            result += 16777215;
        }

        return result;
    }

    public static void toMonochromeImg(String scrFilePath, String targetFilePath) {
        BufferedImage bi = readImageFromFile(scrFilePath);

        int width = bi.getWidth(null);
        int height = bi.getHeight(null);

        int[] pixels = new int[width * height];
        bi.getRGB(0, 0, width, height, pixels, 0, width);

        int[] newPixels = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            newPixels[i] = convertToBlackWhite(pixels[i]);
        }

        bi = new BufferedImage(width, height, 1);
        bi.setRGB(0, 0, width, height, newPixels, 0, width);
        newPixels = null;

        writeImageToFile(targetFilePath, bi);
    }

    public static void toGrayImg(String srcFilePath, String targetFilePath) {
        File srcFile = new File(srcFilePath);
        File targetFile = new File(targetFilePath);
        toGrayImg(srcFile, targetFile);
    }

    public static void toGrayImg(File srcFile, File targetFile) {
        try {
            String fileName = targetFile.getName();
            String formatName = fileName.substring(targetFile.getName().lastIndexOf('.') + 1);
            BufferedImage src = ImageIO.read(srcFile);
            ImageIO.write(toGrayImg(src), formatName, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMonochromeImg(String imgFile) {
        BufferedImage bi = null;
        boolean result = false;
        int w = 0;
        int h = 0;
        int i = 0;
        int j = 0;

        bi = readImageFromFile(imgFile);

        w = bi.getWidth();
        h = bi.getHeight();
        int count = 0;
        int n = 0;
        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {
                int rgb = bi.getRGB(i, j);
                rgb &= 16777215;
                if ((rgb != 16777215) && (rgb != 0)) {
                    n++;
                    break;
                }

                count++;
            }
        }
        if ((i == w) && (j == h))
            result = true;
        else {
            result = false;
        }

        return result;
    }

    public static BufferedImage readImageFromFile(String imageFile) {
        BufferedImage bi = null;
        ImageInputStream iis = null;
        try {
            Iterator readers = ImageIO.getImageReadersByFormatName(getImageType(imageFile));
            ImageReader reader = (ImageReader) readers.next();

            iis = ImageIO.createImageInputStream(
                    new File(imageFile));
            reader.setInput(iis);

            bi = reader.read(0);
            readers = null;
            reader = null;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (iis != null) {
                    iis.close();
                    iis = null;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (iis != null) {
                    iis.close();
                    iis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bi;
    }

    public static void writeImageToFile(String imgFile, BufferedImage bi) {
        ImageOutputStream ios = null;
        try {
            Iterator writers = ImageIO.getImageWritersByFormatName(
                    imgFile.substring(imgFile.lastIndexOf('.') + 1));
            ImageWriter writer = (ImageWriter) writers.next();

            File f = new File(imgFile);

            ios = ImageIO.createImageOutputStream(f);
            writer.setOutput(ios);

            writer.write(bi);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (ios != null) {
                    ios.close();
                    ios = null;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                    ios = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void zoomImg(String srcFilePath, String targetFilePath, int targetWidth, int targetHeight) {
        File srcFile = new File(srcFilePath);
        File targetFile = new File(targetFilePath);
        zoomImg(srcFile, targetFile, targetWidth, targetHeight);
    }

    public static void zoomImg(File srcFile, File targetFile, int targetWidth, int targetHeight) {
        try {
            String fileName = targetFile.getName();
            String formatName = fileName.substring(targetFile.getName().lastIndexOf('.') + 1);
            BufferedImage bImage = ImageIO.read(srcFile);
            bImage = zoomImg(bImage, targetWidth, targetHeight);
            ImageIO.write(bImage, formatName, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void rotateImg(String srcFilePath, String targetFilePath, int degree) {
        try {
            Color whiteColor = new Color(255, 255, 255);
            File srcFile = new File(srcFilePath);
            File targetFile = new File(targetFilePath);
            rotateImg(srcFile, targetFile, degree, whiteColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rotateImg(File srcFile, File targetFile, int degree, Color color) {
        try {
            String fileName = targetFile.getName();
            String formatName = fileName.substring(targetFile.getName().lastIndexOf('.') + 1);
            Image image = ImageIO.read(srcFile);
            BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), 1);
            rotateImg(bImage, degree, color);
            ImageIO.write(bImage, formatName, targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cutImg(String srcFilePath, String targetFilePath, int cutX, int cutY, int cutWidth, int cutHeight)
            throws IOException {
        Rectangle rec = new Rectangle(cutX, cutY, cutWidth, cutHeight);
        saveSubImage(new File(srcFilePath), new File(targetFilePath), rec);
    }

    public static void cutImg(File srcFile, File targetFile, int cutX, int cutY, int cutWidth, int cutHeight)
            throws IOException {
        Rectangle rec = new Rectangle(cutX, cutY, cutWidth, cutHeight);
        saveSubImage(srcFile, targetFile, rec);
    }

    public static void cutImg(String srcFilePath, String targetFilePath, Rectangle rect)
            throws IOException {
        cutImg(new File(srcFilePath), new File(targetFilePath), rect);
    }

    public static void cutImg(String srcFilePath, String targetFilePath, int cutX, int cutY, int cutWidth, int cutHeight, int targetWidth, int targetHeight)
            throws IOException {
        Rectangle cutRec = new Rectangle(cutX, cutY, cutWidth, cutHeight);
        cutImg(new File(srcFilePath), new File(targetFilePath), cutRec, targetWidth, targetHeight);
    }

    public static void cutImg(File srcFile, File targetFile, Rectangle rect, int targetWidth, int targetHeight)
            throws IOException {
        Image image = ImageIO.read(srcFile);
        BufferedImage bImage = makeThumbnail(image);
        bImage = getSubImage(bImage, rect);
        bImage = zoomImg(bImage, targetWidth, targetHeight);
        ImageIO.write(bImage, FileUtils.getFileExtName(targetFile), targetFile);
    }

    public static void cutImg(File srcFile, File targetFile, Rectangle rect)
            throws IOException {
        Image image = ImageIO.read(srcFile);
        BufferedImage bImage = makeThumbnail(image);
        bImage = getSubImage(bImage, rect);
        ImageIO.write(bImage, FileUtils.getFileExtName(srcFile), targetFile);
    }

    public static void createMark(String filePath, String watermark, String words, float alpha, String savePath)
            throws Exception {
        int wid = 0;
        int het = 0;
        ImageIcon imgIcon = new ImageIcon(filePath);
        Image theImg = imgIcon.getImage();
        ImageIcon waterIcon = new ImageIcon(watermark);
        Image waterImg = waterIcon.getImage();
        File f = new File(filePath);
        String picname = f.getName();
        if ((watermark != null) && (!watermark.equals(""))) {
            ImageIcon markIcon = new ImageIcon(watermark);
            Image markImg = markIcon.getImage();
            wid = markImg.getWidth(null);
            het = markImg.getHeight(null);
        }
        int width = theImg.getWidth(null);
        int height = theImg.getHeight(null);
        if ((width < 200) || (height < 200)) {
            return;
        }
        if (savePath.equals(""))
            savePath = filePath;
        else
            savePath = savePath + "/" + picname;
        try {
            BufferedImage bimage = new BufferedImage(width, height,
                    1);
            Graphics2D g = bimage.createGraphics();
            Font font = new Font("黑体", 0, 35);
            g.setColor(Color.white);
            g.setFont(font);
            g.setBackground(Color.white);
            g.drawImage(theImg, 0, 0, null);
            g.drawImage(waterImg, width - wid - 90, height - het - 70, null);

            g.drawString(words, width - 480, height - 40);
            FileOutputStream out = new FileOutputStream(savePath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
            param.setQuality(100.0F, true);
            encoder.encode(bimage, param);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.gc();
        }
    }

    private static BufferedImage toGrayImg(BufferedImage bi) {
        RescaleOp op = new RescaleOp(1.2F, 0.0F, null);
        bi = op.filter(bi, null);

        ColorSpace cs = ColorSpace.getInstance(1003);
        ColorConvertOp cop = new ColorConvertOp(cs, null);
        return cop.filter(bi, null);
    }

    private static BufferedImage zoomImg(BufferedImage bImage, int targetWidth, int targetHeight) {
        BufferedImage target = null;

        double sx = targetWidth / bImage.getWidth();
        double sy = targetHeight / bImage.getHeight();

        int type = bImage.getType();
        if (type == 0) {
            ColorModel cm = bImage.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetWidth, targetHeight);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            target = new BufferedImage(targetWidth, targetHeight, type);
        }
        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(bImage, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    private static void rotateImg(BufferedImage image, int degree, Color bgcolor)
            throws IOException {
        int iw = image.getWidth();
        int ih = image.getHeight();
        int w = 0;
        int h = 0;
        int x = 0;
        int y = 0;
        degree %= 360;
        if (degree < 0)
            degree += 360;
        double ang = Math.toRadians(degree);

        if ((degree == 180) || (degree == 0) || (degree == 360)) {
            w = iw;
            h = ih;
        } else if ((degree == 90) || (degree == 270)) {
            w = ih;
            h = iw;
        } else {
            int d = iw + ih;
            w = (int) (d * Math.abs(Math.cos(ang)));
            h = (int) (d * Math.abs(Math.sin(ang)));
        }

        x = w / 2 - iw / 2;
        y = h / 2 - ih / 2;
        BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        Graphics2D gs = (Graphics2D) rotatedImage.getGraphics();
        if (bgcolor == null) {
            rotatedImage = gs.getDeviceConfiguration().createCompatibleImage(w, h, 3);
        } else {
            gs.setColor(bgcolor);
            gs.fillRect(0, 0, w, h);
        }

        AffineTransform at = new AffineTransform();
        at.rotate(ang, w / 2, h / 2);
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at, 3);
        op.filter(image, rotatedImage);
        image = rotatedImage;
    }

    private static void saveSubImage(File srcFile, File targetFile, Rectangle rect)
            throws IOException {
        cutImg(srcFile, targetFile, rect);
    }

    private static BufferedImage makeThumbnail(Image img) {
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage tag = new BufferedImage(width, height, 1);
        Graphics g = tag.getGraphics();
        g.drawImage(img.getScaledInstance(width, height, 4), 0, 0, null);
        g.dispose();
        return tag;
    }

    private static BufferedImage getSubImage(BufferedImage image, Rectangle subImageBounds)
            throws IOException {
        BufferedImage subImage = new BufferedImage(subImageBounds.width,
                subImageBounds.height, 1);
        Graphics g = subImage.getGraphics();
        if ((subImageBounds.width > image.getWidth()) ||
                (subImageBounds.height > image.getHeight())) {
            int left = subImageBounds.x;
            int top = subImageBounds.y;
            if (image.getWidth() < subImageBounds.width)
                left = (subImageBounds.width - image.getWidth()) / 2;
            if (image.getHeight() < subImageBounds.height)
                top = (subImageBounds.height - image.getHeight()) / 2;
            g.setColor(Color.white);
            g.fillRect(0, 0, subImageBounds.width, subImageBounds.height);
            g.drawImage(image, left, top, null);
        } else {
            g.drawImage(image.getSubimage(subImageBounds.x, subImageBounds.y,
                    subImageBounds.width, subImageBounds.height), 0, 0, null);
        }
        g.dispose();

        return subImage;
    }

    private static void alpha(BufferedImage image, String outPath) {
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.yellow);
        g2d.fillRect(0, 0, 600, 400);

        AlphaComposite ac = AlphaComposite.getInstance(3,
                0.5F);
        g2d.setComposite(ac);
        g2d.setFont(new Font("隶书", 0, 42));
        g2d.setColor(Color.black);
        g2d.drawString("方正粗宋简体透明度为0.5", 20, 40);

        AlphaComposite ac2 = AlphaComposite.getInstance(
                3, 0.2F);
        g2d.setComposite(ac2);
        g2d.setColor(Color.CYAN);
        g2d.fill3DRect(10, 200, 180, 80, false);

        g2d.dispose();
        try {
            FileOutputStream fs = new FileOutputStream(outPath);
            ImageIO.write(image, "jpg", fs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void pressImage(String pressImg, String targetImg, int x, int y, float alpha) {
        try {
            float Alpha = alpha;
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    1);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);

            File _filebiao = new File(pressImg);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(10,
                    Alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2,
                    (height - height_biao) / 2, wideth_biao, height_biao, null);

            g.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void pressText(String pressText, String targetImg, String fontName, int fontStyle, int color, int fontSize, int x, int y, float alpha) {
        try {
            float Alpha = alpha;
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    1);
            Graphics2D g = image.createGraphics();

            g.drawImage(src, 0, 0, wideth, height, null);
            g.setColor(Color.RED);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(10,
                    Alpha));
            g.drawString(pressText, wideth - fontSize - x, height - fontSize /
                    2 - y);
            g.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
