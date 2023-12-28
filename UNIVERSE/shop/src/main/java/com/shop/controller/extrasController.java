package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/extras")
public class extrasController {

    @GetMapping(value = "/map")
    public String maps(){
        return "extras/map";
    }
}
