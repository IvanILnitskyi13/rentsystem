package com.rentsystem.api.repositories;

import com.rentsystem.api.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByTenantName(String tenantName);
    List<Reservation> findAllByRentalObjectId(Long rentalPropertyId);

    @Query("""
        SELECT
            COUNT(r) > 0
        FROM Reservation r
        WHERE
            r.rentalObject.id = :rentalObjectId
            AND (r.startDate BETWEEN :startDate AND :endDate
            OR r.endDate BETWEEN :startDate AND :endDate)
    """)
    boolean existsReservationByPeriod(@Param("rentalObjectId") Long rentalObjectId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
}
