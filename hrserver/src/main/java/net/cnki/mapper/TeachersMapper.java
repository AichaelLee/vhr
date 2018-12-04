package net.cnki.mapper;

import net.cnki.bean.Role;
import net.cnki.bean.TblTeacherBase;

import java.util.List;

/**
 * Created by aichaellee on 2018/11/30.
 */
public interface TeachersMapper {

    TblTeacherBase loadUserByUsername(String username);

    List<Role> getRolesByTeacherId(Long TID);
    List<Role> getTeacherById();


}
