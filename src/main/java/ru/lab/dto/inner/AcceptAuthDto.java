package ru.lab.dto.inner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AcceptAuthDto extends FlattenerDto {

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
