package net.cnki.common;

import net.cnki.bean.Managers;
import net.cnki.bean.TblStudentBase;
import net.cnki.bean.UserBase;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 得到当前用户信息
 * TODO 需要进行修改
 * @author: lizhizhong
 * CreatedDate: 2018/11/28.
 */
public class UserUtils {
    public static UserBase getCurrentHr() {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(o instanceof Managers){
            return (Managers)o;
        }else {
            return (TblStudentBase) o;
        }

    }
    public static UserDetails getCurrentStu() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof Managers){
            return (Managers) principal;
        }else{
            return (TblStudentBase) principal;
        }
    }
}
