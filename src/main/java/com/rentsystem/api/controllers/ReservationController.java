package com.rentsystem.api.controllers;

import com.rentsystem.api.dto.request.ReservationRequestDto;
import com.rentsystem.api.dto.response.ReservationResponseDto;
import com.rentsystem.api.services.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationResponseDto> saveReservation(@Valid @RequestBody ReservationRequestDto reservationDto) {
        ReservationResponseDto savedReservation = reservationService.save(reservationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    @PutMapping(value = "/update/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationResponseDto> updateReservation(
            @PathVariable(value = "reservationId") Long reservationId,
            @RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationResponseDto updatedReservation = reservationService.update(reservationId, reservationRequestDto);
        return ResponseEntity.ok(updatedReservation);
    }

    @GetMapping("/tenant/{tenantName}")
    public ResponseEntity<?> getReservationsForTenant(@PathVariable(value = "tenantName") String tenantName) {
        List<ReservationResponseDto> reservations = reservationService.findAllReservationForTenantName(tenantName);

        if (reservations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/rental-property/{rentalObjectId}")
    public ResponseEntity<?> getReservationsForRentalObjectId(@PathVariable(value = "rentalObjectId") Long rentalObjectId) {
        List<ReservationResponseDto> reservations = reservationService.findAllReservationForRentalObjectId(rentalObjectId);

        if (reservations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(reservations);
    }
}
