package com.cavin.culture;

import com.cavin.culture.controller.MapDisplayController;
import com.cavin.culture.model.PythonModel;
import com.cavin.culture.model.User;
import com.cavin.culture.service.UserService;
import com.cavin.culture.util.PythonUtil;
import org.apache.jena.atlas.json.JSON;
import org.apache.jena.atlas.json.JsonArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.python.core.*;
import org.python.util.PythonInterpreter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest
class CultureApplicationTests {
    @Resource
    private UserService userService;
    @Test
    void contextLoads() {

    }

    //测试逻辑删除用户 信息
  /*  @Test
    public void delUser(){
        Long i=1L;
        userService.delUserById(i);
        User user=userService.getUserById(i);
        System.out.println(user);
    }*/
    //测试修改用户信息
  /*  @Test
    public void updateUser(){
        Long id=1L;
        String name="test01";
        String password="123456";
        String level="1";
        String pwd= SHAUtil.getSHA256(password);
        User user=new User();
        user.setId(id);
        user.setUserName(name);
        user.setUserPassword(pwd);
        user.setLevel(level);
        userService.updateUser(user);
        System.out.println(userService.getUserById(id));
    }*/
/*    @Test
    public void testId(){
//        Long id= UniquelUtil.genId();
//        long randomNum = System.currentTimeMillis();
//        String idd=UUID.randomUUID().toString();
//        System.out.println(idd);
        List<User> users= userService.getAll(pageSize);
//        User name=userService.getUserByName("test02");
        System.out.println(users.toString());

    }*/

