package hello.springcoremvc210.config;

import hello.springcoremvc210.converter.IpPortToStringConverter;
import hello.springcoremvc210.converter.StringToIpPortConverter;
import hello.springcoremvc210.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(
            FormatterRegistry registry
    ) {
        /**
         * 주석 처리 이유
         * - 우선순위가 Formatter 보다 Converter 가 높다
         * - Converter > Formatter
         */
        // registry.addConverter(new StringToIpPortConverter());
        // registry.addConverter(new IpPortToStringConverter());

        // Converter 등록
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        // Formatter 등록
        registry.addFormatter(new MyNumberFormatter());
    }
}
