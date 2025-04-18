package com.cwj.hotel.filter;


import com.cwj.hotel.entity.User;
import com.cwj.hotel.utils.HutoolJWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        String refreshToken = request.getHeader("refreshToken");
        if (token != null && refreshToken != null) {
            if(HutoolJWTUtil.validateToken(token)){
                filterChain.doFilter(request, response);
            }else {
                if (HutoolJWTUtil.validateRefreshToken(refreshToken)){
                    Long userid = Long.parseLong(HutoolJWTUtil.parseToken(refreshToken));
                    User user=new User();
                    user.setId(userid);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"token\": \"" + HutoolJWTUtil.createToken(user,30*60) + "\"}");
                }
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }
}
