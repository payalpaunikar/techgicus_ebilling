package com.example.techgicus_ebilling.techgicus_ebilling.dto.featureDto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "this is the feature of the plan")
public class FeatureResponseDto {

    @Schema(example = "1",type = "number")
    private Long featureId;

    @Schema(example = "CREATE_MULTI_COMPANY",type = "String")
    private String code;

    @Schema(example = "Create multiple companies",type = "String")
    private String name;

    @Schema(example = "Create multiple companies",type = "String")
    private String description;

    @Schema(example = "3")
    private String usageLimit;

    public FeatureResponseDto() {
    }

    public FeatureResponseDto(Long featureId, String code, String name, String description, String usageLimit) {
        this.featureId = featureId;
        this.code = code;
        this.name = name;
        this.description = description;
        this.usageLimit = usageLimit;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(String usageLimit) {
        this.usageLimit = usageLimit;
    }
}
