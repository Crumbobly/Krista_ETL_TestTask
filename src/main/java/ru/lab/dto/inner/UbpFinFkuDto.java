package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UbpFinFkuDto extends FlattenerDto {

    private String fkuDocName;
    private String fkuDocNum;
    private LocalDateTime fkuDocDate;

}
