package graprojection.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class PageController {
    public final static String PROJECT_NAME_PAGE = "";
    @GetMapping("/point/pointForm")
    public String pointForm() {
        //return "page/pointForm";
        return PROJECT_NAME_PAGE + "page/pointForm";
    }
}
