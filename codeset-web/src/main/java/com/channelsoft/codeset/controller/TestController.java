package com.channelsoft.codeset.controller;

import com.channelsoft.codeset.json.JsonAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <dl>
 * <dt>CodeSet</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 北京青牛风科技有限公司</dd>
 * <dd>CreateDate: 2015年09月11日</dd>
 * </dl>
 *
 * @author LuoHui
 */
@Controller
@RequestMapping("/test")
public class TestController {


    @RequestMapping(value = "/getJson", method = RequestMethod.POST)
    public JsonAndView getResult(){
        JsonAndView jsonAndView = new JsonAndView();

        jsonAndView.addData("result", "");
        jsonAndView.addData("result1", "1");
        jsonAndView.addData("result2", "2");
        jsonAndView.addData("result3", null);

        return jsonAndView;
    }





}
