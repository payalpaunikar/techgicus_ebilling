package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyRequestDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartyMapper {

    Party convertRequestIntoEntity(PartyRequestDto partyRequestDto);

    PartyResponseDto convertEntityIntoResponse(Party party);

    List<PartyResponseDto> convertPartiesIntoPartyResponses(List<Party> parties);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartyFromRequest(PartyRequestDto partyRequestDto, @MappingTarget Party party);
}
