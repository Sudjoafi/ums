package com.example.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sys.mapper.RoleMenuMapper;
import com.example.sys.pojo.Role;
import com.example.sys.mapper.RoleMapper;
import com.example.sys.pojo.RoleMenu;
import com.example.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author skh
 * @since 2023-06-14
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public void addRole(Role role) {
        //写入角色表
        this.baseMapper.insert(role);

        //写入角色菜单关系表
        if (null != role.getMenuList()){
            for (Integer menuId : role.getMenuList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(),menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = this.baseMapper.selectById(id);
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByBoleId(id);
        role.setMenuList(menuIdList);
        return role;
    }

    @Override
    public void updateRole(Role role) {
        //更新role表
        this.baseMapper.updateById(role);
        //清除原有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,role.getRoleId());
        roleMenuMapper.delete(wrapper);
        //新增权限
        if (null != role.getMenuList()){
            for (Integer menuId : role.getMenuList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(),menuId));
            }
        }
    }
}
