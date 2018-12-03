package net.cnki.service;

import net.cnki.common.UserUtils;
import net.cnki.bean.Menu;
import net.cnki.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lizhizhong on 2018/11/28.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "menus_cache")
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

//    @Cacheable(key = "#root.methodName")
    public List<Menu> getAllMenu(){
        return menuMapper.getAllMenu();
    }

    public List<Menu> getMenusByUserId() {
        return menuMapper.getMenusByUserId(UserUtils.getCurrentHr().getId());
       // return null;
    }

    public List<Menu> menuTree() {
        return menuMapper.menuTree();
    }

    public List<Long> getMenusByRid(Long rid) {
        return menuMapper.getMenusByRid(rid);
    }


    public List<Menu> getMemusInfoByRid(Long rid){
        return menuMapper.getMemusInfoByRid(rid);
    }
}
