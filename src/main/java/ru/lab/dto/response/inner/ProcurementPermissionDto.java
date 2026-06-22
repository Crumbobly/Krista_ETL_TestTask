package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProcurementPermissionDto  implements Flattenable {

    private String name;
    private String code;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


}
