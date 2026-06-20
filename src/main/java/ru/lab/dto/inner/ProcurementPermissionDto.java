package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProcurementPermissionDto extends FlattenerDto {

    private String name;
    private String code;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


}
