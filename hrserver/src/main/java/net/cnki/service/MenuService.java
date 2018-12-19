package net.cnki.service;

import net.cnki.bean.Menu;
import net.cnki.common.UserUtils;
import net.cnki.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lizhizhong on 2018/11/28.
 */
@Service
@Transactional
//@CacheConfig(cacheNames = "menus_cache")
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

////    @Cacheable(key = "#root.methodName")
//    @Cacheable(key = "#root.methodName")
    public List<Menu> getAllMenu(){
        return menuMapper.getAllMenu();
    }
//    @Cacheable(key = "#root.methodName")
    public List<Menu> getMenusByUserId() {
        System.out.println("1=2333=2=31230294032490234"+UserUtils.getCurrentUser().getId());
        return menuMapper.getMenusByUserId(UserUtils.getCurrentUser().getId());
       // return null;
    }
//    @Cacheable(key = "#root.methodName")
    public List<Menu> menuTree() {
        return menuMapper.menuTree();
    }

//    @Cacheable(key = "#root.methodName")
    public List<Long> getMenusByRid(Long rid) {
        return menuMapper.getMenusByRid(rid);
    }

//    @Cacheable(key = "#root.methodName")
    public List<Menu> getMemusInfoByRid(Long rid){
        return menuMapper.getMemusInfoByRid(rid);
    }
}
