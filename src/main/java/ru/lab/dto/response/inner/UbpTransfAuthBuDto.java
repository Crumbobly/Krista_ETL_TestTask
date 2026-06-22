package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDateTime;

@Getter
@Setter
public class UbpTransfAuthBuDto implements Flattenable {

    private String authBuAuthOrgCode;
    private String authBuAuthName;
    private LocalDateTime authBuStartDate;
    private LocalDateTime authBuEndDate;

}
