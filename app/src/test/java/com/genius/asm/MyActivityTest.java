package com.genius.asm;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyActivityTest {

    private static final String BUILD_OUTPUT = "build\\intermediates\\classes\\debug\\com\\genius\\asm\\";

    @Test
    public void test() throws Exception {
        MyActivity myActivity = new MyActivity();
        myActivity.onCreate();
    }

    @Test
    public void testA() throws IOException {
        File file = new File(BUILD_OUTPUT + "MyActivity.class");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = ASMUtils.referHackWhenInit(fileInputStream);
        fileInputStream.close();
        System.out.println(new String(bytes));

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.close();

        MyActivity myActivity = new MyActivity();
        myActivity.onCreate();
    }
}