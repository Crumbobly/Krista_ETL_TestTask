package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipantPermissionDto  implements Flattenable {

    private String code;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


}
