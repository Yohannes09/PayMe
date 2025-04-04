//package com.payme.app.entity;
//
//import com.payme.app.diffMicroServ.constants.CardType;
//import com.payme.app.diffMicroServ.constants.PaymentType;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.GenericGenerator;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Builder
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "payment_methods")
//public class PaymentMethod {
//
//    @Id
//    @Column(name = "id", updatable = false, nullable = false)
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    private User user;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private PaymentType paymentType;
//
//    // Use String because of leading zeros, length, and formatting
//    @Size(max = 20)
//    @Column(unique = true)
//    private String accountNumber;
//
//    @Size(max = 9)
//    @Column(length = 9)
//    private String routingNumber;
//
//    @Size(max = 16)
//    @Column(unique = true)
//    private String cardNumber;
//
//    @Column()
//    private CardType cardType;
//
//    @Column()
//    private LocalDate expirationDate;
//
//    @Column()
//    private boolean isPrimary;
//
//    @Column()
//    private boolean isActive;
//
//    @Column(updatable = false)
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//}
