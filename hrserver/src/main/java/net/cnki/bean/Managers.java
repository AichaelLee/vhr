package net.cnki.bean;

import lombok.Data;

import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
@Data
public class Managers {
    private Long id;
    private String name;
    private String phone;
    private String telephone;
    private String address = "this is origin adress";
    private boolean enabled;
    private String username;
    private String password;
    private String remark;
    private List<Role> roles;
    private String userface;
}