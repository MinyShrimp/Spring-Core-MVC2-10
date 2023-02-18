package hello.springcoremvc210.config;

import hello.springcoremvc210.converter.IntegerToStringConverter;
import hello.springcoremvc210.converter.IpPortToStringConverter;
import hello.springcoremvc210.converter.StringToIntegerConverter;
import hello.springcoremvc210.converter.StringToIpPortConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(
            FormatterRegistry registry
    ) {
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
    }
}
