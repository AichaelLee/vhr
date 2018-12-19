package net.cnki.controller;

import net.cnki.bean.RespBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lizhizhong on 2018/11/29.
 */
@RestController
public class RegLoginController {
    @RequestMapping("/login_p")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public RespBean login() {
        return RespBean.error("尚未登录，请登录!");
    }

}
