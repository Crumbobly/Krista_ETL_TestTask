package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

@Getter
@Setter
public class FoAccountDto implements Flattenable{

    private String foName;
    private String foCode;
    private String accountTypeName;
    private String num;

}
