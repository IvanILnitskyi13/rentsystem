package com.rentsystem.api.dto.response;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ReservationResponseDto {
    private Long id;
    private RentalObjectResponseDto rentalObjectResponseDto;
    private String tenantName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal cost;
}
