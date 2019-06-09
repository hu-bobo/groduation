package graprojection.controller;

import graprojection.jpa.model.JpaTestInfo;
import graprojection.service.JpaTestInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
public class JpaTestInfoController {
    @Autowired
    private JpaTestInfoService jpaTestInfoService;
//test项目初始运行
    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }


    //查询表测试
    @GetMapping("/test")//,produces = {"application/json;charset=UTF-8"}value =
    public void getAll(){

        List<JpaTestInfo> all = jpaTestInfoService.getAll();

        for (JpaTestInfo jpaTestInfo : all){
            System.out.println(jpaTestInfo);
            //return jpaTestInfo;
            //log.info();
        }
    }
}
