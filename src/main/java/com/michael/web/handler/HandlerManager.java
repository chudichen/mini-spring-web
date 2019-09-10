package com.michael.web.handler;


import com.michael.web.mvc.Controller;
import com.michael.web.mvc.RequestMapping;
import com.michael.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理器管理类
 *
 * @author Michael Chu
 * @since 2019-09-09 16:25
 */
public class HandlerManager {

    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    public static void resolveMappingHandler(List<Class<?>> classList) {
        classList.stream()
                .filter(HandlerManager::isController)
                .forEach(HandlerManager::parseHandlerFromController);
    }

    private static boolean isController(Class cls) {
       return cls.isAnnotationPresent(Controller.class);
    }

    private static void parseHandlerFromController(Class<?> cls) {
        // 先获取该controller中所有的方法
        Method[] methods = cls.getDeclaredMethods();
        // 从中挑选出被RequestMapping注解的方法进行封装
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            // 拿到RequestMapping定义的uri
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            // 保存方法参数的集合
            List<String> paramNameList = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                // 把有被RequestParam注解的参数添加入集合
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            // 把参数集合转为数组，用于反射
            String[] params = paramNameList.toArray(new String[0]);
            // 反射生成MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);
            // 把mappingHandler装入集合中
            mappingHandlerList.add(mappingHandler);
        }
    }
}
