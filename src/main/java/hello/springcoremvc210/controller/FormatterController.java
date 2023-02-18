package hello.springcoremvc210.controller;

import hello.springcoremvc210.type.FormatterForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/formatter")
public class FormatterController {

    @GetMapping("/edit")
    public String formatterForm(
            Model model
    ) {
        FormatterForm form = new FormatterForm(10000, LocalDateTime.now());
        model.addAttribute("form", form);

        return "formatter/form";
    }

    @PostMapping("/edit")
    public String formatterEdit(
            @ModelAttribute("form") FormatterForm form
    ) {
        return "formatter/view";
    }
}
