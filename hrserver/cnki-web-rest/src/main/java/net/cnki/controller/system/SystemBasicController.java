package net.cnki.controller.system;

import lombok.extern.slf4j.Slf4j;
import net.cnki.bean.*;
import net.cnki.common.UserUtils;
import net.cnki.common.fw.LogType;
import net.cnki.common.fw.annotation.SystemLog;
import net.cnki.mapper.TblPlanMapper;
import net.cnki.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统基本设置
 * @author: lizhizhong
 * CreatedDate: 2018/11/26.
 */
@RestController
@RequestMapping("/system/basic")
@Slf4j
public class SystemBasicController {
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;
    @Autowired
    MenuRoleService menuRoleService;

    @Autowired
    HrService hrService;

    @Autowired
    TblPlanMapper tblPlanMapper;

    /**切换用户选择的角色**/
    @Autowired
    RestAuthentication restAuthentication;


    @Autowired
    private SimpleAuthorityMapper simpleAuthorityMapper;



    /**
     * 得到所有角色,注意不是查看某用户的所有角色
     * @return 当前系统存在的所有角色
     */
    @RequestMapping("/roles")
    public List<Role> allRoles() {
        return roleService.roles();
    }

    @GetMapping("/userRoles")
    @SystemLog(type = LogType.AUDITING,description = "得到用户拥有的所有角色")
    public List<Role> getRolesById(){
        return roleService.getRolesByUserId(UserUtils.getCurrentUser().getId());
    }


    @PostMapping(value = "/chooseRole")
    @SystemLog(type = LogType.AUDITING,description = "选择角色")
    public RespBean chooseRole(@AuthenticationPrincipal Object principal, String choosedRole) throws Exception{

        if(principal instanceof Managers){

            log.info("管理员用户之前的全部角色为{}",((Managers) principal).getAuthorities().toString());

            // 根据需求决定
            return RespBean.error("管理员不需要选择角色!");

        }else if(principal instanceof TblTeacherBase){
            log.info("教师角色为:{}",((TblTeacherBase) principal).getAuthorities().toString());

        }
        else{

            log.info("学生用户之前的全部角色为{}", ((TblStudentBase) principal).getAuthorities().toString());
            // 根据需求决定
            return RespBean.error("学生不需要选择角色!");

        }


        log.info("用户选择的角色为:{}",choosedRole);

        // 切换用户选择的角色
        restAuthentication.resetUserAuthorities(choosedRole);


        List<Role> newRoles = new ArrayList<>();
        // TODO if null
        roleService.getRolesByUserId(UserUtils.getCurrentUser().getId())
        .stream().forEach(o->{
            if(o.getName().equals(choosedRole)){
                newRoles.add(o);
            }
        });

        UserUtils.getCurrentUser().setRoles(newRoles);

        return  RespBean.ok("", UserUtils.getCurrentUser());
    }

    /**
     * 当用户点击右上角的切换按钮的时候
     * @param details
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/switchRole")
    public List<Role> switchRole(@AuthenticationPrincipal Managers details) throws Exception{

        log.info("此时用户权限为{}",details.getAuthorities().toString());

        // 查询数据库,得到用户初始的所有角色返回给前端

        List<Role> roles = hrService.getRolesByHrId(details.getId());

        return roles;

    }

    /**
     * 获得该学校的所有学年
     * @return
     */
    @GetMapping(value="/getPlans")
//    @Cacheable("getPlans")
    public RespBean getPlans(){
        TblPlanExample example = new TblPlanExample();
        example.createCriteria().andPlanIdIsNotNull();
        List<TblPlan> plans = tblPlanMapper.selectByExample(example);
        log.info("学年总数为:{}",plans.size());

        return RespBean.ok("",plans);
    }

    /**
     * 教师用户选择完角色后,进入主页前重新获得权限,此时用户只有一个权限
     *
     * TODO 后续或许会加上一个Choose_Role 和 Choose_Role_OK 的这两个权限
     *
     * **/
    @Deprecated
    @GetMapping(value="/getSwitchAuth")
    public RespBean getSwitchAuth(@AuthenticationPrincipal Object principal){
        RespBean respBean = null;
        if(principal instanceof Managers){
            respBean =RespBean.ok("选择角色成功!",  ((Managers) principal));;
        }else{
            respBean =RespBean.ok("选择角色成功!",  ((TblStudentBase) principal));
        }
        return respBean;



    }

    /**
     * 删除角色
     * @param rid
     * @return
     */
    @RequestMapping(value = "/role/{rid}", method = RequestMethod.DELETE)
    public RespBean deleteRole(@PathVariable Long rid) {
        if (roleService.deleteRoleById(rid) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    /**
     * 添加角色
     * @param role
     * @param roleZh
     * @return
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    public RespBean addNewRole(String role, String roleZh) {
        if (roleService.addNewRole(role, roleZh) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @RequestMapping(value = "/menuTree/{rid}", method = RequestMethod.GET)
    public Map<String, Object> menuTree(@PathVariable Long rid) {
        Map<String, Object> map = new HashMap<>();
        List<Menu> menus = menuService.menuTree();
        map.put("menus", menus);
        List<Long> selMids = menuService.getMenusByRid(rid);
        map.put("mids", selMids);
        return map;
    }

    /**
     * 更新菜单权限 TODO
     * @param rid
     * @param mids
     * @return
     */
    @RequestMapping(value = "/updateMenuRole", method = RequestMethod.PUT)
    public RespBean updateMenuRole(Long rid, Long[] mids) {
        if (menuRoleService.updateMenuRole(rid, mids) == mids.length) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }


}
