package com.cavin.culture.service;

import com.cavin.culture.dao.ImageDao;
import com.cavin.culture.model.Image;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ImageService {

    @Resource
    private ImageDao imageDao;

    public int addImage(Image image){
       return imageDao.addImage(image);
    }

    public List<Image> findById(String userId){
        return  imageDao.findById(userId);
    }

    public void delUserId(String userId,int picId){
        imageDao.updateUserId(userId, picId);
    }

}
