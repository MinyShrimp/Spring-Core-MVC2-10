package hello.springcoremvc210.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {
    /**
     * Converting String to Integer
     *
     * @param source String type
     * @return Integer type
     */
    @Override
    public Integer convert(String source) {
        log.info("Convert String To Integer source = {}", source);
        return Integer.valueOf(source);
    }
}
