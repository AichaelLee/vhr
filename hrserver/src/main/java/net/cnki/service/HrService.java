package net.cnki.service;

import net.cnki.bean.Managers;
import net.cnki.mapper.HrMapper;
import net.cnki.bean.Hr;
import net.cnki.bean.ManagersDetails;
import net.cnki.bean.Role;
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
        Managers hr = hrMapper.loadUserByUsername(s);
        if (hr == null) {
            throw new UsernameNotFoundException("用户名不对");
        }
        return new ManagersDetails(hr, getAuthorities(hr));

    }



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

    public int updateHr(Hr hr) {
        return hrMapper.updateHr(hr);
    }

    public int updateHrRoles(Long hrId, Long[] rids) {
        int i = hrMapper.deleteRoleByHrId(hrId);
        return hrMapper.addRolesForHr(hrId, rids);
    }

    public Hr getHrById(Long hrId) {
        return hrMapper.getHrById(hrId);
    }

    public int deleteHr(Long hrId) {
        return hrMapper.deleteHr(hrId);
    }

    public List<Role> getRolesByHrId(Long id){
        return hrMapper.getRolesByHrId(id);
    }

    public List<Hr> getAllHrExceptAdmin() {
        //return hrMapper.getAllHr(UserUtils.getCurrentHr().getManagers());
        return null;
    }
    public List<Hr> getAllHr() {
        return hrMapper.getAllHr(null);
    }
}
