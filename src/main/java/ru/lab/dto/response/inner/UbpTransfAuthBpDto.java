package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

@Getter
@Setter
public class UbpTransfAuthBpDto implements Flattenable {

    private String ppoName;
    private String ppoCode;
    private String budLevelNsiCode;
    private String budLevelNsiName;
    private String budgetNsiCode;
    private String budgetNsiName;
    private String codeBk;
    private String headName;


}
