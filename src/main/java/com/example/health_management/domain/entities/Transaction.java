package com.example.health_management.domain.entities;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amount;
    private String zpTransToken;
    private String mRefundId;
    private String transactionId;
    @ManyToOne(targetEntity = AppointmentRecord.class)
    @JoinColumn(name = "appointment_record_id", referencedColumnName = "id", nullable = true)
    private AppointmentRecord appointmentRecord;
}
