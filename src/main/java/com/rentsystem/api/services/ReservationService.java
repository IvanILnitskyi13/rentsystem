package com.rentsystem.api.services;

import com.rentsystem.api.dto.request.ReservationRequestDto;
import com.rentsystem.api.dto.response.ReservationResponseDto;

import java.util.List;

public interface ReservationService {
    ReservationResponseDto save(ReservationRequestDto reservationDto);
    ReservationResponseDto update(Long reservationId, ReservationRequestDto reservationDto);
    List<ReservationResponseDto> findAllReservationForTenantName(String tenantName);
    List<ReservationResponseDto> findAllReservationForRentalObjectId(Long rentalPropertyId);
}
