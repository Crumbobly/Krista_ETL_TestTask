package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDateTime;

@Getter
@Setter
public class KsAccountsDto implements Flattenable{

    private String num;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private String openTofkCode;
    private String openTofkName;
    private String refSrvUfkCode;
    private String accountVidName;
    private String ppoCode;

}
