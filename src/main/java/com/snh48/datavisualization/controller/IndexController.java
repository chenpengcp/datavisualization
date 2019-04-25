package com.snh48.datavisualization.controller;

import com.alibaba.fastjson.JSON;
import com.snh48.datavisualization.pojo.GrSearch;
import com.snh48.datavisualization.pojo.Member;
import com.snh48.datavisualization.pojo.MemberInfo;
import com.snh48.datavisualization.pojo.Pkmodel;
import com.snh48.datavisualization.service.DataService;
import com.snh48.datavisualization.utils.PYUtils;
import com.snh48.datavisualization.utils.ScalaUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/snh48")
@CrossOrigin
public class IndexController {
    @Autowired
    private DataService dataService;

    @RequestMapping("/toTrend")
    public String toTrend(Model model, String name) throws Exception {
        model.addAttribute("name", name);
        return "trend";
    }

    @RequestMapping("/login")
    public String login(Model model) throws Exception {
        model.addAttribute("msg", "0");
        return "login";
    }

    @RequestMapping("/permission")
    public String permission() throws Exception {
        return "permission";
    }

    @RequestMapping("/toLogin")
    public String toLogin(String username, String password, Model model) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (StringUtils.isEmpty(username)) {
            model.addAttribute("msg", "3");
            return "login";
        } else if (StringUtils.isEmpty(password)) {
            model.addAttribute("msg", "4");
            return "login";
        } else {
            UsernamePasswordToken userToken = new UsernamePasswordToken(username, password);
            try {
                subject.login(userToken);
                return "redirect:/snh48/index";
            } catch (UnknownAccountException e) {
                model.addAttribute("msg", "1");
//            model.addAttribute("msg", "用户名不存在");
                return "login";
            } catch (IncorrectCredentialsException e) {
                model.addAttribute("msg", "2");
//            model.addAttribute("msg", "密码错误");
                return "login";
            }
        }
    }

    @RequestMapping("/card")
    public String toCard(Model model) throws Exception {
        List<Member> members = dataService.findMember();
        model.addAttribute("list", members);
        model.addAttribute("count", members.size());
        return "card";
    }

    @RequestMapping("/analysis")
    public String toAnalysis(Model model) throws Exception {
        String allMoney = dataService.getAllMoney();
        String totalCount = dataService.getTotalCount();
        model.addAttribute("allMoney", "全团集资总金额：" + allMoney);
        model.addAttribute("totalCount", "全团集资总人数：" + totalCount);
        model.addAttribute("wid", (Float.parseFloat(allMoney) * 100 / 10000000) + "%");
        return "analysis";
    }

    @RequestMapping("/rank")
    public String toRank(Model model) throws Exception {
        Map<String, String> map = dataService.getRichCount();
        model.addAttribute("wcount", map.get("wcount"));
        model.addAttribute("kcount", map.get("kcount"));
        model.addAttribute("kkcount", map.get("kkcount"));
        return "rank";
    }

    @RequestMapping("/user")
    public String toUser(String name, Model model) throws Exception {
        List<GrSearch> list = dataService.findSearch(name);
        Float total = 0f;
        Integer count = 0;
        for (GrSearch gr : list
                ) {
            total += Float.parseFloat(gr.getMoney());
            count += Integer.parseInt(gr.getCount());
        }
        model.addAttribute("list", list);
        model.addAttribute("listl", JSON.toJSONString(list));
        model.addAttribute("name", name);
        model.addAttribute("total", ScalaUtils.scala(total));
        model.addAttribute("count", count);
        return "user";
    }

    @RequestMapping("/all")
    public String toAll() throws Exception {
        return "all";
    }

    @RequestMapping("/index")
    public String toMain(HttpServletRequest request, Model model) throws Exception {
        setPic(model);
        String remoteHost = getRemoteHost(request);
        dataService.record(remoteHost);
        return "index";
    }

    @RequestMapping("/recentPK")
    public String toIndex(Model model) throws Exception {
        List<Pkmodel> list = dataService.findPkName();
//        for (Pkmodel p:list
//             ) {
//            if ("0".equals(p.getStatus())) {
//                p.setName(p.getName() + " (已结束)");
//            } else {
//                p.setName(p.getName() + " (进行中)");
//            }
//        }
        model.addAttribute("list", list);
        model.addAttribute("pid", list.get(0).getId());
        return "pk";
    }

    @RequestMapping("/pk")
    @ResponseBody
    public String pk(String id) throws Exception {
        String s = JSON.toJSONString(dataService.findAll(id));
        //System.out.println(s);
        return s;
    }

    @RequestMapping("/result")
    @ResponseBody
    public String result(String name, String id) throws Exception {
        String s = JSON.toJSONString(dataService.resultPk(name, id));
        return s;
    }

    @RequestMapping("/trend")
    @ResponseBody
    public String trend(String name) throws Exception {
        String s = JSON.toJSONString(dataService.trend(name));
        return s;
    }


    @RequestMapping("/total")
    public String toTotal(HttpServletRequest request, Model model) throws Exception {
        setPic(model);
        String remoteHost = getRemoteHost(request);
        dataService.record(remoteHost);
        return "index";
    }

    @RequestMapping("/go")
    @ResponseBody
    public String go() throws Exception {
        String s = JSON.toJSONString(dataService.findTotal());
        return s;
    }

    @RequestMapping("/add")
    @ResponseBody
    public String add() throws Exception {
        String s = JSON.toJSONString(dataService.findAdd());
        return s;
    }

    @RequestMapping("/resultAll")
    @ResponseBody
    public String resultAll(String name) throws Exception {
        String s = JSON.toJSONString(dataService.result(PYUtils.getTotal(name)));
        return s;
    }

    @RequestMapping("/search")
    @ResponseBody
    public String toSearch(String name) throws Exception {
        return JSON.toJSONString(dataService.findSearch(name));
    }

    @RequestMapping("/rankResult")
    @ResponseBody
    public String findMax() throws Exception {
        return dataService.findMax();
    }

    @RequestMapping("/analysisResult")
    @ResponseBody
    public String findAnalysis(String id) throws Exception {
        return dataService.findAnalysis(id);
    }

    @RequestMapping("/model")
    public String findModel(String name, Model model) throws Exception {
        MemberInfo memberInfo = dataService.resultCount(name);
        model.addAttribute("m", memberInfo);
        return "info";
    }

    public String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    public void setPic(Model model) {
        String[] ss = {"n1.JPG", "n2.JPG", "n3.jpg"};
        Random random = new Random();
        int i = random.nextInt(3);
        model.addAttribute("pic", ss[i]);
    }
}
