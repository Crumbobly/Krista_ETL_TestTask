package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

@Getter
@Setter
public class PermissionDto implements Flattenable {

    private String permissionCode;
    private String permissionName;

}
