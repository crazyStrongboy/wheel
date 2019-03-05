package github.com.mars_jun.utils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

public class RandomNumUtils {
    protected ByteArrayInputStream image;
    protected String str;
    protected Random random = new Random();
    protected boolean rotate_able;
    protected static String base_code_en = "ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";

    protected static String base_code_cn = "请问企鹅欠款金额后请客户亲客气饿了企鹅去了企鹅额进去了去就去蓝可儿乐趣就乔尔卢卡起了就";
    public static final int EN_CODE = 0;
    public static final int CN_CODE = 1;
    public static final int UNION_CODE = 3;
    protected static String base_code;
    protected int code_length;
    private static RandomNumUtils randomNumUtil;

    public static RandomNumUtils instance() {
        return instance(4);
    }

    public static RandomNumUtils instance(int length) {
        return instance(0, length);
    }

    public static RandomNumUtils instance(int base_code_key, int length) {
        if (randomNumUtil == null) {
            randomNumUtil = new RandomNumUtils();
        }
        if (base_code == null) {
            base_code = randomNumUtil.getBaseCode(base_code_key);
        }
        randomNumUtil.code_length = (length == 0 ? 4 : length);
        randomNumUtil.init();
        return randomNumUtil;
    }

    public ByteArrayInputStream getImage() {
        return this.image;
    }

    public String getString() {
        return this.str;
    }

    protected void init() {
        int width = 16 * this.code_length + 6;
        int height = 20;
        BufferedImage image = new BufferedImage(width, height, 1);

        Graphics2D g = image.createGraphics();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);

        g.setFont(getFont());

        g.setColor(getRandColor(160, 200));

        for (int i = 0; i < 155; i++) {
            int x = this.random.nextInt(width);
            int y = this.random.nextInt(height);
            int xl = this.random.nextInt(12);
            int yl = this.random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuffer randSB = new StringBuffer();
        for (int i = 0; i < this.code_length; i++) {
            int start = this.random.nextInt(base_code.length());
            String rand = base_code.substring(start, start + 1);

            randSB.append(rand);

            g.setColor(new Color(20 + this.random.nextInt(110), 20 + this.random.nextInt(110), 20 + this.random.nextInt(110)));

            g.drawString(rand, 16 * i + 3, 16);
        }

        this.str = randSB.toString();

        g.dispose();

        ByteArrayInputStream input = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
            ImageIO.write(image, "JPEG", imageOut);
            imageOut.close();
            input = new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            System.out.println("验证码图片产生出现错误：" + e.toString());
        }

        this.image = input;
    }

    protected String getBaseCode(int key) {
        switch (key) {
            case 0:
                return base_code_en;
            case 1:
                return base_code_cn;
            case 3:
                return base_code_en + base_code_cn;
            case 2:
        }
        return base_code_en;
    }

    protected Color getRandColor(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + this.random.nextInt(bc - fc);
        int g = fc + this.random.nextInt(bc - fc);
        int b = fc + this.random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    protected Font getFont() {
        return new Font(null, 0, 18);
    }

    public static void setBaseCode(String baseCode) {
        base_code = baseCode;
    }
}
