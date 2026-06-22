package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDate;

@Getter
@Setter
public class AcceptAuthDto implements Flattenable{

    private String code;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String authBudgCode;
    private String authBudgName;
    private String authPpoCode;
    private String authPpoName;
    private String authKbkGlavaCode;
    private String authKbkGlavaName;
    private String authGiverCode;
    private String authGiverName;
    private String userArea;
    private String authRegNum;


}
