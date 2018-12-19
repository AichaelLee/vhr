package net.cnki.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cnki.bean.Managers;
import net.cnki.bean.RespBean;
import net.cnki.bean.TblStudentBase;
import net.cnki.bean.TblTeacherBase;
import net.cnki.common.CustomUserTypeAuthenticationFilter;
import net.cnki.service.SecurityUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * spring security 配置类
 * @author: lizhizhong
 * CreatedDate: 2018/11/26.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    SecurityUserDetailService hrService;
    @Autowired
    CustomMetadataSource metadataSource;
    @Autowired
    UrlAccessDecisionManager urlAccessDecisionManager;
    @Autowired
    AuthenticationAccessDeniedHandler deniedHandler;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SimpleAuthorityMapper simpleAuthorityMapper;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

   // @Autowired
    //HrService hrService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.html", "/static/**", "/login_p");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
        .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setSecurityMetadataSource(metadataSource);
                o.setAccessDecisionManager(urlAccessDecisionManager);
                return o;
            }
        })
        .and()
        .formLogin().loginPage("/login_p").loginProcessingUrl("/login")
        .usernameParameter("username").passwordParameter("password")
        .failureHandler(new CustomAuthenticationFailureHandler())
        .successHandler(new CustomAuthenticationSuccessHandler())
        .permitAll()
        .and()
        .logout().permitAll()
        .and().csrf().disable()
        .exceptionHandling().accessDeniedHandler(deniedHandler);
    }



    /**
     * 自定义用户类型验证过滤器,增加userType字段
     * @return
     * @throws Exception
     */
    public CustomUserTypeAuthenticationFilter authenticationFilter() throws Exception {
        CustomUserTypeAuthenticationFilter filter = new CustomUserTypeAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        return filter;
    }


    /**
     * 自定义验证失败处理器
     */
    private class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

        @Override
        public void onAuthenticationSuccess(HttpServletRequest req,
                                            HttpServletResponse resp,
                                            Authentication auth) throws IOException {
            resp.setContentType("application/json;charset=utf-8");

            RespBean respBean = null;

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof Managers){
                respBean =RespBean.ok("登录成功!",  ((Managers) principal));
            }else if(principal instanceof TblTeacherBase){
                respBean =RespBean.ok("登录成功!",  ((TblTeacherBase) principal));
            }else{
                respBean =RespBean.ok("登录成功!",  ((TblStudentBase) principal));
            }
            ObjectMapper om = new ObjectMapper();
            PrintWriter out = resp.getWriter();
            out.write(om.writeValueAsString(respBean));
            out.flush();
            out.close();
        }
    }

    /**
     * 自定义验证失败处理器
     *
     * **/
    private class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{


        @Override
        public void onAuthenticationFailure(HttpServletRequest req,
                                            HttpServletResponse resp,
                                            AuthenticationException e) throws IOException {
            resp.setContentType("application/json;charset=utf-8");
            RespBean respBean = null;
            if (e instanceof BadCredentialsException ||
                    e instanceof UsernameNotFoundException) {
                respBean = RespBean.error("账户名或者密码输入错误!");
            } else if (e instanceof LockedException) {
                respBean = RespBean.error("账户被锁定，请联系管理员!");
            } else if (e instanceof CredentialsExpiredException) {
                respBean = RespBean.error("密码过期，请联系管理员!");
            } else if (e instanceof AccountExpiredException) {
                respBean = RespBean.error("账户过期，请联系管理员!");
            } else if (e instanceof DisabledException) {
                respBean = RespBean.error("账户被禁用，请联系管理员!");
            } else {
                respBean = RespBean.error("登录失败!");
            }
            resp.setStatus(401);
            ObjectMapper om = new ObjectMapper();
            PrintWriter out = resp.getWriter();
            out.write(om.writeValueAsString(respBean));
            out.flush();
            out.close();
        }

    }
}