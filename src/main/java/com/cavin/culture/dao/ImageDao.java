package com.cavin.culture.dao;

import com.cavin.culture.model.Image;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ImageDao {
    // 添加信息
    int addImage(Image image);

    //按照用户id查询所属图片
    List<Image> findById(Map<String, Object> param);

    //根据userId删除对应分享
    void updateUserId(@Param("userId") String userId, @Param("picId") String picId);

    //获取数据数量
    int getCount(Map<String, Object> param);

    //添加图片到数据库
    int savePhoto(Image image);
}
