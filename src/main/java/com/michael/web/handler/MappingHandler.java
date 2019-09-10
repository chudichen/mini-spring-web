package com.michael.web.handler;

import com.michael.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 请求映射处理器
 *
 * @author Michael Chu
 * @since 2019-09-09 16:29
 */
public class MappingHandler {

    private String uri;
    private Method method;
    private Class<?> controller;
    private String[] args;

    MappingHandler(String uri, Method method, Class<?> cls, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = cls;
        this.args = args;
    }

    /**
     * 处理请求
     *
     * @param req
     * @param res
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws IOException
     */
    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InvocationTargetException, IOException {
        // 获取请求的uri
        String requestUri = ((HttpServletRequest) req).getRequestURI();
        if (!Objects.equals(requestUri, uri)) {
            return false;
        }
        Object[] parameters = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            parameters[i] = req.getParameter(args[i]);
        }
        Object ctl = BeanFactory.getBean(controller);
        Object response = method.invoke(ctl, parameters);
        res.getWriter().println(response.toString());
        return true;
    }

}
