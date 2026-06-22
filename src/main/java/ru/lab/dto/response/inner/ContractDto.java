package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDate;

@Getter
@Setter
public class ContractDto implements Flattenable{

    private String contractNumber;
    private LocalDate signDate;
    private String orgCodeContract;
    private String orgNameContract;

    // нет в доках
    private String orgcontractbudgetcode;
    //



}
