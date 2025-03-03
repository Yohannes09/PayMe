package com.payme.app.constants;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* The description for constants should be upper-case for case-insensitive search. */
public enum TransactionStatus {
    PENDING("Pending", 1),
    APPROVED("Approved", 2),
    REJECTED("Rejected", 3),
    COMPLETED("Completed", 4);

    private final String description;
    private final Integer id;

    TransactionStatus(String description, Integer id) {
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getId() {
        return this.id;
    }

    public static final Map<String, Integer> STATUS_IDS =
            Stream.of(TransactionStatus.values())
                    .collect(Collectors.toMap(TransactionStatus::getDescription, TransactionStatus::getId));

    public static Integer findIdByDescription(String description) {
        return STATUS_IDS.getOrDefault(description, -1);
    }

}
