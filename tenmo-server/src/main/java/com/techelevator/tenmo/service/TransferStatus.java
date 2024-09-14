package com.techelevator.tenmo.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TransferStatus {
    PENDING("Pending", 1),
    APPROVED("Approved", 2),
    REJECTED("Rejected", 3);

    private static final Set<Integer> transferStatusIds = Stream.of(TransferStatus.values())
            .map(TransferStatus::getStatusId)
            .collect(Collectors.toSet());

    private final String statusDescription;
    private final Integer statusId;

    public static Set<Integer> validTransferIds(){
        return transferStatusIds;
    }
}
