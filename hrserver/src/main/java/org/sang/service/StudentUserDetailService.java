package org.sang.service;

import org.sang.bean.Role;
import org.sang.bean.StudentDetails;
import org.sang.bean.TblStudentBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class StudentUserDetailService{

    @Autowired
    StudentService studentService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TblStudentBase studentBase = studentService.loadStudentByUsername(username);
        if (studentBase == null) {
            throw new UsernameNotFoundException("用户名错误");
        }
        return new StudentDetails(studentBase, getAuthorities(studentBase));

    }

    protected Collection<GrantedAuthority> getAuthorities(TblStudentBase studentBase) {

            // 得到学生的权限(实际上以学生登录只有[学生]这一个角色,但是我们还是要赋予他这个角色)
            List<GrantedAuthority> authorities = new ArrayList<>();
            List<Role> roles = studentService.getRolesByStudentId(studentBase.getSid());
        System.out.println("--------roelssss"+ roles.size());
           // List<Role> roles = studentBase.getRoles();
//            for (Role role : roles) {
//                authorities.add(new SimpleGrantedAuthority("ROLE_studentsss"));
//            }
        authorities.add(new SimpleGrantedAuthority("ROLE_studentsss"));
            return authorities;
    }

}
