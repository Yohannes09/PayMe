//package com.tenmo.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Table(name = "transfer_type")
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class TransferType {
//    @Id
//    @Column(name = "transfer_type_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer typeId;
//
//    @Column(name = "transfer_type_desc")
//    private String typeDescription;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//
//    public TransferType(String typeDescription){
//        this.typeDescription = typeDescription;
//        this.createdAt = LocalDateTime.now();
//    }
//}
