package com.michael.beans;

import com.michael.web.mvc.Controller;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化bean，以及获取bean
 *
 * @author Michael Chu
 * @since 2019-09-09 15:03
 */
@Slf4j
public class BeanFactory {

    /** 保存Bean实例的映射集合 */
    private static Map<Class<?>, Object> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * 根据class类型获取bean
     *
     * @param cls 类型
     * @return bean
     */
    public static Object getBean(Class<?> cls) {
        return beanDefinitionMap.get(cls);
    }

    /**
     * 将class进行初始化
     *
     * @param classList
     * @throws Exception
     */
    public static void initBean(List<Class<?>> classList) throws Exception {
        // 先创建一个.class文件集合的副本
        List<Class<?>> toCreate = new ArrayList<>(classList);
        while (toCreate.size() != 0) {
            // 记录开始时集合大小，如果一轮结束后大小没变证明有相互依赖
            int remainSize = toCreate.size();
            toCreate.removeIf(BeanFactory::doCreateBeanDefinition);
            if (toCreate.size() == remainSize) {
                throw new Exception("Cycle dependency!");
            }
        }
    }

    /**
     * 创建bean，{@code true}表示完成
     *
     * @param cls 类型
     * @return 是否成功
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static boolean doCreateBeanDefinition(Class<?> cls) {
        // 创建的bean实例仅包括Bean和Controller注释的类
        if (!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)) {
            return true;
        }

        // 先创建实例对象
        try {
            Object bean = cls.getDeclaredConstructor().newInstance();
            // 看看实例对象是否需要执行依赖注入，注入其他bean
            for (Field field : cls.getDeclaredFields()) {
                if (field.isAnnotationPresent(AutoWired.class)) {
                    Class<?> fieldType = field.getType();
                    Object reliantBean = BeanFactory.getBean(fieldType);
                    // 如果要注入的bean还未被创建就先跳过
                    if (reliantBean == null) {
                        return false;
                    }
                    field.setAccessible(true);
                    field.set(bean, reliantBean);
                }
            }
            beanDefinitionMap.put(cls, bean);
            return true;
        } catch (Exception e) {
            log.error("New instance failed", e);
        }
        return false;
    }
}
