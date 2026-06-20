package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.util.List;

@Getter
@Setter
public class AuthorityDto extends FlattenerDto {

    private String authorityCode;
    private String authorityName;
    private List<PermissionDto> permissions;

}
