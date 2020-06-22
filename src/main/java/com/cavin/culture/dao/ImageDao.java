package com.cavin.culture.dao;

import com.cavin.culture.model.Image;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDao {
    // 添加信息
    int addImage(Image image);
}
