package hello.springcoremvc210.controller;

import hello.springcoremvc210.type.Form;
import hello.springcoremvc210.type.IpPort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/converter")
public class ConverterController {
    @GetMapping("/view")
    public String converterView(
            Model model
    ) {
        model.addAttribute("number", 1000);
        model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));
        return "converter/view";
    }

    @GetMapping("/edit")
    public String converterForm(
            Model model
    ) {
        IpPort ipPort = new IpPort("127.0.0.1", 8080);
        Form form = new Form(ipPort);

        model.addAttribute("form", form);
        return "converter/form";
    }

    @PostMapping("/edit")
    public String converterEdit(
            @ModelAttribute Form form,
            Model model
    ) {
        IpPort ipPort = form.getIpPort();
        model.addAttribute("ipPort", ipPort);
        return "converter/view";
    }
}
