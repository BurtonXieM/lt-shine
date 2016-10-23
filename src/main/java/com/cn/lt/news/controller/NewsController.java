package com.cn.lt.news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2016/10/23.
 */
@Controller
@RequestMapping("news")
public class NewsController {

    @RequestMapping("index")
    @ResponseBody
    public String index(){

        return "hello" ;
    }
}
