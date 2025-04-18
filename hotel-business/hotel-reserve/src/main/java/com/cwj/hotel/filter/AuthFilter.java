package com.cwj.hotel.filter;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.cwj.hotel.entity.User;
import com.cwj.hotel.utils.HutoolJWTUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter implements Filter {
    private static final int ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000; // 15 minutes
//    private static final int REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000; // 1 days

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("enter filter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String accessToken = httpRequest.getHeader("Authorization");
        String refreshToken = httpRequest.getHeader("RefreshToken");

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
            if (HutoolJWTUtil.validateToken(accessToken)) {
                System.out.println("token pass");
                chain.doFilter(request, response);
                return;
            }
        }

        if (refreshToken != null) {
            if (HutoolJWTUtil.validateRefreshToken(refreshToken)) {
                Long userId = Long.parseLong(HutoolJWTUtil.parseToken(refreshToken));
                User user = new User();
                user.setId(userId);
                String newAccessToken = HutoolJWTUtil.createToken(user, ACCESS_TOKEN_EXPIRATION);
                httpResponse.setHeader("Authorization", "Bearer " + newAccessToken);
                chain.doFilter(request, response);
                System.out.println("refresh token pass");
                return;
            }
        }
        System.out.println("pass fail");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化逻辑
    }

    @Override
    public void destroy() {
        // 销毁逻辑
    }
}