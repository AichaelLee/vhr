package net.cnki.mapper;

import net.cnki.bean.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
public interface HrMapper {

    List<Role> getRolesByHrId(Long id);



    int deleteRoleByHrId(Long userId);

    int addRolesForHr(@Param("userId") Long hrId, @Param("rids") Long[] rids);

}
