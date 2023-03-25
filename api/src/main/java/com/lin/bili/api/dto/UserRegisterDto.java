package com.lin.bili.api.dto;

import com.lin.bili.common.validation.annotation.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto implements Serializable {
    /**
     * 用户名
     */
    @Size(min = 3, max = 20, message = "长度必须为3到20")
    private String name;

    /**
     * 账户名
     */
    @Size(min = 6, max = 20, message = "长度必须为6到20")
    private String account;

    /**
     * 密码
     */
    @Size(min = 6, max = 20, message = "长度必须为6到20")
    private String password;

    /**
     * 手机号码
     */
    @Phone
    private String phoneNumber;

    /**
     * 验证码
     */
    @Size(min = 6, max = 6, message = "验证码长度不合格")
    private String smsCode;
}
