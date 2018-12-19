package net.cnki.service;

import lombok.extern.slf4j.Slf4j;
import net.cnki.common.UserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author: lizhizhong
 * CreatedDate: 2018/11/30.
 */
@Service
@Slf4j
public class SecurityUserDetailService implements UserDetailsService{

    @Autowired
    StudentUserDetailService studentUserDetailService;

    @Autowired
    HrService hrService;

    @Autowired
    ManagerUserDetailService managerUserDetailService;

    @Autowired
    TeacherUserDetailService teacherUserDetailService;



    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {


        String[] usernameAndUserType = StringUtils.split(s, String.valueOf(Character.LINE_SEPARATOR));
        if (usernameAndUserType == null || usernameAndUserType.length != 2) {
            throw new UsernameNotFoundException("Username and UserType must be provided");
        }
        String username = usernameAndUserType[0];
        String userType = usernameAndUserType[1];

        System.out.println("========登录用户类型为=========="+UserTypeEnum.getUserTypeZh(userType)+"asdfasdfsadfmingziwei:"+username);

        log.info("登录用户类型为:{},用户名为:{}",UserTypeEnum.getUserTypeZh(userType),username);

        // 如果账号类型为 [学生]
        if(userType.equals(UserTypeEnum.STUDENT.getUserType())){
            return studentUserDetailService.loadUserByUsername(username);
        }

        // 如果账号类型为 [管理员]
        else if(userType.equals(UserTypeEnum.ADMIN.getUserType())){

            return managerUserDetailService.loadUserByUsername(username);
        }

        // 如果账号类型为 [教师]
        else if(userType.equals(UserTypeEnum.TEACHER.getUserType())){

            return teacherUserDetailService.loadUserByUsername(username);

            //return hrService.loadUserByUsername(username);
        }
        else {
            //TODO 前台暂时没有改,所有先不用抛出异常

            //return hrService.loadUserByUsername(username);
            // 错误的类型，抛出异常
            log.error("错误的用户类型!");
            throw new IllegalArgumentException("未知的用户类型:" + userType);
        }


    }

}
