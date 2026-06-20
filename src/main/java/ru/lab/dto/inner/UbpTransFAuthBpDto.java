package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

@Getter
@Setter
public class UbpTransFAuthBpDto extends FlattenerDto {

    private String ppoName;
    private String ppoCode;
    private String budLevelNsiCode;
    private String budLevelNsiName;
    private String budgetNsiCode;
    private String budgetNsiName;
    private String codeBk;
    private String headName;


}
