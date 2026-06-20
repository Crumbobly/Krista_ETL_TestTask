package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class HeadDto extends FlattenerDto {

    private String fio;
    private String post;
    private String headMain;  // must be BOOLEAN, but docs conflict. We have values like "0", but only "false", "true" is allowed.
    private String docName;
    private String docNum;
    private LocalDate docDate;

}