    @Test
    public void testpython(){
//
        System.out.println("************");
        Properties props = new Properties();
        props.put("python.console.encoding", "UTF-8"); // Used to prevent: console: Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
        props.put("python.security.respectJavaAccessibility", "false"); //don't respect java accessibility, so that we can access protected members on subclasses
        props.put("python.import.site","false");
        Properties preprops = System.getProperties();
        PythonInterpreter.initialize(preprops, props, new String[0]);
        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("import sys");
//        interp.exec("sys.path.append('D:/Program Files (x86)/jython2.7.0/Lib')");//jython自己的
//        interp.exec("sys.path.append('E:\\\\Tupu\\\\NOW\\\\tornado_kg copy\\\\venv')");//jython自己的
        interp.exec("sys.path.append('E:\\\\Tupu\\\\NOW\\\\tornado_kg copy\\\\venv\\\\lib\\\\site-packages')");//我们自己写的
        interp.exec("sys.path.append('E:\\\\Tupu\\\\NOW\\\\tornado_kg copy\\\\venv\\\\lib\\\\site-packages\\\\py2neo')");
        PythonInterpreter interpreter = new PythonInterpreter();
        //interpreter.execfile("D:\\neo2json.py");
        interpreter.execfile("E:\\testByTupu\\tornado_kg copy\\neo4j2json.py");
        // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
        PyFunction pyFunction = interpreter.get("kg_constructor", PyFunction.class);
        //int a = 5, b = 10;
        //调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
        PyObject pyobj = pyFunction.__call__();//#(new PyInteger(a), new PyInteger(b));
        System.out.println("the anwser is: " + pyobj);
    }
    @Test
    public void main() throws IOException {

        PythonInterpreter interpreter = new PythonInterpreter();
        PySystemState sys = Py.getSystemState();
        System.out.println(sys.path.toString());
        sys.path.add("D:\\jython2.2\\Lib");
        interpreter.exec("import py2neo");
        interpreter.exec("import jieba");

        //InputStream filepy = new FileInputStream("D:\\tjar.py");

        InputStream filepy = new FileInputStream("E:\\testByTupu\\tornado_kg copy\\neo4j2json.py");

        interpreter.execfile(filepy);
        filepy.close();
    }
    @Test
    public  void maine() throws IOException {
        PythonInterpreter interpreter = new PythonInterpreter();
        PySystemState sys = Py.getSystemState();
        sys.path.add("D:\\jython2.7.0\\Lib");
        interpreter.exec("import sys");
        interpreter.exec("");
        interpreter.exec("print sys.path");
        interpreter.exec("path = \"D:\\jython2.7.0\\Lib\"");
        interpreter.exec("sys.path.append(path)");
        interpreter.exec("print sys.path");
//        interpreter.exec("a=3; b=5;");
        InputStream filepy = new FileInputStream("E:\\testByTupu\\tornado_kg copy\\neo4j2json.py");
        interpreter.execfile(filepy);
        filepy.close();
    }
    @Test
    public void maina() throws IOException {
        Properties properties = new Properties();
        properties.put("python.console.encoding","UTF-8");
        System.setProperty("python.home","D:\\jython2.7.0");

        // 1. Python面向函数式编程: 在Java中调用Python函数
        /*String pythonFunc = "E:\\testByTupu\\tornado_kg copy\\neo4j2json.py";

        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("import sys");
        interp.exec("sys.path.append('E:\\Tupu\\NOW\\tornado_kg copy\\venv\\lib\\site-packages')");
        // 加载python程序
        interp.execfile(pythonFunc);
        // 调用Python程序中的函数
        PyFunction pyf = interp.get("main", PyFunction.class);
        PyObject dddRes = pyf.__call__();
        System.out.println(dddRes);
        interp.cleanup();
        interp.close();*/

        // 2. 面向对象式编程: 在Java中调用Python对象实例的方法
        String pythonClass = "E:\\testByTupu\\tornado_kg copy\\neo4j2json.py";
        // python对象名
        String pythonObjName = "cons";
        // python类名
        String pythonClazzName = "kg_constructor";
        PythonInterpreter pi2 = new PythonInterpreter();
        // 加载python程序
        pi2.execfile(pythonClass);
        // 实例化python对象
        pi2.exec(pythonObjName + "=" + pythonClazzName + "()");
        // 获取实例化的python对象
        PyObject pyObj = pi2.get(pythonObjName);
        // 调用python对象方法,传递参数并接收返回值
        PyObject result = pyObj.invoke("constructor", new PyObject[] {Py.newInteger(2), Py.newInteger(3)});
        double power = Py.py2double(result);
        System.out.println(power);
        pi2.cleanup();
        pi2.close();
    }
    @Test
    public void mainc() throws IOException {
        System.setProperty("python.home","D:\\jython2.7.0");
        String python = "E:\\testByTupu\\tornado_kg copy\\neo4j2json.py";
        PythonInterpreter interp = new PythonInterpreter();
        interp.execfile(python);
        interp.cleanup();
        interp.close();
    }
    @Test
    public void mainxx() {

        try
        {   // --cons
            // 创建库
//            String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--cons"};
            // 获取全部实体标签
            String[] args1 = new String[] { "python", "F:\\zhishitupu\\zstp\\src\\main\\resources\\static\\py\\neo4j2json_cons.py","--getlabel"};
            // 有问题
            //获取标签下所有实体
//            String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getentitybylabel","e3"};
            //获取一度关系
       //     String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getkgR1","飞行器材料损伤传感信号的特征分析和损伤模式识别"};
            // 获取最短路径
//            String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getkgShortestPath","加密","三维热传导"};
            // 获取全图
            //String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getalldata"};
            //查询子图
            //String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--searchsubkg", "飞行器材料损伤传感信号的特征分析和损伤模式识别和三维热传导的关系是什么"};
            //Process proc = Runtime.getRuntime().exec("python3 /Users/gunanxi/Downloads/md/project/2020-03_304/tornado_kg/kg_304/kg/neo4j2json.py");
            Process p;
            p = Runtime.getRuntime().exec(args1);
            //取得命令结果的输出流
            InputStream fis=p.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            String result="";
            //直到读完为止
           /* while((line=br.readLine())!=null)
            {
                result+=line;
            }*/

            ArrayList<String> strBuffer = new ArrayList<String>();
            ArrayList<String> str = new ArrayList<String>();
            while((line=br.readLine())!=null)
            {

                str.add(line);
//                result+=line;
            }
            for(int i=1;i<str.size();i++){
/*                strBuffer.add(str.get(i));
                System.out.println(strBuffer);*/
                result += str.get(i);
/*                strBuffer.add(str.get(i));
                System.out.println("***********************");
                System.out.println(strBuffer);*/
//                result = new String(result.getBytes("GBK"), "UTF-8");
                System.out.println(result);
            }

/*            System.out.println(result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            System.out.println(jsonObject);*/

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    @Test
    public void PyhtonUtilTest() throws IOException{
    /*    org.springframework.core.io.Resource resource= new ClassPathResource("static/py/neo4j2json_cons.py");
        System.out.println(resource.getFile());*/
        MapDisplayController mapDisplayController= new MapDisplayController();
        mapDisplayController.getAllLabel();
        System.out.println(mapDisplayController.getAllLabel());
    }

}
