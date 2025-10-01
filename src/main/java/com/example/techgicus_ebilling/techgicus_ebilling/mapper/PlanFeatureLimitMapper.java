package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PlanFeatureLimit;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.featureDto.FeatureResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PlanFeatureLimitMapper {

    @Mapping(target = "featureId",   source = "feature.featureId")
    @Mapping(target = "code",        source = "feature.code")
    @Mapping(target = "name",        source = "feature.name")
    @Mapping(target = "description", source = "feature.description")

    // usageLimit needs to look at both usageLimit/booleanLimit + type
    @Mapping(target = "usageLimit", expression = "java(formatUsageLimit(pfl))")
//    @Mapping(target = "limitType",  expression = "java(pfl.getLimitType() != null ? pfl.getLimitType().name() : null)")
    FeatureResponseDto toDto(PlanFeatureLimit pfl);

    Set<FeatureResponseDto> toDtoSet(Set<PlanFeatureLimit> pfls);

    // Helper for computed field
    default String formatUsageLimit(PlanFeatureLimit pfl) {
        if (pfl == null || pfl.getLimitType() == null) return null;
        switch (pfl.getLimitType()) {
            case BOOLEAN:
                return pfl.getBooleanLimit() == null ? null : String.valueOf(pfl.getBooleanLimit());
            case INTEGER:
                return pfl.getUsageLimit() == null ? null : String.valueOf(pfl.getUsageLimit());
            default:
                return null;
        }
    }
}
