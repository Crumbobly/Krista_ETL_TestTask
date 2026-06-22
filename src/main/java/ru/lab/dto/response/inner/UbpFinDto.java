package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDateTime;

@Getter
@Setter
public class UbpFinDto  implements Flattenable {

    private String finDocName;
    private String finDocNum;
    private LocalDateTime finDocDate;

}
