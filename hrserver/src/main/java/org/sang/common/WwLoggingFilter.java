package org.sang.common;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
/**
 * 日志格式过滤器
 * 日志添加用户名session等通用信息,方便定位问题
 * @author: lizhizhong
 * CreatedDate: 2018/11/30.
 */
@Slf4j
public class WwLoggingFilter implements Filter {

    private static final String USER_ID = "userId";
    private static final String SESSION_ID = "sessionId";
    private static final String ANONYMOUS = "未設定";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // NO-OP

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        Principal auth = req.getUserPrincipal();
        String userId = "";
        if (auth != null) {
            userId = auth.getName();
        } else {
            userId = ANONYMOUS;
        }
        log.info("========userId is{}",userId);
        MDC.put(USER_ID, userId);

        HttpSession session = req.getSession(false);
        if (session != null) {
            MDC.put(SESSION_ID, session.getId());
        }

        try {
            chain.doFilter(request, response);
        } finally {

            MDC.remove(USER_ID);
            MDC.remove(SESSION_ID);
        }

    }

    @Override
    public void destroy() {
        // NO-OP

    }

}
