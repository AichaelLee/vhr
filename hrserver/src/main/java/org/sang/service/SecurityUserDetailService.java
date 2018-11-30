package org.sang.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by aichaellee on 2018/11/30.
 */
@Service
public class SecurityUserDetailService implements UserDetailsService{

    @Autowired
    StudentUserDetailService studentUserDetailService;

    @Autowired
    HrService hrService;

    public static final String STUDENT = "1";

    public static final String ADMIN = "2";

    public static final String TEACHER = "3";


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {


        String[] usernameAndUserType = StringUtils.split(s, String.valueOf(Character.LINE_SEPARATOR));
        if (usernameAndUserType == null || usernameAndUserType.length != 2) {
            throw new UsernameNotFoundException("Username and UserType must be provided");
        }
        String username = usernameAndUserType[0];
        String userType = usernameAndUserType[1];

        System.out.println("usertypeadndname==========="+username);
        System.out.println("usertype==========="+userType);

        // 如果账号类型为 [学生]
        if(userType.equals(STUDENT)){
            return studentUserDetailService.loadUserByUsername(username);
        }

        else if(userType.equals(ADMIN)){
            // 如果账号类型为 [管理员]
            return hrService.loadUserByUsername(username);
        }

        else {
            // 如果账号类型为 [教师] TODO
            return hrService.loadUserByUsername(username);
        }

        // 如果账号类型为 教师


    }

}
