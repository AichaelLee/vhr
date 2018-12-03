package net.cnki.mapper;

import net.cnki.bean.Managers;
import org.apache.ibatis.annotations.Param;
import net.cnki.bean.Hr;
import net.cnki.bean.Role;

import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
public interface HrMapper {
    Managers loadUserByUsername(String username);

    List<Role> getRolesByHrId(Long id);

    int hrReg(@Param("username") String username, @Param("password") String password);

    List<Hr> getHrsByKeywords(@Param("keywords") String keywords);


    int deleteRoleByHrId(Long userId);

    int addRolesForHr(@Param("userId") Long hrId, @Param("rids") Long[] rids);

    Hr getHrById(Long hrId);

    List<Hr> getAllHr(@Param("currentId") Long currentId);
}
