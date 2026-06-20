package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class UbpFinDto extends FlattenerDto {

    private String finDocName;
    private String finDocNum;
    private LocalDateTime finDocDate;

}
