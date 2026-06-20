package ru.lab.dto.inner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransFAuthDto extends FlattenerDto {

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
