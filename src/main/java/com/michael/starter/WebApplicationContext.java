package com.michael.starter;

import com.michael.beans.BeanFactory;
import com.michael.core.ClassScanner;
import com.michael.web.handler.HandlerManager;
import com.michael.web.server.TomcatServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 应用启动类
 *
 * @author Michael Chu
 * @since 2019-09-09 16:21
 */
@Slf4j
public class WebApplicationContext {

    public static void run(Class<?> cls, String[] args) {
        TomcatServer tomcatServer = new TomcatServer(args);

        try {
            // 启动Tomcat
            tomcatServer.startServer();
            // 扫面启动类下所有的.class文件
            List<Class<?>> classList = ClassScanner.scanClass(cls.getPackage().getName());
            // 初始化bean工厂
            BeanFactory.initBean(classList);
            // 解析所有.class文件，获取mappingHandler集合
            HandlerManager.resolveMappingHandler(classList);
            // 启动成功
            log.info("Application start successful");
        } catch (Exception e) {
            log.error("Start failed", e);
        }
    }
}
