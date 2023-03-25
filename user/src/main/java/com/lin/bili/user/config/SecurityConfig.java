package com.lin.bili.user.config;

import com.lin.bili.user.security.filter.JwtAuthenticationFilter;
import com.lin.bili.user.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


/**
 * springSecurity配置
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 密码加密
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 获取更新用户信息
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * 配置安全相关信息
     * （禁用了登录相关的配置，自己实现了登录逻辑）
     * （禁用了session相关配置，使用jwt验证用户状态）
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin().disable();
        http.httpBasic().disable();
        http.sessionManagement().disable();

        http.authorizeRequests().antMatchers("/user/login").anonymous();
        http.authorizeRequests().antMatchers("/user/exist/**").permitAll();
        http.authorizeRequests().antMatchers("/user/send/**").permitAll();
            http.authorizeRequests().antMatchers("/user/register").permitAll();
        http.authorizeRequests().antMatchers("/user/checkToken/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();
        http.authorizeRequests().anyRequest().authenticated();

        http.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
    }
}
