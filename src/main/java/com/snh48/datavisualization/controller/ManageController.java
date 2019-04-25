package com.snh48.datavisualization.controller;


import com.snh48.datavisualization.core.TaskSpider2;
import com.snh48.datavisualization.core.TaskSpider3;
import com.snh48.datavisualization.core.TotalRank;
import com.snh48.datavisualization.pojo.Member;
import com.snh48.datavisualization.pojo.UrlModel;
import com.snh48.datavisualization.service.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @description:
 * @author: chenpeng
 */
@Controller
@RequestMapping("/manage")
@CrossOrigin
public class ManageController {
    private static final Logger LOG = LoggerFactory.getLogger(ManageController.class);
    @Autowired
    private ManageService manageService;
    @Autowired
    private TaskSpider3 taskSpider3;
    @Autowired
    private TaskSpider2 taskSpider2;
    @Autowired
    private TotalRank totalRank;

    @RequestMapping("/index")
    public String toManage(Model model) throws Exception {
        List<Member> members = manageService.findMember();
        model.addAttribute("list", members);
        return "manage";
    }

    @RequestMapping("/update")
    public String updateMember(String id) throws Exception {
        manageService.updateMember(id);
        return "redirect:/manage/index";
    }

    @RequestMapping("/member")
    public String toUrl(Model model, String name) throws Exception {
        List<UrlModel> list = manageService.findUrlModel(name);
        model.addAttribute("list", list);
        return "url";
    }

    @RequestMapping("/updateUrl")
    public String updateUrl(String name, String id, String status, RedirectAttributes redirectAttributes) throws Exception {
        manageService.updateUrl(name, id, status);
        redirectAttributes.addAttribute("name", name);
        return "redirect:/manage/member";
    }

    @RequestMapping("/add")
    public String toAddUrl(Model model) throws Exception {
        List<Member> members = manageService.findMember();
        model.addAttribute("list", members);
        model.addAttribute("msg", "0");
        return "add";
    }

    @RequestMapping("/addUrl")
    public String addUrl(String url, String name, Model model) throws Exception {
        List<Member> members = manageService.findMember();
        LOG.info(name + "===========>" + url);
        try {
            manageService.addUrl(url, name);
            LOG.info(url + "添加成功!");
            return "redirect:/manage/index";
        } catch (Exception e) {
            LOG.error(url + "添加失败!");
            LOG.error(e.getMessage());
            model.addAttribute("list", members);
            model.addAttribute("msg", "1");
            return "add";
        }
    }

    @RequestMapping("/freshUpdate")
    @ResponseBody
    public String freshUpdate() throws Exception {
        try {
            taskSpider3.update();
            return "开始重新抓取！";
        } catch (Exception e) {
            return "重新抓取失败！";
        }
    }

    @RequestMapping("/freshByName")
    @ResponseBody
    public String freshByName(String name) throws Exception {
        try {
            taskSpider3.updateByName(name);
            return name + "的集资开始重新抓取！";
        } catch (Exception e) {
            return "重新抓取失败！";
        }
    }

    @RequestMapping("/freshUrl")
    @ResponseBody
    public String freshUrl() throws Exception {
        try {
            taskSpider2.updateUrlModel();
            return "开始重新抓取！";
        } catch (Exception e) {
            return "重新抓取失败！";
        }
    }

    @RequestMapping("/freshTotal")
    @ResponseBody
    public String freshTotal() throws Exception {
        try {
            totalRank.update();
            return "开始更新数据！";
        } catch (Exception e) {
            return "数据更新失败！";
        }
    }
}
