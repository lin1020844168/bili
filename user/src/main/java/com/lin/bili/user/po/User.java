package com.lin.bili.user.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_info")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId
    private Long id;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机
     */
    private String phoneNumber;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private Boolean sex;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 用户头像
     */
    private String image;
    /**
     * vip等级
     */
    private Integer vipLevel;
    /**
     * 普通等级
     */
    private Integer simpleLevel;
    /**
     * 粉丝数
     */
    private Integer fansCnt;
    /**
     * 关注数
     */
    private Integer concernCnt;
    /**
     * 获赞数
     */
    private Integer acquiredGoodCnt;
    /**
     * 硬币数
     */
    private Integer coinCnt;
    /**
     * 个性签名
     */
    private String styleSignature;
    /**
     * 出生年月
     */
    private Date birthday;
    /**
     * 所在地
     */
    private String home;
    /**
     * gmt_modified
     */
    private Date gmtModified;
    /**
     * gmt_create
     */
    private Date gmtCreate;

}
