package org.sang.service;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SecurityUserDetailService implements UserDetailsService{

    @Autowired
    StudentUserDetailService studentUserDetailService;

    @Autowired
    HrService hrService;


    // 学生用户
    public static final String STUDENT = "1";

    // 管理员用户
    public static final String ADMIN = "2";

    // 教师用户
    public static final String TEACHER = "3";


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {


        String[] usernameAndUserType = StringUtils.split(s, String.valueOf(Character.LINE_SEPARATOR));
        if (usernameAndUserType == null || usernameAndUserType.length != 2) {
            throw new UsernameNotFoundException("Username and UserType must be provided");
        }
        String username = usernameAndUserType[0];
        String userType = usernameAndUserType[1];


        // 如果账号类型为 [学生]
        if(userType.equals(STUDENT)){
            return studentUserDetailService.loadUserByUsername(username);
        }

        else if(userType.equals(ADMIN)){
            // 如果账号类型为 [管理员]
            return hrService.loadUserByUsername(username);
        }

        else if(userType.equals(TEACHER)){
            // 如果账号类型为 [教师] TODO
            return hrService.loadUserByUsername(username);
        }
        else {
            // 错误的类型，抛出异常
            log.error("错误的用户类型!");
            throw new IllegalArgumentException("未知的用户类型:" + userType);
        }


    }

}
