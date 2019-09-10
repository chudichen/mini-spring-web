package com.michael.service;

import com.michael.beans.Bean;

/**
 * service服务
 *
 * @author Michael Chu
 * @since 2019-09-09 17:44
 */
@Bean
public class HelloService {

    public String hello(String name) {
        return "hello " + name;
    }
}
