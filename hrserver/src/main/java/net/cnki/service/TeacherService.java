package net.cnki.service;

import net.cnki.bean.Role;
import net.cnki.bean.TeacherBase;
import net.cnki.mapper.TeachersMapper;
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
