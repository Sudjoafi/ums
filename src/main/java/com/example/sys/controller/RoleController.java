package com.example.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.sys.pojo.Role;
import com.example.sys.service.IRoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author skh
 * @since 2023-06-14
 */
@Api(tags = {"角色接口列表"})
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("/list")
    public Result<Map<String,Object>> getRoleList(@RequestParam(value = "roleName",required = false) String roleName,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName,roleName);

        Page<Role> page = new Page<>(pageNo,pageSize);
        roleService.page(page,lambdaQueryWrapper);

        HashMap<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("records",page.getRecords());

        return Result.success(data);
    }

    @PostMapping("/insert")
    public Result<?> addRole(@RequestBody Role role){
        roleService.addRole(role);
        return Result.success("添加角色成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteRoleById(@PathVariable("id") Integer id){
        roleService.removeById(id);
        return Result.success("删除角色成功");
    }

    @PutMapping("/update")
    public Result<?> updateRole(@RequestBody Role role){
        roleService.updateRole(role);
        return Result.success("角色修改成功");
    }

    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id){
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    @GetMapping("/all")
    public Result<List<Role>> getAllRole(){
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
    }

}
