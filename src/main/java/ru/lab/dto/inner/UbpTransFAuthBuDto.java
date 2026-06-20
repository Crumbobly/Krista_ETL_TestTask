package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class UbpTransFAuthBuDto extends FlattenerDto {

    private String authBuAuthOrgCode;
    private String authBuAuthName;
    private LocalDateTime authBuStartDate;
    private LocalDateTime authBuEndDate;

}
