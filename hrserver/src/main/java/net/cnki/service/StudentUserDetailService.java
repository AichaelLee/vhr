package net.cnki.service;

import lombok.extern.slf4j.Slf4j;
import net.cnki.bean.StudentDetails;
import net.cnki.bean.TblStudentBase;
import net.cnki.bean.Role;
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
 * Created by lizhizhong on 2018/11/28.
 */
@Service
@Transactional
@Slf4j
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
            log.info("该学生权限为:{}", roles.size());
           // List<Role> roles = studentBase.getRoles();
//            for (Role role : roles) {
//                authorities.add(new SimpleGrantedAuthority("ROLE_studentsss"));
//            }
            authorities.add(new SimpleGrantedAuthority("ROLE_student"));
            return authorities;
    }

}
