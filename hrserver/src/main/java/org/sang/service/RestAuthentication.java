package org.sang.service;

import org.sang.bean.Hr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * Created by aichaellee on 2018/11/28.
 */
//@Service
public class RestAuthentication {

    @Autowired
    private SimpleAuthorityMapper simpleAuthorityMapper;

    public void resetUserAuthorities(Hr hr){
        Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                oldAuth.getPrincipal(),oldAuth.getCredentials(),
                simpleAuthorityMapper.mapAuthorities(getAuthorities(hr)));
        SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }


    private Collection<GrantedAuthority> getAuthorities(Hr users) {

        if (users != null) {

                return AuthorityUtils.createAuthorityList("ROLE_manager");
        }
        return AuthorityUtils.createAuthorityList();
    }
}
