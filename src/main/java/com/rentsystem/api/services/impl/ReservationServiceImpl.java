package com.rentsystem.api.services.impl;

import com.rentsystem.api.dto.request.ReservationRequestDto;
import com.rentsystem.api.dto.response.RentalObjectResponseDto;
import com.rentsystem.api.dto.response.ReservationResponseDto;
import com.rentsystem.api.exceptions.NotFoundException;
import com.rentsystem.api.exceptions.ReservationConflictException;
import com.rentsystem.api.models.Customer;
import com.rentsystem.api.models.RentalObject;
import com.rentsystem.api.models.Reservation;
import com.rentsystem.api.repositories.CustomerRepository;
import com.rentsystem.api.repositories.RentalObjectRepository;
import com.rentsystem.api.repositories.ReservationRepository;
import com.rentsystem.api.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final RentalObjectRepository rentalObjectRepository;

    @Override
    public ReservationResponseDto save(ReservationRequestDto reservationRequestDto) {
        boolean isReservationConflict = reservationRepository.existsReservationByPeriod(
                reservationRequestDto.getRentalObjectId(),
                reservationRequestDto.getStartDate(),
                reservationRequestDto.getEndDate()
        );

        if (isReservationConflict) {
            throw new ReservationConflictException("There is already a reservation, either partial or full, for the specified period");
        }

        Reservation reservation = createOrUpdateReservation(null, reservationRequestDto);
        return mapToReservationResponseDto(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponseDto update(Long reservationId, ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));

        reservation = reservationRepository.save(createOrUpdateReservation(reservation, reservationRequestDto));
        return mapToReservationResponseDto(reservation);
    }

    @Override
    public List<ReservationResponseDto> findAllReservationForTenantName(String tenantName) {
        return reservationRepository.findAllByTenantName(tenantName).stream()
                .map(this::mapToReservationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponseDto> findAllReservationForRentalObjectId(Long rentalObjectId) {
        return reservationRepository.findAllByRentalObjectId(rentalObjectId).stream()
                .map(this::mapToReservationResponseDto)
                .collect(Collectors.toList());
    }

    private ReservationResponseDto mapToReservationResponseDto(Reservation reservation) {
        return ReservationResponseDto.builder()
                .id(reservation.getId())
                .rentalObjectResponseDto(mapToRentalObjectResponseDto(reservation.getRentalObject()))
                .tenantName(reservation.getTenant().getName())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .cost(reservation.getCost())
                .build();
    }

    private RentalObjectResponseDto mapToRentalObjectResponseDto(RentalObject rentalObject) {
        return RentalObjectResponseDto.builder()
                .id(rentalObject.getId())
                .landlordName(rentalObject.getLandlord().getName())
                .unitPrice(rentalObject.getUnitPrice())
                .area(rentalObject.getArea())
                .description(rentalObject.getDescription())
                .build();
    }

    private Reservation createOrUpdateReservation(Reservation reservation, ReservationRequestDto reservationRequestDto) {
        Customer tenant = customerRepository.findByName(reservationRequestDto.getTenantName())
                .orElseThrow(() -> new NotFoundException("Tenant not found with name: " + reservationRequestDto.getTenantName()));
        RentalObject rentalObject = rentalObjectRepository.findById(reservationRequestDto.getRentalObjectId())
                .orElseThrow(() -> new NotFoundException("Rental object not found with ID: " + reservationRequestDto.getRentalObjectId()));

        long rentalTerm = ChronoUnit.DAYS.between(reservationRequestDto.getStartDate(), reservationRequestDto.getEndDate()) + 1;
        BigDecimal rentalCost = rentalObject.getUnitPrice().multiply(BigDecimal.valueOf(rentalTerm));

        if (reservation == null) {
            reservation = new Reservation();
        }

        reservation.setRentalObject(rentalObject);
        reservation.setTenant(tenant);
        reservation.setStartDate(reservationRequestDto.getStartDate());
        reservation.setEndDate(reservationRequestDto.getEndDate());
        reservation.setCost(rentalCost);

        return reservation;
    }
}
