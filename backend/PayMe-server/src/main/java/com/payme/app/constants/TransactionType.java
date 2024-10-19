package com.payme.app.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*  The description for constants should be uppercase for case-insensitive search. */
@Getter
@AllArgsConstructor
public enum TransactionType {
    REQUEST("Request", 1),
    SEND("Send", 2);

    private final String description;
    private final Integer id;


    private static final Map<String, Integer> TYPE_IDS =
            Stream.of(TransactionType.values())
            .collect(Collectors.toMap(TransactionType::getDescription, TransactionType::getId));

    public static Integer findIdByDescription(String description){
        return TYPE_IDS.getOrDefault(description.toUpperCase(), -1);
    }

}
