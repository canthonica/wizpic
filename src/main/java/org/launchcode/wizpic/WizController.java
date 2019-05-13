package org.launchcode.wizpic;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WizController {

    @RequestMapping(value = "")
    @ResponseBody

    public String index() {


        return "Hello Wizpic";
    }
    @RequestMapping(value = "goodbye")
    @ResponseBody
    public String goodby() {
        return "Goodbye";


    }
}
