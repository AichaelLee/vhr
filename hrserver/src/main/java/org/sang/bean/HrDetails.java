package org.sang.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by aichaellee on 2018/11/28.
 */
public class HrDetails extends User {

    private final Hr2 hr2;

    public HrDetails(Hr2 hr,Collection<? extends GrantedAuthority> authorities){
        super(hr.getName(),hr.getPassword(),authorities);
        this.hr2 = hr;
    }

}
