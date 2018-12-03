package net.cnki.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by aichaellee on 2018/11/26.
 */
public class ManagersDetails extends User {

    private final Managers managers;

    public ManagersDetails(Managers hr, Collection<? extends GrantedAuthority> authorities){
        super(hr.getName(),hr.getPassword(),authorities);
        this.managers = hr;
    }

    public Managers getManagers(){
        return managers;
    }

}
