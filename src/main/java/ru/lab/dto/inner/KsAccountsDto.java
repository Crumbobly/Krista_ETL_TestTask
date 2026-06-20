package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class KsAccountsDto extends FlattenerDto {

    private String num;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private String openTofkCode;
    private String openTofkName;
    private String refSrvUfkCode;
    private String accountVidName;
    private String ppoCode;

}
