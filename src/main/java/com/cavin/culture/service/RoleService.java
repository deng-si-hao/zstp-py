package com.cavin.culture.service;

import com.cavin.culture.dao.RoleDao;
import com.cavin.culture.model.SysRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoleService {

    @Resource
    private RoleDao roleDao;

    public void save(SysRole role){
        if(role != null){
            if(checkExist(role.getRoleName(),role.getRoleId())){
               roleDao.update(role);
            }else {
                roleDao.insert(role);
            }
        }

    }





    private boolean checkExist(String name, Integer id) {
        return roleDao.countByName(name, id) > 0;
    }
}
