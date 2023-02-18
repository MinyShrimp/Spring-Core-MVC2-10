package hello.springcoremvc210.controller;

import hello.springcoremvc210.type.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {
    @GetMapping("/hello/v1")
    public String helloV1(
            HttpServletRequest req
    ) {
        String data = req.getParameter("data");
        Integer intValue = Integer.valueOf(data);

        log.info("intValue = {}", intValue);
        log.info("typeof data = {}", data.getClass().getSimpleName());
        log.info("typeof intValue = {}", intValue.getClass().getSimpleName());

        return "hello " + data;
    }

    @GetMapping("/hello/v2")
    public String helloV2(
            @RequestParam Integer data
    ) {
        log.info("data = {}", data);
        log.info("typeof data = {}", data.getClass().getSimpleName());

        return "hello " + data;
    }

    @GetMapping("/ip-port")
    public String ipPort(
            @RequestParam IpPort ipPort
    ) {
        log.info("ipPort = {}", ipPort);
        return ipPort.toString();
    }
}
