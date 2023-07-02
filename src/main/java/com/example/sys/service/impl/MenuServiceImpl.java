package com.example.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sys.pojo.Menu;
import com.example.sys.mapper.MenuMapper;
import com.example.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author skh
 * @since 2023-06-14
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> getAllMenu() {
        //查询一级菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, 0);
        List<Menu> menuList = this.list(wrapper);
        //查询子菜单
//        if (menuList != null){
//            for (Menu menu : menuList){
//                LambdaQueryWrapper<Menu> wrapper1 = new LambdaQueryWrapper<>();
//                wrapper1.eq(Menu::getParentId,menu.getMenuId());
//                List<Menu> parentMenuList = this.list(wrapper1);
//                menu.setChildren(parentMenuList);
//            }
//        }
        setMenuChildren(menuList);
        return menuList;
    }

    private void setMenuChildren(List<Menu> menuList) {
        if (menuList != null){
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> parentWrapper = new LambdaQueryWrapper<>();
                parentWrapper.eq(Menu::getParentId,menu.getMenuId());
                List<Menu> parentMenuList = this.list(parentWrapper);
                menu.setChildren(parentMenuList);
                //递归(后续有多级菜单均可查询)  缺点：数据多时效率低
                setMenuChildren(parentMenuList);
            }
        }
    }

    @Override
    public List<Menu> getMenuListByUserId(Integer userId) {
        //一级菜单
        List<Menu> menuList = this.baseMapper.getMenuListByUserId(userId, 0);
        //子菜单
        setMenuChildrenByUserId(userId, menuList);
        return menuList;
    }

    private void setMenuChildrenByUserId(Integer userId, List<Menu> menuList) {
        if (menuList != null){
            for (Menu menu : menuList) {
                List<Menu> subMenuList = this.baseMapper.getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(subMenuList);
                //递归
                setMenuChildrenByUserId(userId,subMenuList);
            }
        }
    }

}
