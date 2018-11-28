package org.sang.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sang on 2017/12/29.
 */
@RestController
@RequestMapping("/user")
public class UserlController {
    @RequestMapping("/info")
    public List<String> hello() {
        return Arrays.asList("院长","指导老师");
    }
}
