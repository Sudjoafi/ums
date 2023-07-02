package com.example.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sys.pojo.Role;
import com.example.sys.pojo.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色菜单映射表 Mapper 接口
 * </p>
 *
 * @author skh
 * @since 2023-06-14
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    public List<Integer> getMenuIdListByBoleId(Integer roleId);

//    void delete(LambdaQueryWrapper<Role> wrapper);
}
