package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDateTime;

@Getter
@Setter
public class UbpFinFkuDto implements Flattenable {

    private String fkuDocName;
    private String fkuDocNum;
    private LocalDateTime fkuDocDate;

}
