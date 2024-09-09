package com.techelevator.tenmo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Used to send relevant and non-sensitive transfer details to the client.*/

public record TransferResponseDto(Long transferId,
                                  String accountFromUsername,
                                  String accountToUsername,
                                  BigDecimal amount,
                                  String transferMessage,
                                  String currency,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt,
                                  String transferTypeDescription,   // not good practice to reveal the id, instead provide the descriptions
                                  String transferStatusDescription ){
    public TransferResponseDto{
        currency = currency.toUpperCase();
    }

}

//public class TransferResponseDto {
//
//    private  Long transferId;
//
//    private  Long senderAccountId;
//
//    private  Long recipientAccountId;
//
//    private  String senderUsername;
//
//    private  String recipientUsername;
//
//    private BigDecimal amount;
//
//    private  Integer transferStatusId;
//
//    private  Integer transferTypeId;
//
//
//    public TransferResponseDto(){
//
//    }
//
//    public TransferResponseDto(
//            Long transferId,
//            Long senderAccountId,
//            Long recipientAccountId,
//            String senderUsername,
//            String recipientUsername,
//            BigDecimal amount) {
//
//        this.transferId = transferId;
//        this.senderAccountId = senderAccountId;
//        this.recipientAccountId = recipientAccountId;
//        this.senderUsername = senderUsername;
//        this.recipientUsername = recipientUsername;
//        this.amount = amount;
//    }
//
//
//    // GETTERS
//    public Long getTransferId() {
//        return transferId;
//    }
//
//    public String getSenderUsername() {
//        return senderUsername;
//    }
//
//    public String getRecipientUsername() {
//        return recipientUsername;
//    }
//
//    public Long getSenderAccountId() {
//        return senderAccountId;
//    }
//
//    public Long getRecipientAccountId() {
//        return recipientAccountId;
//    }
//
//    public BigDecimal getAmount() {
//        return amount;
//    }
//
//
//    // SETTERS
//    public void setTransferId(Long transferId) {
//        this.transferId = transferId;
//    }
//
//    public void setSenderAccountId(Long senderAccountId) {
//        this.senderAccountId = senderAccountId;
//    }
//
//    public void setRecipientAccountId(Long recipientAccountId) {
//        this.recipientAccountId = recipientAccountId;
//    }
//
//    public void setSenderUsername(String senderUsername) {
//        this.senderUsername = senderUsername;
//    }
//
//    public void setRecipientUsername(String recipientUsername) {
//        this.recipientUsername = recipientUsername;
//    }
//
//    public void setAmount(BigDecimal amount) {
//        this.amount = amount;
//    }
//
//    @Override
//    public String toString() {
//        return "TransferResponseDto{" +
//                "transferId=" + transferId +
//                ", senderAccountId=" + senderAccountId +
//                ", recipientAccountId=" + recipientAccountId +
//                ", senderUsername='" + senderUsername + '\'' +
//                ", recipientUsername='" + recipientUsername + '\'' +
//                ", amount=" + amount +
//                '}';
//    }
//}
