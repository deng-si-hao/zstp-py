package com.cavin.culture.controller;

import com.cavin.culture.model.Image;
import com.cavin.culture.service.ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/upload/pic")
public class UPImageController {

//    static String fileLocation = "/static/image/";//图片资源访问路径
//    static String pathLocation=new ClassPathResource("static/excl/元器件筛选、DPA数据.xlsx");
//    String pathLocal=System.getProperty("user.dir");
    //存储预返回页面的结果对象
    private Map<String, Object> result = new HashMap<>();

    //注入业务对象
    @Resource
    private ImageService imageService;

    /**
    * 接收前端传的fromData数据
    *
    * */
    @PostMapping("/addImage")
    public Map<String, Object> uploadPic(String name,String userId, MultipartFile pictureFile,
                                          HttpServletRequest request) throws IOException {
        //获取cookie中的用户id
        Cookie[] cookies = request.getCookies();
        String createBy=null;
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("userId"));
                createBy=cookie.getValue();
            }
        }
        //如果createBy为空，设置一个默认值(测试数据用)
        if(createBy==null){
            createBy="100012";
        }
        //获取当前时间
        String createDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        createDate = simpleDateFormat.format(new Date());

        //获取提交文件名称
        String filename = pictureFile.getOriginalFilename();
        //在文件更名为时间戳，避免重名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time=sdf.format(new Date());
        String type=filename.substring(filename.lastIndexOf("."));
        String newfilename=time+type;

        //定义上传文件存放的路径
//        String path = request.getSession().getServletContext().getRealPath(fileLocation);//此处为tomcat下的路径，服务重启路径会变化
       //存到根目录下
        String pathlocal=System.getProperty("user.dir")+"\\src\\main\\resources\\static\\image";
        //存到缓存文件下
//        String pathurl= ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/image/";
//        System.out.println(pathlocal);
    /*    //返回保存的url，根据url可以进行文件查看或者下载
        String filePath = request.getScheme() + "://" + request.getServerName()
//                + ":" + request.getServerPort() //端口 https443端口无需添加
                + pathlocal + filename;*/
        String pictureFileURL = pathlocal+"\\"+newfilename;//根路径+文件名
        //生成UUID用于标识图片
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        //获取当前登录用户id
//        System.out.println(pictureFileURL);
        //写入文件
        try {
//            pictureFile.write(pictureFileURL);
            fileupload(pictureFile.getBytes(),pathlocal,newfilename);
            //插入这条数据
            imageService.addImage(new Image(name, userId, uuid, pictureFileURL,createBy,new Date()));
            result.put("Result", "添加图片信息成功");
        } catch (IOException e) {
            e.printStackTrace();
            result.put("Result", "添加图片信息失败");
        }
        return result;
    }
/*    *//**
     * 获取当前系统路径
     *//*
    private String getUploadPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!path.exists()) path = new File("");
        File upload = new File(path.getAbsolutePath(), "static/upload/");
        if (!upload.exists()) upload.mkdirs();
        return upload.getAbsolutePath();
    }*/


    /**
    * @Param file 二进制文件
     * @Param filePath 文件路径
     * @Param fileName 文件名
    * 通过该方法将在指定目录下添加指定文件
    * */
    public static void fileupload(byte[] file,String filePath,String fileName) throws IOException {
        //目标目录
        File targetfile = new File(filePath);
        if(targetfile.exists()) {
            targetfile.mkdirs();
        }
        //二进制流写入
        FileOutputStream out = new FileOutputStream(filePath+"\\"+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    @RequestMapping("/findPicById")
    public List<Image> findPicById(String userId,Date startDate,Date endDate,String picName){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        String startTime=sdf.format(startDate);
        String endTime = sdf.format(endDate);
        Map<String,Object> param = new HashMap<>();
        param.put("userId",userId);
        param.put("startTime",startTime);
        param.put("endTime",endTime);
        param.put("picName",picName);
        List<Image> imageByUser= imageService.findById(param);
        System.out.println(imageByUser.size());
        return imageByUser;
    }

    @RequestMapping("/delUserId")
    public Map<String, Object> delUserId(String userId,int picId){
        try {
            imageService.delUserId(userId,picId);
            result.put("Result","删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("Result","删除失败！");
        }
        return result;
    }

}

