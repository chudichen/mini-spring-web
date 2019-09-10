package com.michael;

import com.michael.starter.WebApplicationContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试服务启动类
 *
 * @author Michael Chu
 * @since 2019-09-09 17:44
 */
@Slf4j
public class App {

    public static void main(String[] args) {
        WebApplicationContext.run(App.class, args);
    }
}
