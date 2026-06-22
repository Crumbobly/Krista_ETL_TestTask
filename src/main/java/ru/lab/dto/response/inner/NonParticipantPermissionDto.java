package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDateTime;

@Getter
@Setter
public class NonParticipantPermissionDto  implements Flattenable {

    private String code;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String authBudgCode;
    private String authBudgName;
    private String authPpoCode;
    private String authPpoName;
    private String authKbkGlavaCode;
    private String authKbkGlavaName;
    private String registryNum;

}
