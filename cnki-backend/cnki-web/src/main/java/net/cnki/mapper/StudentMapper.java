package net.cnki.mapper;

import net.cnki.bean.Role;
import net.cnki.bean.TblStudentBase;

import java.util.List;

/**
 * Created by aichaellee on 2018/11/30.
 */
public interface StudentMapper {

    TblStudentBase loadUserByUsername(String username);

    List<Role> getRolesByStudentId(Integer id);


}
