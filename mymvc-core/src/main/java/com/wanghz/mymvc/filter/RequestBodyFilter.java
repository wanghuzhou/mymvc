package com.wanghz.mymvc.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestBodyFilter implements Filter {
    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (((HttpServletRequest) request).getHeader("Content-Type").contains("form")) {
            chain.doFilter(request, response);
        } else {
            requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
            chain.doFilter(requestWrapper, response);
        }
    }

    /**
     * 初始化函数时，需要获取排除在外的url
     */
    public void init(FilterConfig config) throws ServletException {

    }
}
