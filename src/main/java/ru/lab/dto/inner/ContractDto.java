package ru.lab.dto.inner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDate;

@Getter
@Setter
public class ContractDto extends FlattenerDto {

    private String contractNumber;
    private LocalDate signDate;
    private String orgCodeContract;
    private String orgNameContract;

    // нет в доках
    private String orgcontractbudgetcode;
    //



}
