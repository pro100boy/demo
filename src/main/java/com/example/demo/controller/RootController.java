package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class RootController {
    // TODO проверять на отсутствие nameFilter, чтоб не регексить, а сразу возвращать весь список
    // TODO переадресация с / на /?nameFilter=&page=1&cnt=100
    // TODO использовать ResponseEntity с кодами ошибок (статусами)
    @GetMapping
    public String root(RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
/*        redirectAttributes.addAttribute("nameFilter", "^[В-Яв-я].*$");
        redirectAttributes.addAttribute("page", "1");
        redirectAttributes.addAttribute("cnt", "10");
        //redirectAttributes.addFlashAttribute("fa", true);
        return "redirect:/hello";*/
        return String.format("redirect:%s/?nameFilter=&page=&cnt=", "/hello");
    }
}
