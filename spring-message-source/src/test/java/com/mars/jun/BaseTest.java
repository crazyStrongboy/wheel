package com.mars.jun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

/**
 * @author hejun
 * @date 2019/04/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class BaseTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    public void testEnMessageSource() {
        String app = messageSource.getMessage("0000", null, Locale.ENGLISH);
        System.err.println("0000--english: "+app);
    }

    @Test
    public void testBaseMessageSource() {
        String app = messageSource.getMessage("0001", null, Locale.ENGLISH);
        System.err.println("0001--english: "+app);
    }

    @Test
    public void testZhMessageSource() {
        String app = messageSource.getMessage("0000", null, Locale.CHINESE);
        System.err.println("0000--chinese: "+app);
    }

    @Test
    public void testJpMessageSource() {
        String app = messageSource.getMessage("0000", null, Locale.JAPAN);
        System.err.println("0000--jpan: "+app);
    }


    @Test
    public void testNoExsitMessageSource() {
        String app = messageSource.getMessage("0003", null, Locale.CHINESE);
        System.err.println("0003--chinese: "+app);
    }
}
