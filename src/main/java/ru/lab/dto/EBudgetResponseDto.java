package ru.lab.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.inner.AcceptAuthDto;
import ru.lab.dto.inner.ActivityDto;
import ru.lab.dto.inner.AuthorityDto;
import ru.lab.dto.inner.ContactDto;
import ru.lab.dto.inner.ContractDto;
import ru.lab.dto.inner.FacialAccountDto;
import ru.lab.dto.inner.FoAccountDto;
import ru.lab.dto.inner.HeadDto;
import ru.lab.dto.inner.InfoDto;
import ru.lab.dto.inner.KsAccountsDto;
import ru.lab.dto.inner.NonParticipantPermissionDto;
import ru.lab.dto.inner.ParticipantPermissionDto;
import ru.lab.dto.inner.ProcurementPermissionDto;
import ru.lab.dto.inner.SuccessionDto;
import ru.lab.dto.inner.TransFAuthDto;
import ru.lab.dto.inner.UbpFinDto;
import ru.lab.dto.inner.UbpFinFkuDto;
import ru.lab.dto.inner.UbpTransFAuthBpDto;
import ru.lab.dto.inner.UbpTransFAuthBuDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EBudgetResponseDto extends FlattenerDto {

    private InfoDto info;

    private List<ActivityDto> activities;
    private List<AuthorityDto> authorities;
    private List<HeadDto> heads;
    private List<FacialAccountDto> facialAccounts;
    private List<FoAccountDto> foAccounts;
    private List<ContractDto> contracts;
    private List<ParticipantPermissionDto> participantPermissions;
    private List<NonParticipantPermissionDto> nonParticipantPermissions;
    private List<ProcurementPermissionDto> procurementPermissions;
    private List<AcceptAuthDto> acceptAuths;
    private List<TransFAuthDto> transFAuth;
    private List<UbpTransFAuthBpDto> ubpTransFAuthBp;
    private List<SuccessionDto> successions;
    private List<ContactDto> contacts;
    private List<UbpFinFkuDto> ubpFinFku;
    private List<UbpFinDto> ubpfin;
    private List<UbpTransFAuthBuDto> ubpTransFAuthBu;
    private List<KsAccountsDto> ksAccounts;

    // В доках нет
//    private List<Object> attachment;
    //


}
