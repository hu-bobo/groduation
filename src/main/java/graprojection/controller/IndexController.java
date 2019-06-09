package graprojection.controller;

        //import io.swagger.annotations.Api;
        import lombok.extern.slf4j.Slf4j;
        //import org.apache.shiro.SecurityUtils;
        //import org.apache.shiro.subject.Subject;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title: IndexController
 */
@Controller
//("登录页面相关的api")
@Slf4j
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("login")
    public String tologin(){
        logger.info("定向登陆页");
        return "login";
    }

    @RequestMapping("home")
    public String home(){
        logger.info("定向主页");
        return "home";
    }

    @RequestMapping("register")
    public String register(){
        logger.info("定向注册页");
        return "register";
    }

//    @RequestMapping("logout")
//    public String logout(){
//        logger.info("退出系统");
//        Subject subject = SecurityUtils.getSubject();
//        subject.logout();
//        return "redirect:login";
//    }

}

