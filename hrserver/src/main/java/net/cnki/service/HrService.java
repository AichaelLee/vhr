package net.cnki.service;

import net.cnki.bean.Hr;
import net.cnki.bean.Managers2;
import net.cnki.bean.Role;
import net.cnki.mapper.HrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
@Service
@Transactional
public class HrService {

    @Autowired
    HrMapper hrMapper;


    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Managers2 managers = hrMapper.loadUserByUsername(s);
        if (managers == null) {
            throw new UsernameNotFoundException("用户名不对");
        }
        return managers;

    }



    protected Collection<GrantedAuthority> getAuthorities(Managers2 users) {

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

    public int hrReg(String username, String password) {
        //如果用户名存在，返回错误
        if (hrMapper.loadUserByUsername(username) != null) {
            return -1;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(password);
        return hrMapper.hrReg(username, encode);
    }

    public List<Hr> getHrsByKeywords(String keywords) {
        return hrMapper.getHrsByKeywords(keywords);
    }


    public int updateHrRoles(Long hrId, Long[] rids) {
        int i = hrMapper.deleteRoleByHrId(hrId);
        return hrMapper.addRolesForHr(hrId, rids);
    }

    public Hr getHrById(Long hrId) {
        return hrMapper.getHrById(hrId);
    }


    public List<Role> getRolesByHrId(Long id){
        return hrMapper.getRolesByHrId(id);
    }

    public List<Hr> getAllHr() {
        return hrMapper.getAllHr(null);
    }
}
