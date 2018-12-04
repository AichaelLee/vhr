package net.cnki.service;

import net.cnki.bean.Role;
import net.cnki.bean.TblTeacherBase;
import net.cnki.mapper.TeachersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by aichaellee on 2018/11/30.
 */
@Service
public class TeacherService {

    @Autowired
    TeachersMapper teachersMapper;

    public TblTeacherBase loadTeachertByName(String username){
        return teachersMapper.loadUserByUsername(username);
    }

    public List<Role> getRolesByTeacherId(Long id){

        return teachersMapper.getRolesByTeacherId(id);
    }
}
