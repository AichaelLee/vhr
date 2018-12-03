package net.cnki.controller;

import lombok.extern.slf4j.Slf4j;
import net.cnki.bean.Managers;
import net.cnki.common.UserUtils;
import net.cnki.service.MenuService;
import net.cnki.bean.Menu;
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
    public List<Menu> getsyMenu(){

        Long rid = UserUtils.getCurrentHr().getManagers().getRoles().get(0).getId();
        String rzh = UserUtils.getCurrentHr().getManagers().getRoles().get(0).getName();
        log.info("选择用户角色为{},id为:{},该角色拥有的菜单权限为",rzh,rid);

        return menuService.getMemusInfoByRid(rid);

    }

    /**
     * 获取当前登录用户的详细信息
     * @return 当前用户
     */
    @RequestMapping("/hr")
    public Managers currentUser() {
        return UserUtils.getCurrentHr().getManagers();
    }
}
