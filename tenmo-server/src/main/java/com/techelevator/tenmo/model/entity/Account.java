//package com.techelevator.tenmo.model.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//// Industry convention for creating entities. Avoids repetitive code.
//// Fields cannot be final because Jpa needs a no-args constructor and ability to set fields.
//
////Lombok
//@NoArgsConstructor
//@AllArgsConstructor
//@Setter
//@Getter
//// Jpa
//@Table(name = "account")
//@Entity
//public class Account {
//
//    @Id
//    @Column(name = "account_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA needs to know the DB will handle the Generation.
//    private Long accountId;
//
//    @Column(name = "user_id") // table row has underscore in the variable name hence the @Column annotation.
//    private Long userId;
//
//    @Column(name = "account_number")
//    private Long accountNumber;
//
//    @Column(name = "account_type")
//    private AccountType accountType;    // enum syntax for Postgres-> account_type ENUM('checking', 'savings', ...)
//
//    private BigDecimal balance;
//
//    @Column
//    private LocalDateTime acountCreatedOn;
//}
