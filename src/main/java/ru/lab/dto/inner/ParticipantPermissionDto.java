package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipantPermissionDto extends FlattenerDto {

    private String code;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


}
