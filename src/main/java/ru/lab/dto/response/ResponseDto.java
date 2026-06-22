package ru.lab.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDto {

    private Integer offset;
    private Integer pageNum;
    private Integer pageSize;
    private Integer recordCount;
    private List<EBudgetResponseDto> data;
    private String order;
    private String orderDirection;

//    private Object limits;  // no info about field

    private Integer version;
    private Integer pageCount;



}
