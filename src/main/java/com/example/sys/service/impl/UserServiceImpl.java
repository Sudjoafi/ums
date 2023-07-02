package com.example.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sys.mapper.UserRoleMapper;
import com.example.sys.pojo.Menu;
import com.example.sys.pojo.User;
import com.example.sys.mapper.UserMapper;
import com.example.sys.pojo.UserRole;
import com.example.sys.service.IMenuService;
import com.example.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.JwtUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author skh
 * @since 2023-06-14
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IMenuService menuService;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public Map<String, Object> login(User user) {

        //根据用户名查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);

        //结果不为空，且密码与传入密码匹配，则生成token
        if (loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())){
            //一般用jwt，这里暂用uuid
//            String key = "user:" + UUID.randomUUID();
            //存入redis
//            loginUser.setPassword(null);
//            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);//登陆缓存时效

            loginUser.setPassword(null);
            String jwt = jwtUtil.createToken(loginUser);

            //返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token",jwt);
            return data;
        }
        return null;

//        //根据用户名和密码查询
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername,user.getUsername())
//                .eq(User::getPassword,user.getPassword());
//        User loginUser = this.baseMapper.selectOne(wrapper);
//
//        //结果不为空，则生成token，并将用户信息存入redis
//        if (loginUser != null){
//            //一般用jwt，这里暂用uuid
//            String key = "user:" + UUID.randomUUID();
//
//            //存入redis
//            loginUser.setPassword(null);
//            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);//登陆缓存时效
//
//            //返回数据
//            HashMap<String, Object> data = new HashMap<>();
//            data.put("token",key);
//            return data;
//        }
//        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
//        Object obj = redisTemplate.opsForValue().get(token);
        //解析jwt
        User loginUser = null;
        try {
            loginUser = jwtUtil.parseToken(token,User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (loginUser != null){
//            User loginUser = JSON.parseObject(JSON.toJSONString(obj),User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar",loginUser.getAvatar());

            //获取角色信息
            List<String> roleList = this.baseMapper.getRoleNameByUserId(loginUser.getId());
            data.put("roles",roleList);

            //权限菜单列表
            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList",menuList);

            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
//        redisTemplate.delete(token);
    }

    @Override
    public void addUser(User user) {
        //写入用户表
        this.baseMapper.insert(user);
        //写入用户角色表
        List<Integer> roleIdList = user.getRoleIdList();
        if(roleIdList != null){
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId));
            }
        }
    }

    @Override
    public User getUserById(Integer id) {
        User user = this.baseMapper.selectById(id);
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId,id);
        List<UserRole> userRolesList = userRoleMapper.selectList(wrapper);

        List<Integer> RoleIdList = userRolesList.stream()
                .map(userRole -> {return userRole.getRoleId();})
                .collect(Collectors.toList());

        user.setRoleIdList(RoleIdList);

        return user;
    }

    @Override
    public void updateUser(User user) {
        //更新用户表
        this.baseMapper.updateById(user);
        //清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId,user.getId());
        userRoleMapper.delete(wrapper);
        //设置新角色
        List<Integer> roleIdList = user.getRoleIdList();
        if(roleIdList != null){
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId));
            }
        }
    }

    @Override
    public void deleteUserById(Integer id) {
        this.baseMapper.deleteById(id);
        //清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId,id);
        userRoleMapper.delete(wrapper);
    }
}
