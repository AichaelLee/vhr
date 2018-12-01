package net.cnki.controller;

import net.cnki.bean.Managers;
import net.cnki.common.UserUtils;
import net.cnki.service.MenuService;
import net.cnki.bean.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 这是一个只要登录就能访问的Controller
 * 主要用来获取一些配置信息
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    MenuService menuService;
    @RequestMapping("/sysmenu")
    public List<Menu> sysmenu() {
        return menuService.getMenusByHrId();
    }

    @RequestMapping("/hr")
    public Managers currentUser() {
        return UserUtils.getCurrentHr().getManagers();
    }
}
