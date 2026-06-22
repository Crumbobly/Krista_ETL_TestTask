package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.util.List;

@Getter
@Setter
public class AuthorityDto implements Flattenable{

    private String authorityCode;
    private String authorityName;
    private List<PermissionDto> permissions;

}
