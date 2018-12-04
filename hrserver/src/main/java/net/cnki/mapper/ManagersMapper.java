package net.cnki.mapper;

import net.cnki.bean.Managers;
import net.cnki.bean.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
public interface ManagersMapper {

    Managers loadUserByUsername(String username);

    List<Role> getRolesByManagerId(Long id);

    int addRolesForManager(@Param("userId") Long hrId, @Param("rids") Long[] rids);

    Managers getManagerById(Long hrId);

    List<Managers> getAllManagers(@Param("currentId") Long currentId);
}
