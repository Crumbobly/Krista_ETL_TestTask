package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class InfoDto extends FlattenerDto {

    private String regNum;
    private String code;
    private String divisionParentName;
    private String divisionParentCode;
    private String dsp;
    private String ogrn;
    private String fullName;
    private String shortName;
    private String inn;
    private String kpp;
    private String specEventCode;
    private String specEventCodeDop1;
    private String specEventCodeDop2;
    private String specEventCodeDop3;
    private LocalDateTime regDate;
    private String okopfName;
    private String okopfCode;
    private String refokopfCode;
    private String okfsName;
    private String okfsCode;
    private String refokfsCode;
    private String postIndex;
    private String regionKladrCode;
    private String regionCode;
    private String regionType;
    private String regionName;
    private String planningStructureType;
    private String planningStructureName;

    private String areaCode;
    private String areaType;
    private String areaName;
    private String cityCode;
    private String cityType;
    private String cityName;
    private String localCode;
    private String localType;
    private String localName;
    private String streetCode;
    private String streetType;
    private String streetName;
    private String house;
    private String building;
    private String apartment;
    private String oktmoCode;
    private String oktmoName;
    private String orfkName;

    private String orfkCode;
    private String reforfkCode;
    private String oksmName;
    private String oksmCode;
    private String location;
    private String kbkCode;

    private String kbkName;

    private String okoguCode;
    private String refokoguCode;
    private String okoguName;
    private String okpoCode;

    private String orgTypeCode;
    private String orgTypeName;
    private String establishmentKindCode;
    private String establishmentKindName;

    private String legalPersonKindCode;
    private String legalPersonKindName;
    private String ougvName;
    private String ougvCode;
    private String uoCode;
    private String uoName;
    private String creatorKindName;

    private String creatorKindCode;

    private String creatorPlaceName;
    private String creatorPlaceCode;

    private String founderKindName;

    private String founderPlaceName;
    private String founderPlaceCode;
    private String founderKindCode;

    private String budgetLvlName;
    private String budgetLvlCode;

    private String budgetName;
    private String budgetCode;
    private String statusCode;
    private String statusName;

    private Boolean naibZnachUch;
    private Boolean isOGV;
    private String isUch;
    private Boolean isObosob;
    private Boolean isExcluded;
    private Boolean isReorg;

    private String orgStatus;
    private String parentCode;
    private String parentName;
    private String firmName;
    private String kofkCode;
    private String refkofkCode;
    private String nameDocs;
    private String okatoCode;
    private String okatoName;

    private String guid;   // GUID type

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;

    private String recordNum;
    private String controlNum;
    private String bidNum;
    private String firstRegGuid;   // GUID type
    private LocalDateTime firstRegDate;
    private String lastRegNum;
    private String lastRegGuid;  // GUID type
    private LocalDateTime lastRegDate;
    private String updateReason;
    private Integer updateNum;  // Number type
    private String accMgmt;
    private LocalDateTime inclusionDate;
    private LocalDateTime exclusionDate;
    private String pubpCode;
    private String rubpCode;
    private String nubpCode;
    private String cpzCode;
    private String pgmyCode;

    private String parentRecordNum;
    private String reformationDocument;
    private String reformationDocumentNum;
    private LocalDate reformationDocumentDate;
    private String reformationName;
    private String reformationCode;
    private LocalDate reformationStartDate;
    private LocalDate reformationEndDate;
    private String contourTypeCode;
    private LocalDate loadDate;
    private LocalDate dateUpdate;
    private String egrulNotIncluded;

    // Отсутствует в документации
    private String ppotypecode;
    private String ppotypename;
    //

}
