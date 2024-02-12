package com.rentsystem.api.dto.response;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class RentalObjectResponseDto {
    private Long id;
    private String landlordName;
    private BigDecimal unitPrice;
    private BigDecimal area;
    private String description;
}
