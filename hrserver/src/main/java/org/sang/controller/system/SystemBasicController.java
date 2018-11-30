package org.sang.controller.system;

import org.sang.bean.*;
import org.sang.common.UserUtils;
import org.sang.mapper.TblPlanMapper;
import org.sang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
public class SystemBasicController {
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;
    @Autowired
    MenuRoleService menuRoleService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    PositionService positionService;
    @Autowired
    JobLevelService jobLevelService;

    @Autowired
    HrService hrService;

    @Autowired
    TblPlanMapper tblPlanMapper;

    /**切换用户选择的角色**/
    @Autowired
    RestAuthentication restAuthentication;


    @Autowired
    private SimpleAuthorityMapper simpleAuthorityMapper;

    @RequestMapping(value = "/role/{rid}", method = RequestMethod.DELETE)
    public RespBean deleteRole(@PathVariable Long rid) {
        if (roleService.deleteRoleById(rid) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @PostMapping(value = "/chooseRole")
    public RespBean chooseRole(@AuthenticationPrincipal Object principal, String choosedRole) throws Exception{

        if(principal instanceof ManagersDetails){
            System.out.println("管理员用户之前的全部角色为"+
                    ((ManagersDetails) principal).getAuthorities().toString());
        }else{
            System.out.println("学生用户之前的全部角色为"+
                    ((StudentDetails) principal).getAuthorities().toString());
        }


        System.out.println("用户选择的角色为:"+choosedRole);

        // 切换用户选择的角色
        restAuthentication.resetUserAuthorities(choosedRole);

        System.out.println("重置之后的角色为:"+SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

        Role newRole = new Role();
        newRole.setName(choosedRole);
        UserUtils.getCurrentHr().getManagers().setRoles(Arrays.asList(newRole));
        return  RespBean.ok("登录成功!", UserUtils.getCurrentHr().getManagers());
    }

    /**
     * 当用户点击右上角的切换按钮的时候
     * @param details
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/switchRole", method = RequestMethod.GET)
    public RespBean switchRole(@AuthenticationPrincipal ManagersDetails details) throws Exception{

        System.out.println("此时用户权限为"+details.getAuthorities().toString());

        // 查询数据库,得到用户初始的所有角色返回给前端

        List<Role> roles = hrService.getRolesByHrId(details.getManagers().getId());


        UserUtils.getCurrentHr().getManagers().setRoles(roles);


        return RespBean.ok("登录成功!", UserUtils.getCurrentHr().getManagers());
    }

    /**
     * 获得该学校的所有学年
     * @return
     */
    @GetMapping(value="/getPlans")
    public RespBean getPlans(){
        TblPlanExample example = new TblPlanExample();
        example.createCriteria().andPlanIdIsNotNull();
        List<TblPlan> plans = tblPlanMapper.selectByExample(example);
        System.out.println("---------------"+plans.size());

        return RespBean.ok("获取所有学年成功",plans);
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
        if(principal instanceof ManagersDetails){
            respBean =RespBean.ok("选择角色成功!",  ((ManagersDetails) principal).getManagers());;
        }else{
            respBean =RespBean.ok("选择角色成功!",  ((StudentDetails) principal).getStudent());
        }
        return respBean;



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

    /**
     * 得到所有角色,注意不是查看某用户的所有角色
     * @return 当前系统存在的所有角色
     */
    @RequestMapping("/roles")
    public List<Role> allRoles() {
        return roleService.roles();
    }

    @RequestMapping(value = "/dep", method = RequestMethod.POST)
    public Map<String, Object> addDep(Department department) {
        Map<String, Object> map = new HashMap<>();
        if (departmentService.addDep(department) == 1) {
            map.put("status", "success");
            map.put("msg", department);
            return map;
        }
        map.put("status", "error");
        map.put("msg", "添加失败!");
        return map;
    }

    @RequestMapping(value = "/dep/{did}", method = RequestMethod.DELETE)
    public RespBean deleteDep(@PathVariable Long did) {
        if (departmentService.deleteDep(did) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @RequestMapping(value = "/dep/{pid}", method = RequestMethod.GET)
    public List<Department> getDepByPid(@PathVariable Long pid) {
        return departmentService.getDepByPid(pid);
    }

    @RequestMapping(value = "/deps", method = RequestMethod.GET)
    public List<Department> getAllDeps() {
        return departmentService.getAllDeps();
    }

    @RequestMapping(value = "/position", method = RequestMethod.POST)
    public RespBean addPos(Position pos) {
        int result = positionService.addPos(pos);
        if (result == 1) {
            return RespBean.ok("添加成功!");
        } else if (result == -1) {
            return RespBean.error("职位名重复，添加失败!");
        }
        return RespBean.error("添加失败!");
    }

    @RequestMapping(value = "/positions", method = RequestMethod.GET)
    public List<Position> getAllPos() {
        return positionService.getAllPos();
    }

    @RequestMapping("/position/{pids}")
    public RespBean deletePosById(@PathVariable String pids) {
        if (positionService.deletePosById(pids)) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @RequestMapping(value = "/position", method = RequestMethod.PUT)
    public RespBean updatePosById(Position position) {
        if (positionService.updatePosById(position) == 1) {
            return RespBean.ok("修改成功!");
        }
        return RespBean.error("修改失败!");
    }

    @RequestMapping(value = "/joblevel", method = RequestMethod.POST)
    public RespBean addJobLevel(JobLevel jobLevel) {
        int result = jobLevelService.addJobLevel(jobLevel);
        if (result == 1) {
            return RespBean.ok("添加成功!");
        } else if (result == -1) {
            return RespBean.error("职称名重复，添加失败!");
        }
        return RespBean.error("添加失败!");
    }

    @RequestMapping(value = "/joblevels", method = RequestMethod.GET)
    public List<JobLevel> getAllJobLevels() {
        return jobLevelService.getAllJobLevels();
    }

    @RequestMapping(value = "/joblevel/{ids}", method = RequestMethod.DELETE)
    public RespBean deleteJobLevelById(@PathVariable String ids) {
        if (jobLevelService.deleteJobLevelById(ids)) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @RequestMapping(value = "/joblevel", method = RequestMethod.PUT)
    public RespBean updateJobLevel(JobLevel jobLevel) {
        if (jobLevelService.updateJobLevel(jobLevel) == 1) {
            return RespBean.ok("修改成功!");
        }
        return RespBean.error("修改失败!");
    }
}
