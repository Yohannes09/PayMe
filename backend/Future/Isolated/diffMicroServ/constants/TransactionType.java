package com.payme.authentication.diffMicroServ.constants;

/*  The description for constants should be uppercase for case-insensitive search. */
public enum TransactionType {
    REQUEST("Request"),// 1),
    SEND("Send");//, 2);

    private final String description;
    //private final Integer id;

    TransactionType(String description){//, Integer id){
        this.description = description;
        //this.id = id;
    }

    public String getDescription(){
        return this.description;
    }

//    public Integer getId(){
//        return this.id;
//    }

//    private static final Map<String, Integer> TYPE_IDS =
//            Stream.of(TransactionType.values())
//            .collect(Collectors.toMap(TransactionType::getDescription, TransactionType::getId));

//    public static Integer findIdByDescription(String description){
//        return TYPE_IDS.getOrDefault(description.toUpperCase(), -1);
//    }

}
