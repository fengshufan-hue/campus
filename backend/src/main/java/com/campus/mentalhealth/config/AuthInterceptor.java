package com.campus.mentalhealth.config;

import com.campus.mentalhealth.common.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // Allow login endpoint without auth
        String uri = request.getRequestURI();
        if (uri.equals("/api/login")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
            return false;
        }

        String token = authHeader.substring(7);
        Long userId = JwtUtil.getUserId(token);
        String role = JwtUtil.getRole(token);

        if (userId == null || role == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"登录已过期，请重新登录\"}");
            return false;
        }

        request.setAttribute("userId", userId);
        request.setAttribute("role", role);
        return true;
    }
}
