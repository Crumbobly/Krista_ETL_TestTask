package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

@Getter
@Setter
public class ActivityDto extends FlattenerDto {

    private String activityCode;
    private String refActivityCode;
    private String activityName;
    private String activityKind;

}
