package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDate;

@Getter
@Setter
public class TransfAuthDto implements Flattenable {

    private String authFoVillagesPpoName;
    private String authFoVillagesPpoCode;
    private String authFoVillagesCode;
    private String authFoVillagesName;
    private String authFoMunicipalCode;
    private String authFoMunicipalName;
    private String authRegNum;
    private LocalDate authStartDate;
    private LocalDate authEndDate;
    private String kbkGlavaCode;
    private String budgetCode;

}
