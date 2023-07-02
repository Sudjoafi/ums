package com.example.sys.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author skh
 * @since 2023-06-14
 */

@Data
@TableName("u_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

//    @TableField(select = false)
    private String password;

    private String email;

    private String phone;

    private Integer status;

    private String avatar;

//    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private List<Integer> roleIdList;


}
