package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDate;

@Getter
@Setter
public class FacialAccountDto implements Flattenable{

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
