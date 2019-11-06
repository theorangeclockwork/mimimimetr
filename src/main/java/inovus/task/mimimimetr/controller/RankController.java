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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RankController {

    private final ContenderService contenderService;
    private final static String uploadDirectory = "C:\\Users\\Cifer\\IdeaProjects\\mimimimetr\\src\\main\\resources\\image";

    @Autowired
    public RankController(ContenderService contenderService) {
        this.contenderService = contenderService;
    }

    @GetMapping("/ranking")
    public String showVoting(@CookieValue(value = "usedIds", defaultValue = "") String usedIds,
                             Model model,
                             HttpServletResponse response) {
        List<String> list = new ArrayList<>(Arrays.asList(usedIds.split("\\.")));
        Set<Long> set;
        List<Contender> contenderPair;

        if (!usedIds.isEmpty()) {
            set = list.stream().filter(s -> s.length() > 0).map(Long::valueOf).collect(Collectors.toSet());
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
        ContendersDto contendersDto = new ContendersDto();

        for (int i = 0; i < 10; i++) {
            contendersDto.addContender(new Contender());
        }

        model.addAttribute("contenders", contendersDto);
        return "addContenders";
    }

    @PostMapping("/contenders/add")
    public String processContenders(@ModelAttribute ContendersDto contendersDto,
                                    @RequestParam("images") MultipartFile[] files) throws IOException {

        for (int i = 0; i < contendersDto.getContenderList().size(); i++) {
            Path fileNameAndPath = Paths.get(uploadDirectory, files[i].getOriginalFilename());

            System.out.println(fileNameAndPath);

            contendersDto.getContenderList().get(i).setImageUrl(String.valueOf(fileNameAndPath));
            Files.write(fileNameAndPath, files[i].getBytes());
        }
        contenderService.saveAll(contendersDto.getContenderList());
        return "redirect:/contenders/all";
    }

    @GetMapping("/contenders/all")
    public String showAll(Model model) {
        model.addAttribute("contenders", contenderService.getAll());
        return "allContenders";
    }

}
