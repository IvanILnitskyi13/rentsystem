package com.rentsystem.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "rental_object")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentalObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "landlord_id", referencedColumnName = "id", nullable = false)
    private Customer landlord;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private BigDecimal area;

    @Column(nullable = false)
    private String description;
}
