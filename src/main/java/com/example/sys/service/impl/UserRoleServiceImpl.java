package com.example.sys.service.impl;

import com.example.sys.pojo.UserRole;
import com.example.sys.mapper.UserRoleMapper;
import com.example.sys.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色映射表 服务实现类
 * </p>
 *
 * @author skh
 * @since 2023-06-14
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
