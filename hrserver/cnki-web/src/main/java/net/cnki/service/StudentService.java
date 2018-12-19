package net.cnki.service;

import net.cnki.bean.Role;
import net.cnki.bean.TblStudentBase;
import net.cnki.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by aichaellee on 2018/11/30.
 */
@Service
public class StudentService {

    @Autowired
    StudentMapper studentMapper;

    public TblStudentBase loadStudentByUsername(String username){
        return studentMapper.loadUserByUsername(username);
    }

    public List<Role> getRolesByStudentId(Integer id){
        return studentMapper.getRolesByStudentId(id);
    }
}
