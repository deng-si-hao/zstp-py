package com.cavin.culture.util;

import com.cavin.culture.model.PythonModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PythonUtil {

    public static String noParam(PythonModel pythonModel) throws IOException{
        String[] strArray = new String[3];
        strArray[0]="python";
        strArray[1]=pythonModel.getPath();
        strArray[2]=pythonModel.getMethod();
        return getInfo(strArray);
    }
    public static String oneParam(PythonModel pythonModel) throws IOException{
        String[] strArray= new String[4];
        strArray[0]="python";
        strArray[1]=pythonModel.getPath();
        strArray[2]=pythonModel.getMethod();
        strArray[3]=pythonModel.getParam1();
        return getInfo(strArray);
    }
    public static String twoParam(PythonModel pythonModel) throws IOException {
        String[] strArray= new String[5];
        strArray[0]="python";
        strArray[1]=pythonModel.getPath();
        strArray[2]=pythonModel.getMethod();
        strArray[3]=pythonModel.getParam1();
        strArray[4]=pythonModel.getParam2();
        return getInfo(strArray);
    }

    public static String getInfo(String[] strArray) throws IOException{
          // --cons
            // 创建库
            //String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--cons"};
            // 获取全部实体标签
            //String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getlabel"};
            // 有问题
            //获取标签下所有实体
//            String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getentitybylabel","e3"};
            //获取一度关系
            //String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getkgR1","飞行器材料损伤传感信号的特征分析和损伤模式识别"};
            // 获取最短路径
//            String[] args1 = new String[] { "python",pythonModel.getPath(),pythonModel.getMethod(),pythonModel.getParam1(),pythonModel.getParam2()};
            // 获取全图
            //String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getalldata"};
            //查询子图
            //String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--searchsubkg", "飞行器材料损伤传感信号的特征分析和损伤模式识别和三维热传导的关系是什么"};
            //Process proc = Runtime.getRuntime().exec("python3 /Users/gunanxi/Downloads/md/project/2020-03_304/tornado_kg/kg_304/kg/neo4j2json.py");

            Process p;
            p = Runtime.getRuntime().exec(strArray);
            //取得命令结果的输出流
            InputStream fis=p.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            String result="";
            //直到读完为止
            /*while((line=br.readLine())!=null)
            {
                result+=line;
            }*/
        //用于接收全部数据
        ArrayList<String> str = new ArrayList<String>();
        //用于去掉第一行无用数
        ArrayList<String> strBuffer = new ArrayList<String>();
        while((line=br.readLine())!=null)
        {
            str.add(line);
        }
        for(int i=1;i<str.size();i++){
            result += str.get(i);
        }
            return result;
    }
}
