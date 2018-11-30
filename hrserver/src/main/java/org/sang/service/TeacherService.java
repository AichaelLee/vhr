package org.sang.service;

import org.sang.bean.Role;
import org.sang.bean.TeacherBase;
import org.sang.mapper.TeachersMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by aichaellee on 2018/11/30.
 */
public class TeacherService {

    @Autowired
    TeachersMapper studentMapper;

    public TeacherBase loadStudentByUsername(String username){
        return studentMapper.loadUserByUsername(username);
    }

    public List<Role> getRolesByStudentId(Integer id){
        return studentMapper.getRolesByStudentId(id);
    }
}
