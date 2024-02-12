package com.rentsystem.api.services;

import com.rentsystem.api.dto.request.ReservationRequestDto;
import com.rentsystem.api.dto.response.ReservationResponseDto;
import com.rentsystem.api.exceptions.NotFoundException;
import com.rentsystem.api.exceptions.ReservationConflictException;
import com.rentsystem.api.models.Customer;
import com.rentsystem.api.models.RentalObject;
import com.rentsystem.api.models.Reservation;
import com.rentsystem.api.repositories.CustomerRepository;
import com.rentsystem.api.repositories.RentalObjectRepository;
import com.rentsystem.api.repositories.ReservationRepository;
import com.rentsystem.api.services.impl.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RentalObjectRepository rentalObjectRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void saveReservation_Success() {
        // Arrange
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(1L, "Jan Kowalski", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        Customer tenant = new Customer(1L, "Jan Kowalski");
        Customer landlord = new Customer(1L, "Jolanta Krasowska");
        RentalObject rentalObject = new RentalObject(1L, landlord, BigDecimal.TEN, BigDecimal.ONE, "Kawalerka w Poznaniu");
        Reservation reservation = new Reservation(1L, rentalObject, tenant, reservationRequestDto.getStartDate(), reservationRequestDto.getEndDate(), BigDecimal.TEN);

        when(reservationRepository.existsReservationByPeriod(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);
        when(customerRepository.findByName(tenant.getName())).thenReturn(Optional.of(tenant));
        when(rentalObjectRepository.findById(rentalObject.getId())).thenReturn(Optional.of(rentalObject));
        when(reservationRepository.save(any())).thenReturn(reservation);

        // Act
        ReservationResponseDto reservationResponseDto = reservationService.save(reservationRequestDto);

        // Assert
        assertNotNull(reservationResponseDto);
        verify(customerRepository, times(1)).findByName(tenant.getName());
        verify(rentalObjectRepository, times(1)).findById(rentalObject.getId());
        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    void saveReservation_Conflict() {
        // Arrange
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(1L, "Jan Kowalski", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        when(reservationRepository.existsReservationByPeriod(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        // Act & Assert
        assertThrows(ReservationConflictException.class, () -> reservationService.save(reservationRequestDto));
    }

    @Test
    void updateReservation_Success() {
        // Arrange
        Long reservationId = 1L;
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(1L, "Jan Kowalski", LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));

        Customer tenant = new Customer(1L, "Jan Kowalski");
        Customer landlord = new Customer(1L, "Jolanta Krasowska");
        RentalObject rentalObject = new RentalObject(1L, landlord, BigDecimal.TEN, BigDecimal.ONE, "Kawalerka w Poznaniu");
        Reservation existingReservation = new Reservation(1L, rentalObject, tenant, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), BigDecimal.TEN);
        Reservation updatedReservation = new Reservation(1L, rentalObject, tenant, reservationRequestDto.getStartDate(), reservationRequestDto.getEndDate(), BigDecimal.TEN);


        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existingReservation));
        when(customerRepository.findByName(tenant.getName())).thenReturn(Optional.of(tenant));
        when(rentalObjectRepository.findById(1L)).thenReturn(Optional.of(rentalObject));
        when(reservationRepository.save(any())).thenReturn(updatedReservation);

        // Act
        ReservationResponseDto returnedReservation= reservationService.update(reservationId, reservationRequestDto);

        // Assert
        assertNotNull(returnedReservation);
        assertEquals(reservationId, returnedReservation.getId());
        assertEquals(reservationRequestDto.getStartDate(), returnedReservation.getStartDate());
        assertEquals(reservationRequestDto.getEndDate(), returnedReservation.getEndDate());
        verify(reservationRepository, times(1)).findById(reservationId);
        verify(rentalObjectRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    void updateReservation_NotFound() {
        // Arrange
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(1L, "Oscar Lang", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        when(reservationRepository.findById(reservationRequestDto.getRentalObjectId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> reservationService.update(reservationRequestDto.getRentalObjectId(), reservationRequestDto));
        verify(reservationRepository, times(1)).findById(reservationRequestDto.getRentalObjectId());
        verify(rentalObjectRepository, times(0)).findById(1L);
        verify(reservationRepository, times(0)).save(any());
    }

    @Test
    void findAllReservationForTenantName_Success() {
        // Arrange
        Customer tenant = new Customer(1L, "Jolanta Krasowska");
        Customer landlord = new Customer(1L, "Jan Kowalski");
        RentalObject rentalObject = new RentalObject(1L, landlord, BigDecimal.TEN, BigDecimal.ONE, "Kawalerka w Poznaniu");
        Reservation reservationOne = new Reservation(1L, rentalObject, tenant, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), BigDecimal.TEN);
        Reservation reservationTwo = new Reservation(2L, rentalObject, tenant, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), BigDecimal.TEN);

        when(reservationRepository.findAllByTenantName(tenant.getName())).thenReturn(List.of(reservationOne, reservationTwo));

        // Act
        List<ReservationResponseDto> reservations = reservationService.findAllReservationForTenantName(tenant.getName());

        // Assert
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        verify(reservationRepository, times(1)).findAllByTenantName(tenant.getName());
    }

    @Test
    void findAllReservationForRentalObjectId_Success() {
        // Arrange
        Customer tenant = new Customer(1L, "Jolanta Krasowska");
        Customer landlord = new Customer(1L, "Jan Kowalski");
        RentalObject rentalObject = new RentalObject(1L, landlord, BigDecimal.TEN, BigDecimal.ONE, "Kawalerka w Poznaniu");
        Reservation reservationOne = new Reservation(1L, rentalObject, tenant, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), BigDecimal.TEN);
        Reservation reservationTwo = new Reservation(2L, rentalObject, tenant, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), BigDecimal.TEN);

        when(reservationRepository.findAllByRentalObjectId(rentalObject.getId())).thenReturn(List.of(reservationOne, reservationTwo));

        // Act
        List<ReservationResponseDto> reservations = reservationService.findAllReservationForRentalObjectId(rentalObject.getId());

        // Assert
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        verify(reservationRepository, times(1)).findAllByRentalObjectId(rentalObject.getId());
    }
}