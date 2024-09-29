package com.tenmo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* The description for constants should be upper-case for case-insensitive search. */
@Getter
@AllArgsConstructor
public enum TransferStatusEnum {
    PENDING("Pending", 1),
    APPROVED("Approved", 2),
    REJECTED("Rejected", 3),
    COMPLETED("COMPLETED", 4);

    private final String description;
    private final Integer id;


    private static final Map<String, Integer> STATUS_IDS =
            Stream.of(TransferStatusEnum.values())
            .collect(Collectors.toMap(TransferStatusEnum::getDescription, TransferStatusEnum::getId));

    public static Integer findIdByDescription(String description){
        return STATUS_IDS.getOrDefault(description, -1);
    }

}
