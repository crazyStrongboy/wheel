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

    protected static String base_code_cn = "的一了是我不在人们有来他这上着个地到大里说就去子得也和那要下看天时过出小么起你都把好还多没为又可家学只以主会样年想生同老中十从自面前头道它后然走很像见两用她国动进成回什边作对开而己些现山民候经发工向事命给长水几义三声于高手知理眼志点心战二问但身方实吃做叫当住听革打呢真全才四已所敌之最光产情路分总条白话东席次亲如被花口放儿常气五第使写军吧文运再果怎定许快明行因别飞外树物活部门无往船望新带队先力完却站代员机更九您每风级跟笑啊孩万少直意夜比阶连车重便斗马哪化太指变社似士者干石满日决百原拿群究各六本思解立河村八难早论吗根共让相研今其书坐接应关信觉步反处记将千找争领或师结块跑谁草越字加脚紧爱等习阵怕月青半火法题建赶位唱海七女任件感准张团屋离色脸片科倒睛利世刚且由送切星导晚表够整认响雪流未场该并底深刻平伟忙提确近亮轻讲农古黑告界拉名呀土清阳照办史改历转画造嘴此治北必服雨穿内识验传业菜爬睡兴形量咱观苦体众通冲合破友度术饭公旁房极南枪读沙岁线野坚空收算至政城劳落钱特围弟胜教热展包歌类渐强数乡呼性音答哥际旧神座章帮啦受系令跳非何牛取入岸敢掉忽种装顶急林停息句区衣般报叶压慢叔背细";
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
