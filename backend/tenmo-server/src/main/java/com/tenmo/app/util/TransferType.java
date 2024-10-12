package com.tenmo.app.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*  The description for constants should be uppercase for case-insensitive search. */
@Getter
@AllArgsConstructor
public enum TransferType {
    REQUEST("Request", 1),
    SEND("Send", 2);

    private final String description;
    private final Integer id;


    private static final Map<String, Integer> TYPE_IDS =
            Stream.of(TransferType.values())
            .collect(Collectors.toMap(TransferType::getDescription, TransferType::getId));

    public static Integer findIdByDescription(String description){
        return TYPE_IDS.getOrDefault(description.toUpperCase(), -1);
    }

}
