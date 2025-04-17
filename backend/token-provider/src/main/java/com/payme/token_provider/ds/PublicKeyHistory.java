package com.payme.token_provider.ds;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * PublicKeyHistory is a thread-safe data structure that stores a fixed-size history of public keys.
 */
@RequiredArgsConstructor
public class PublicKeyHistory {
    private final int maxSize;

    private Deque<String> keyHistory = new ConcurrentLinkedDeque<>();

    public void addKey(String key){
        if(keyHistory.size() >= maxSize){
            keyHistory.pollFirst();
        }
        keyHistory.addLast(key);
    }

    public List<String> getKeyHistory(){
        return new ArrayList<>(keyHistory);
    }
}