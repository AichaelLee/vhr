package org.sang.common;

import org.sang.bean.ManagersDetails;
import org.sang.bean.StudentDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * 得到当前用户信息
 * TODO 需要进行修改
 * @author: lizhizhong
 * CreatedDate: 2018/11/28.
 */
public class UserUtils {
    public static ManagersDetails getCurrentHr() {
        return (ManagersDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static User getCurrentStu() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof ManagersDetails){
            return (ManagersDetails) principal;
        }else{
            return (StudentDetails) principal;
        }
    }
}
