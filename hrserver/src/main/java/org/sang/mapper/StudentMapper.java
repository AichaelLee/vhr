package org.sang.mapper;

import org.sang.bean.Role;
import org.sang.bean.TblStudentBase;

import java.util.List;

/**
 * Created by aichaellee on 2018/11/30.
 */
public interface StudentMapper {

    TblStudentBase loadUserByUsername(String username);

    List<Role> getRolesByStudentId(Integer id);


}
