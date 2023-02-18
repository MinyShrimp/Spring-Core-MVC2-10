package hello.springcoremvc210.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class FormatterForm {
    @NumberFormat(pattern = "###,###")
    private final Integer number;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime localDateTime;
}
