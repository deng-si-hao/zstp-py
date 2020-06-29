package com.cavin.culture.dao;

import com.cavin.culture.model.Image;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageDao {
    // 添加信息
    int addImage(Image image);
    //按照用户id查询所属图片
    List<Image> findById(@Param("userId") String userId);
    //根据userId删除对应分享
    void delUserId(@Param("userId")String userId,@Param("picId")int picId);
}
