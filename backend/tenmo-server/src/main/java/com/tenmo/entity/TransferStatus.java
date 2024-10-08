//package com.tenmo.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "transfer_status")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class TransferStatus {
//
//    @Column(name = "transfer_status_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    private Integer statusId;
//
//    @Column(name = "status_desc")
//    private String statusDescription;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    public TransferStatus(String statusDescription){
//        this.statusDescription = statusDescription;
//        createdAt = LocalDateTime.now();
//    }
//}
