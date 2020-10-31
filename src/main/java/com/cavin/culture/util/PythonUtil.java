package com.cavin.culture.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PythonUtil {

    public static String noParam(String path, String method) throws IOException {
        String[] strArray = new String[3];
        strArray[0] = "python";
        strArray[1] = path;
        strArray[2] = method;
        return getInfo(strArray);
    }

    public static String oneParam(String path, String method, String param) throws IOException {
        String[] strArray = new String[4];
        strArray[0] = "python";
        strArray[1] = path;
        strArray[2] = method;
        strArray[3] = param;
        return getInfo(strArray);
    }

    public static String twoParam(String path, String method, String param1, String param2) throws IOException {
        String[] strArray = new String[5];
        strArray[0] = "python";
        strArray[1] = path;
        strArray[2] = method;
        strArray[3] = param1;
        strArray[4] = param2;
        return getInfo(strArray);
    }

    public static String getInfo(String[] strArray) throws IOException {
        Long startTime = System.currentTimeMillis();
        Process p;
        p = Runtime.getRuntime().exec(strArray);

        //取得命令结果的输出流
        InputStream fis = p.getInputStream();
        //用一个读输出流类去读
        InputStreamReader isr = new InputStreamReader(fis);
        //用缓冲器读行
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        String result = "";
        //用于接收全部数据
        List<String> str = new ArrayList<String>();
        //用于去掉第一行无用数
//        ArrayList<String> strBuffer = new ArrayList<String>();
//        byte[] rss=new byte[1024];

        while ((line = br.readLine()) != null) {
            str.add(line);
        }
        fis.close();
        isr.close();
        br.close();
        for (int i = 1; i < str.size(); i++) {
            result += str.get(i);
        }
        Long endTime = System.currentTimeMillis();
        // 计算并打印耗时
        Long tempTime = (endTime - startTime);
        System.out.println("消耗时间：>>>>>>>>>>>>>>" +
                (((tempTime / 86400000) > 0) ? ((tempTime / 86400000) + "d") : "") +
                ((((tempTime / 86400000) > 0) || ((tempTime % 86400000 / 3600000) > 0)) ? ((tempTime % 86400000 / 3600000) + "h") : ("")) +
                ((((tempTime / 3600000) > 0) || ((tempTime % 3600000 / 60000) > 0)) ? ((tempTime % 3600000 / 60000) + "m") : ("")) +
                ((((tempTime / 60000) > 0) || ((tempTime % 60000 / 1000) > 0)) ? ((tempTime % 60000 / 1000) + "s") : ("")) +
                ((tempTime % 1000) + "ms"));
        return result;
    }

}
