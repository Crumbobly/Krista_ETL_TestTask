package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

@Getter
@Setter
public class ActivityDto implements Flattenable{

    private String activityCode;
    private String refActivityCode;
    private String activityName;
    private String activityKind;

}
