package net.cnki.controller;

import lombok.extern.slf4j.Slf4j;
import net.cnki.bean.Menu;
import net.cnki.bean.Role;
import net.cnki.common.UserUtils;
import net.cnki.common.fw.LogType;
import net.cnki.common.fw.annotation.SystemLog;
import net.cnki.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 这是一个只要登录就能访问的Controller
 * 主要用来获取一些配置信息
 * @author: lizhizhong
 * CreatedDate: 2018/11/28.
 */

@RestController
@RequestMapping("/config")
@Slf4j
public class ConfigController {
    @Autowired
    MenuService menuService;

    /**
     * 获取当前用户可访问的菜单选项
     * @return 菜单
     */
    @RequestMapping("/sysmenu")
    public List<Menu> sysmenu() {
        return menuService.getMenusByUserId();
    }

    /**
     * 根据用户所选定的角色重新获得该角色所拥有的菜单权限
     * @return 菜单项
     */
    @GetMapping("/regetMenu")
    @SystemLog(type = LogType.AUDITING,description = "获取系统菜单")
    public List<Menu> getsyMenu(){

        List<Role> rrr = UserUtils.getCurrentUser().getRoles();
        System.out.println("============sadfadf"+rrr.size());
        Long rid = UserUtils.getCurrentUser().getRoles().get(0).getId();
        String rzh = UserUtils.getCurrentUser().getRoles().get(0).getName();
        log.info("选择用户角色为{},id为:{},该角色拥有的菜单权限为",rzh,rid);

        return menuService.getMemusInfoByRid(rid);

    }


}
