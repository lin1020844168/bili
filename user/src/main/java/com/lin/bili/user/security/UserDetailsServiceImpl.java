package com.lin.bili.user.security;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.bili.user.exception.client.PasswordErrorException;
import com.lin.bili.user.exception.client.UserNotFoundException;
import com.lin.bili.user.mapper.UserMapper;
import com.lin.bili.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

/**
 * 处理获取用户登录信息的逻辑类
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 通过用户名加载用户信息
     * @param account
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        User user = userMapper.selectOne(queryWrapper);
        if (!existUser(user)) {
            throw new UsernameNotFoundException("user is not found");
        }
        return new LoginUserDetails(user);
    }

    private boolean existUser(User user) {
        return user != null;
    }

    /**
     * 用户信息封装，供SpringSecurity验证使用
     */
    private class LoginUserDetails implements UserDetails {
        private User user;

        public LoginUserDetails(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getId().toString();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
