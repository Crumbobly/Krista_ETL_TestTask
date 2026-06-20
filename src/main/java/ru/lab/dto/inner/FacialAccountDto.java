package ru.lab.dto.inner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class FacialAccountDto extends FlattenerDto {

    private String srvUfkCode;
    private String refSrvUfkCode;
    private String srvUfkName;
    private String openTofkCode;
    private String openTofkName;
    private String openUfkCode;
    private String refOpenUfkCode;
    private String openUfkName;
    private String kindName;
    private String kindCode;
    private String status;
    private String num;
    private LocalDate closeDate;
    private LocalDate createDate;
    private String accountOrgCode;
    private String accountOrgFullName;
    private String ppoCode;
    private String ppoName;

}
