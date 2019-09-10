package com.michael.controller;

import com.michael.beans.AutoWired;
import com.michael.service.HelloService;
import com.michael.web.mvc.Controller;
import com.michael.web.mvc.RequestMapping;
import com.michael.web.mvc.RequestParam;

/**
 * controller服务
 *
 * @author Michael Chu
 * @since 2019-09-09 17:44
 */
@Controller
public class HelloController {

    @AutoWired
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello(@RequestParam("name") String name) {
        return helloService.hello(name);
    }
}
