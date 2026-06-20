package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

@Getter
@Setter
public class PermissionDto extends FlattenerDto {

    private String permissionCode;
    private String permissionName;

}
