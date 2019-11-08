package inovus.task.mimimimetr.controller;

import inovus.task.mimimimetr.form.ContendersDto;
import inovus.task.mimimimetr.model.Contender;
import inovus.task.mimimimetr.service.ContenderService;
import inovus.task.mimimimetr.util.CookiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class RankController {
    private final ContenderService contenderService;

    @Autowired
    public RankController(ContenderService contenderService) {
        this.contenderService = contenderService;
    }

    @GetMapping("/ranking")
    public String showVoting(
            @CookieValue(value = "usedIds", defaultValue = "") String usedIds,
            Model model,
            HttpServletResponse response
    ) {
        List<Contender> contenderPair;
        if (!usedIds.isEmpty()) {
            Set<Long> set = Arrays.stream(usedIds.split("\\.")).filter(s -> s.length() > 0).map(Long::valueOf).collect(Collectors.toSet());
            contenderPair = contenderService.getTwoRandomContendersNotUsedBefore(set);
        } else {
            contenderPair = contenderService.getTwoRandomContenders();
        }

        if (contenderPair.isEmpty()) {
            return "redirect:/ranking/result";
        }

        String cookedIds = CookiesUtil.updateCookies(usedIds, contenderPair);
        response.addCookie(new Cookie("pendingIds", cookedIds));
        model.addAttribute("cookie", cookedIds);
        model.addAttribute("contenderList", contenderPair);
        return "vote";
    }

    @PostMapping("/ranking")
    public String processVote(@ModelAttribute("contenderId") Long id,
                              HttpServletResponse response,
                              @CookieValue(value = "pendingIds", defaultValue = "") String pendingIds) {
        response.addCookie(new Cookie("usedIds", pendingIds));
        contenderService.updateContenderIncrementScoreBy1(id);
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
        List<Contender> contenders = IntStream.range(0, 10)
                .boxed()
                .map(e -> new Contender())
                .collect(Collectors.toList());
        ContendersDto contendersDto = new ContendersDto(contenders);
        model.addAttribute("contenders", contendersDto);
        return "addContenders";
    }

    @PostMapping("/contenders/add")
    public String processContenders(@ModelAttribute ContendersDto contendersDto,
                                    @RequestParam("images") MultipartFile[] files) {
        List<Contender> contenders = IntStream.range(0, contendersDto.getContenderList().size()).boxed()
                .map(i -> {
                    try {
                        return contendersDto.getContenderList().get(i).setImageData(files[i].getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(contender -> contender.getName().length() > 0)
                .collect(Collectors.toList());
        contenderService.saveAll(contenders);
        return "redirect:/contenders/all";
    }

    @GetMapping("/contenders/all")
    public String showAll(Model model) {
        model.addAttribute("contenders", contenderService.getAll());
        return "allContenders";
    }

}
