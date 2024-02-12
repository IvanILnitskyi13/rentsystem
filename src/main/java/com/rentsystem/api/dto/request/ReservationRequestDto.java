package com.rentsystem.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReservationRequestDto {
    @NotNull(message = "Cannot have a null value")
    private Long rentalObjectId;

    @NotNull(message = "Cannot have a null value")
    @Size(min = 3, max = 200, message = "Must be between 3 and 200 characters")
    private String tenantName;

    @NotNull(message = "Cannot have a null value")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Future(message = "Must be a date in the future")
    private LocalDate startDate;

    @NotNull(message = "Cannot have a null value")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Future(message = "Must be a date in the future")
    private LocalDate endDate;
}
