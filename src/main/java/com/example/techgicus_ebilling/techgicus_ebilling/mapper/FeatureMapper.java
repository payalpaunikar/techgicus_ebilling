package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Feature;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.featureDto.FeatureResponseDto;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface FeatureMapper {
    FeatureResponseDto toDto(Feature feature);
    Set<FeatureResponseDto> toDtoSet(Set<Feature> features);
}
