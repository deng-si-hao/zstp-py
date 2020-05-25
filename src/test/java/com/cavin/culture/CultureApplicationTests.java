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

    //�����߼�ɾ���û� ��Ϣ
  /*  @Test
    public void delUser(){
        Long i=1L;
        userService.delUserById(i);
        User user=userService.getUserById(i);
        System.out.println(user);
    }*/
    //�����޸��û���Ϣ
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
//        interp.exec("sys.path.append('D:/Program Files (x86)/jython2.7.0/Lib')");//jython�Լ���
//        interp.exec("sys.path.append('E:\\\\Tupu\\\\NOW\\\\tornado_kg copy\\\\venv')");//jython�Լ���
        interp.exec("sys.path.append('E:\\\\Tupu\\\\NOW\\\\tornado_kg copy\\\\venv\\\\lib\\\\site-packages')");//�����Լ�д��
        interp.exec("sys.path.append('E:\\\\Tupu\\\\NOW\\\\tornado_kg copy\\\\venv\\\\lib\\\\site-packages\\\\py2neo')");
        PythonInterpreter interpreter = new PythonInterpreter();
        //interpreter.execfile("D:\\neo2json.py");
        interpreter.execfile("E:\\testByTupu\\tornado_kg copy\\neo4j2json.py");
        // ��һ������Ϊ������õĺ����������������֣��ڶ�������Ϊ�������صĶ�������
        PyFunction pyFunction = interpreter.get("kg_constructor", PyFunction.class);
        //int a = 5, b = 10;
        //���ú��������������Ҫ��������Java�б����Ƚ�����ת��Ϊ��Ӧ�ġ�Python���͡�
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

        // 1. Python������ʽ���: ��Java�е���Python����
        /*String pythonFunc = "E:\\testByTupu\\tornado_kg copy\\neo4j2json.py";

        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("import sys");
        interp.exec("sys.path.append('E:\\Tupu\\NOW\\tornado_kg copy\\venv\\lib\\site-packages')");
        // ����python����
        interp.execfile(pythonFunc);
        // ����Python�����еĺ���
        PyFunction pyf = interp.get("main", PyFunction.class);
        PyObject dddRes = pyf.__call__();
        System.out.println(dddRes);
        interp.cleanup();
        interp.close();*/

        // 2. �������ʽ���: ��Java�е���Python����ʵ���ķ���
        String pythonClass = "E:\\testByTupu\\tornado_kg copy\\neo4j2json.py";
        // python������
        String pythonObjName = "cons";
        // python����
        String pythonClazzName = "kg_constructor";
        PythonInterpreter pi2 = new PythonInterpreter();
        // ����python����
        pi2.execfile(pythonClass);
        // ʵ����python����
        pi2.exec(pythonObjName + "=" + pythonClazzName + "()");
        // ��ȡʵ������python����
        PyObject pyObj = pi2.get(pythonObjName);
        // ����python���󷽷�,���ݲ��������շ���ֵ
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
            // ������
//            String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--cons"};
            // ��ȡȫ��ʵ���ǩ
            String[] args1 = new String[] { "python", "F:\\zhishitupu\\zstp\\src\\main\\resources\\static\\py\\neo4j2json_cons.py","--getlabel"};
            // ������
            //��ȡ��ǩ������ʵ��
//            String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getentitybylabel","e3"};
            //��ȡһ�ȹ�ϵ
       //     String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getkgR1","�������������˴����źŵ���������������ģʽʶ��"};
            // ��ȡ���·��
//            String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getkgShortestPath","����","��ά�ȴ���"};
            // ��ȡȫͼ
            //String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--getalldata"};
            //��ѯ��ͼ
            //String[] args1 = new String[] { "python", "C:\\Users\\86173\\Documents\\WeChat Files\\d15095827251\\FileStorage\\File\\2020-05\\neo4j2json_cons.py","--searchsubkg", "�������������˴����źŵ���������������ģʽʶ�����ά�ȴ����Ĺ�ϵ��ʲô"};
            //Process proc = Runtime.getRuntime().exec("python3 /Users/gunanxi/Downloads/md/project/2020-03_304/tornado_kg/kg_304/kg/neo4j2json.py");
            Process p;
            p = Runtime.getRuntime().exec(args1);
            //ȡ���������������
            InputStream fis=p.getInputStream();
            //��һ�����������ȥ��
            InputStreamReader isr=new InputStreamReader(fis);
            //�û���������
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            String result="";
            //ֱ������Ϊֹ
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
