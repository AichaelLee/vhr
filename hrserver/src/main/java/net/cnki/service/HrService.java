package net.cnki.service;

import net.cnki.bean.Managers;
import net.cnki.bean.Role;
import net.cnki.mapper.HrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lizhizhong on 2018/11/28.
 */
@Service
@Transactional
public class HrService {

    @Autowired
    HrMapper hrMapper;




    protected Collection<GrantedAuthority> getAuthorities(Managers users) {

        if (users != null) {

            List<GrantedAuthority> authorities = new ArrayList<>();
            List<Role> roles = users.getRoles();
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return authorities;
        }
        return AuthorityUtils.createAuthorityList();
    }



    public int updateHrRoles(Long hrId, Long[] rids) {
        int i = hrMapper.deleteRoleByHrId(hrId);
        return hrMapper.addRolesForHr(hrId, rids);
    }


    public List<Role> getRolesByHrId(Long id){
        return hrMapper.getRolesByHrId(id);
    }

}
