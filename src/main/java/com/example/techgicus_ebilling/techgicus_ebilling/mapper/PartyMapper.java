package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PartyActivity;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PartyLedger;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyActivityDTO;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyLedgerDTO;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyRequestDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartyMapper {

//    Party convertRequestIntoEntity(PartyRequestDto partyRequestDto);
//
//    PartyResponseDto convertEntityIntoResponse(Party party);
//
//    List<PartyResponseDto> convertPartiesIntoPartyResponses(List<Party> parties);
//
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updatePartyFromRequest(PartyRequestDto partyRequestDto, @MappingTarget Party party);

    Party convertRequestIntoEntity(PartyRequestDto partyRequestDto);

    PartyResponseDto convertEntityIntoResponse(Party party);

    List<PartyResponseDto> convertPartiesIntoPartyResponses(List<Party> parties);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartyFromRequest(PartyRequestDto partyRequestDto, @MappingTarget Party party);


    @Mapping(target = "ledgerId", source = "ledgerId")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "referenceId", source = "referenceId")
    @Mapping(target = "referenceNo", source = "referenceNo")
    @Mapping(target = "debitAmount", source = "debitAmount")
    @Mapping(target = "creditAmount", source = "creditAmount")
    @Mapping(target = "runningBalance", source = "runningBalance")
    @Mapping(target = "description", source = "description")
    PartyLedgerDTO toLedgerDto(PartyLedger ledger);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "activityDate", source = "activityDate")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "referenceId", source = "referenceId")
    @Mapping(target = "referenceNumber", source = "referenceNumber")
    @Mapping(target = "runningBalance", source = "runningBalance")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "note", source = "note")
    PartyActivityDTO toActivityDto(PartyActivity activity);
}
