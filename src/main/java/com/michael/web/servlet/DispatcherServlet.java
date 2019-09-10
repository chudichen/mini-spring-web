package com.michael.web.servlet;

import com.michael.web.handler.HandlerManager;
import com.michael.web.handler.MappingHandler;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 转发servlet，负责进行路径的匹配
 *
 * @author Michael Chu
 * @since 2019-09-09 17:29
 */
public class DispatcherServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        for(MappingHandler mappingHandler : HandlerManager.mappingHandlerList){
            try {
                if (mappingHandler.handle(servletRequest, servletResponse)){
                    return;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
