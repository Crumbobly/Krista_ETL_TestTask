package ru.lab.dto.response;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.response.inner.AcceptAuthDto;
import ru.lab.dto.response.inner.ActivityDto;
import ru.lab.dto.response.inner.AuthorityDto;
import ru.lab.dto.response.inner.ContactDto;
import ru.lab.dto.response.inner.ContractDto;
import ru.lab.dto.response.inner.FacialAccountDto;
import ru.lab.dto.response.inner.FoAccountDto;
import ru.lab.dto.response.inner.HeadDto;
import ru.lab.dto.response.inner.InfoDto;
import ru.lab.dto.response.inner.KsAccountsDto;
import ru.lab.dto.response.inner.NonParticipantPermissionDto;
import ru.lab.dto.response.inner.ParticipantPermissionDto;
import ru.lab.dto.response.inner.ProcurementPermissionDto;
import ru.lab.dto.response.inner.SuccessionDto;
import ru.lab.dto.response.inner.TransfAuthDto;
import ru.lab.dto.response.inner.UbpFinDto;
import ru.lab.dto.response.inner.UbpFinFkuDto;
import ru.lab.dto.response.inner.UbpTransfAuthBpDto;
import ru.lab.dto.response.inner.UbpTransfAuthBuDto;
import ru.lab.service.flattener.Flattenable;

import java.util.List;

@Getter
@Setter
public class EBudgetResponseDto implements Flattenable{

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
    private List<TransfAuthDto> transfAuth;
    private List<UbpTransfAuthBpDto> ubpTransfAuthBp;
    private List<SuccessionDto> successions;
    private List<ContactDto> contacts;
    private List<UbpFinFkuDto> ubpFinFku;
    private List<UbpFinDto> ubpfin;
    private List<UbpTransfAuthBuDto> ubpTransfAuthBu;
    private List<KsAccountsDto> ksAccounts;

    // В доках нет
    // private List<Object> attachment;
    //


}
