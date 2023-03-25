package com.lin.bili.user.security.filter;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.lin.bili.common.constant.JWTConstant;
import com.lin.bili.common.constant.RedisConstant;
import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.common.utils.TokenUtils;
import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.lin.bili.common.constant.JWTConstant.*;
import static com.lin.bili.common.constant.RedisConstant.USERDATA_PREFIX;

/**
 * 基于jwt进行授权认证的过滤器
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(JWTConstant.USERDATA_HEAD);
        if (token == null || token.equals("")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!TokenUtils.checkToken(token)) {
            responseErr(response,"JWT已过期或不符合规范");
            return;
        }
        JSONObject payloads = JWT.of(token).getPayloads();
        String userdataKey = (String) payloads.get(PAYLOAD_USERDATA);
        if (!redisUtils.hasKey(userdataKey)) {
            responseErr(response,"登录信息已过期");
            return;
        }
        String userDataJson = redisUtils.get(userdataKey);
        JSONObject userData = JSONUtil.parseObj(userDataJson);
        String userid = userData.get(USERDATA_USERID).toString();
        List<String> authoritiesString = (List<String>) userData.get(JWTConstant.USERDATA_AUTHORITIES);
        List<SimpleGrantedAuthority> authorities = authoritiesString.stream().map(e -> new SimpleGrantedAuthority(e)).collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userid, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private void responseErr(HttpServletResponse response, String message) throws IOException {
        ResponseResult<String> res = ResponseResult.failure(message);
        response.setContentType(ContentType.JSON.toString());
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSONUtil.parse(res));
        writer.flush();
    }
}
