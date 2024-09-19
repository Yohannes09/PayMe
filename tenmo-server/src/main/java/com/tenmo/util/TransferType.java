package com.tenmo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TransferType {
    REQUEST("Request", 1),
    SEND("Send", 2);

    private static final Set<Integer> transferTypeIds = Stream.of(TransferType.values())
            .map(TransferType::getTypeId)
            .collect(Collectors.toSet());

    private final String typeDescription;
    private final Integer typeId;

    public Set<Integer> validTransferIds(){
        return transferTypeIds;
    }

    public static boolean validateTypeId(Integer id){
        return transferTypeIds.contains(id);
    }


    public static Set<Integer> getValidTransferTypes(){
        return transferTypeIds;
    }
}
