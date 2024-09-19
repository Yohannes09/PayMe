package com.tenmo.util;

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
    REJECTED("Rejected", 3),
    COMPLETED("COMPLETED", 4);

    private static final Set<Integer> transferStatusIds = Stream.of(TransferStatus.values())
            .map(TransferStatus::getStatusId)
            .collect(Collectors.toSet());

    private final String statusDescription;
    private final Integer statusId;

    public static Set<Integer> getValidTransferStatus(){
        return transferStatusIds;
    }
}
