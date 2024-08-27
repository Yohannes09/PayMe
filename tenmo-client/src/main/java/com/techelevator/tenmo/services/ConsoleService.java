package com.techelevator.tenmo.services;


import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.service.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleService {

    private static final NumberFormat USD_FOMATTER = NumberFormat.getCurrencyInstance(Locale.US);

    private final Scanner scanner = new Scanner(System.in);
    private final TransferService transferService = new RestTransferService();
    private final UserService userService = new RestUserService();
    private final AccountService accountService = new RestAccountService();
    private final String redAnsii = "\u001B[31m";
    private final String resetAnsii = "\u001B[0m";

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printLine(int length, char symbol){
        int counter = 1;
        while(counter <= length){
            System.out.print(symbol);
            counter++;
        }
        System.out.println();
    }

    public void printTransferScreen(){
        System.out.println("-------------------------------------------");
        System.out.println("Transfers                                  ");
        System.out.println("ID          From/To                  Amount");
        System.out.println("-------------------------------------------");

    }


    //*** Many things below should be in their own class ***
    // console service is only in charge of input and output.


    // horrible name. Meant to print any information regarding an object by calling its toString();
    // hasn't been thought through yet
    public <T> void printAllTypes(T t){

    }

    public boolean sendBucksScreen(int senderUserId){
        Optional<Account> currentUserAccount = accountService.getAccountByUserId(senderUserId);

        while(true){
            try {
                int recipientAccountId = promptForInt("Enter recipient's account ID or username > ");

                if(!accountService.getAccountById(recipientAccountId).isPresent())
                    throw new DaoException("User does not exist");

                double amountTransfered = promptForBigDecimal("Enter amount > ").doubleValue();

                Optional<Transfer> transfer = transferService.processTransfer(
                        currentUserAccount.get().getAccountId(),
                        recipientAccountId,
                        amountTransfered
                );

                // Check if transfer was successful
                return transferService
                        .getTransferById(transfer.get().getTransferId())
                        .isPresent();

            } catch (Exception e) {
                System.out.println("\n" + redAnsii + "Error: " + e.getMessage() + resetAnsii + "\n");
            }
        }

    }

    // repo needs adjustments to only show pending REQUESTS
    public void printTransferHistory(int userId, int transferStatus){
        Optional<Account> account = accountService.getAccountByUserId(userId);

        List<Transfer> transferHistory = transferService.accountTransferHistory(
                account.get().getAccountId())
                .stream()
                .filter(transfer ->
                                transfer.getTransferStatusId() == transferStatus)
                .collect(Collectors.toList());

        System.out.println("\nPENDING REQUESTS:");
        printLine(30, '-');

        transferHistory.forEach(transfer ->
                System.out.println(USD_FOMATTER.format(transfer.getAmount()))
        );
    }

    public void printTransferHistory(int userId){
        Optional<Account> account = accountService.getAccountByUserId(userId);

        List<Transfer> transferHistory = transferService.accountTransferHistory(account.get().getAccountId());
        System.out.println("\nTRANSFER HISTORY:");
        printLine(30, '-');

        transferHistory.forEach(transfer ->
                System.out.println(String.format(
                        "%s%16s",
                        getUserName(transfer.getRecipientAccountId()).orElse("Unkown user. "),
                        USD_FOMATTER.format(transfer.getAmount())
                        )
                )

        );
    }

    public void getAccountBalance(int userId){
        double balance = accountService.getAccountByUserId(userId).get().getBalance();
        System.out.println("\nCURRENT BALANCE:");
        printLine(30,'-');
        System.out.println(USD_FOMATTER.format(balance));
    }

    private Optional<String> getUserName(int accountId){
        int userId = accountService.getAccountById(accountId).get().getUserId();

        return Optional.ofNullable(redAnsii +
                userService.getUserById(userId)
                        .get().getUsername() + resetAnsii
        );
    }

}
