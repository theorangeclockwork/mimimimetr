package inovus.task.mimimimetr.controller;

import inovus.task.mimimimetr.form.ContendersDto;
import inovus.task.mimimimetr.model.Contender;
import inovus.task.mimimimetr.service.ContenderService;
import inovus.task.mimimimetr.util.CookiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RankController {

    private final ContenderService contenderService;

    @Autowired
    public RankController(ContenderService contenderService) {
        this.contenderService = contenderService;
    }

    @GetMapping("/ranking")
    public String showVoting(@CookieValue(value = "usedIds", defaultValue = "") String usedIds,
                             Model model,
                             HttpServletResponse response) {
        List<String> list = new ArrayList<>(Arrays.asList(usedIds.split("\\s*,\\s*")));
        Set<Long> set = list.stream().map(Long::valueOf).collect(Collectors.toSet());
        List<Contender> contenderPair = contenderService.getTwoRandomContendersNotUsedBefore(set);
        if (contenderPair.isEmpty()) {
            return "redirect:/ranking/result";
        }
        String cookedIds = CookiesUtil.updateCookies(usedIds, contenderPair);
        response.addCookie(new Cookie("usedIds", cookedIds));
        model.addAttribute("cookie", usedIds);
        model.addAttribute("contenders", new ContendersDto(contenderPair));
        return "vote";
    }

    @PostMapping("/ranking")
    public String processVote(@ModelAttribute Contender contender) {
        contender.setScore((contender.getScore() + 1));
        contenderService.save(contender);
        return "redirect:/ranking";
    }

    @GetMapping("/ranking/result")
    public String showResult(Model model) {
        List<Contender> top10ByScore = contenderService.getTop10ByScore();
        model.addAttribute("contenders", top10ByScore);
        return "result";
    }

    @GetMapping("/contenders/add")
    public String addContenders(Model model) {
        ContendersDto contendersDto = new ContendersDto();

        for (int i = 0; i < 10; i++) {
            contendersDto.addContender(new Contender());
        }

        model.addAttribute("contenders", contendersDto);
        return "addContenders";
    }

    @PostMapping("/contenders/add")
    public String processContenders(@ModelAttribute ContendersDto contendersDto, Model model) {
        contenderService.saveAll(contendersDto.getContenderList());
        return "redirect:/contenders/all";
    }

    @GetMapping("/contenders/all")
    public String showAll(Model model) {
        model.addAttribute("contenders", contenderService.getAll());
        return "allContenders";
    }

}
