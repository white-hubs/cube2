package com.guhecloud.platform.cloud.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author appet
 * @className ChannelFilter
 * @description: Channel过滤器，用于载入对自定义Request装饰类并放行
 * @date 2023/7/26 16:23
 **/
@Component
@WebFilter(filterName = "channelFilter", urlPatterns = { "/*" })
public class ChannelFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /* 不重写 */
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            ServletRequest requestWrapper = null;
            /* 判断当前请求是否属于Http类型,是的话创建自定义Request装饰类 */
            if (request instanceof HttpServletRequest) {
                requestWrapper = new RepeatableRequestWrapper((HttpServletRequest) request);
            }

            /* 自定义Request装饰类对象不为空时放行传给下一个过滤器或者直达Servlet,为空时传入默认Request对象 */
            if (null == requestWrapper) {
                chain.doFilter(request, response);
            } else {
                chain.doFilter(requestWrapper, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        /* 不重写 */
    }
}


