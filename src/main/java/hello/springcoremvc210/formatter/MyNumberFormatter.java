package hello.springcoremvc210.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {
    @Override
    public Number parse(
            String text,
            Locale locale
    ) throws ParseException {
        log.info("MyNumberFormatter.parse call");
        log.info("text = {}, locale = {}", text, locale);

        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text);
    }

    @Override
    public String print(
            Number number,
            Locale locale
    ) {
        log.info("MyNumberFormatter.print call");
        log.info("number = {}, locale = {}", number, locale);

        return NumberFormat.getInstance(locale).format(number);
    }
}
