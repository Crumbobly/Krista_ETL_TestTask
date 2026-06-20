package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

@Getter
@Setter
public class FoAccountDto extends FlattenerDto {

    private String foName;
    private String foCode;
    private String accountTypeName;
    private String num;

}
