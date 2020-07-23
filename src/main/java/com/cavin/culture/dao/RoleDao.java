package com.cavin.culture.dao;

import com.cavin.culture.model.SysRole;
import org.apache.ibatis.annotations.Param;


public interface RoleDao {

    int countByName(@Param("name") String name, @Param("id") Integer id);

    void insert(SysRole sysRole);

    int update(SysRole sysRole);


}
