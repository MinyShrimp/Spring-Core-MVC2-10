package hello.springcoremvc210.converter;

import hello.springcoremvc210.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {
    @Override
    public String convert(IpPort source) {
        log.info("Convert IpPort To String source = {}", source);
        return source.getIp() + ":" + source.getPort();
    }
}
