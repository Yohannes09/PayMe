package com.techelevator.tenmo.services.utils;

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
            .map(TransferType::getTransferTypeId)
            .collect(Collectors.toSet());

    private final String transferTypeDescription;
    private final Integer transferTypeId;

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
