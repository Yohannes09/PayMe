package com.techelevator.tenmo;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.service.RestUserService;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private static final NumberFormat USD_FOMATTER = NumberFormat.getCurrencyInstance(Locale.US);

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final RestClientTransferService clientTransferServce = new RestClientTransferService();
    private final RestClientAccountService clientAccountService = new RestClientAccountService();
    private final RestClientUserService clientUserService = new RestClientUserService();

    private AuthenticatedUser currentUser;
    private Optional<Account> currentAccount;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);

        String token = currentUser.getToken();

        clientTransferServce.setToken(token);
        clientAccountService.setToken(token);
        clientUserService.setToken(token);

        currentAccount = clientAccountService.getAccountByUserId(currentUser.getUser().getId());

        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        BigDecimal currentBalance = BigDecimal.valueOf(currentAccount.get().getBalance());
        System.out.println(USD_FOMATTER.format(currentBalance));
	}



//    -------------------------------------------
//    Transfers
//    ID          From/To                 Amount
//    -------------------------------------------
//    23          From: Bernice          $ 903.14
//    79          To:    Larry           $  12.55
//    ---------
//    Please enter transfer ID to view details (0 to cancel): "

	private void viewTransferHistory() {
		// TODO Auto-generated method stub

        consoleService.printTransferScreen();

//        List<TransferDto> transferHistory = clientTransferServce.getTransferByAccountId(currentAccount.get().getAccountId());
//
//        for(Map.Entry<String, List<TransferDto>> mappedTransfers: mapTransfers(transferHistory).entrySet()){
//
//            for(TransferDto dto: mappedTransfers.getValue()){
//
//                // Check if the current user is the sender in order to print the correct message.
//                String fromOrTo = dto.getSenderAccountId() == currentAccount.get().getAccountId() ? "To" : "From";
//
//                String.format("%d %6s: %s %10f",
//                        dto.getTransferId(),
//                        fromOrTo ,
//                        mappedTransfers.getKey(),
//                        dto.getAmount());
//            }
//
//        }

        consoleService.printLine(15, '-');
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub

        // Transfer status codes - 1:Pending, 2:Approved, 3:Rejected
        List<TransferDto> transfers = clientTransferServce.getTransfersByStatusId(currentAccount.get().getAccountId(), 1);
		consoleService.printTransferHistory(currentUser.getUser().getId(), 1);
	}

	private void sendBucks() {
		// TODO Auto-generated method stub

        boolean successfulTransfer = consoleService.sendBucksScreen(currentUser.getUser().getId());

        if(successfulTransfer)
            System.out.println("Transaction successful. ");
        else
            System.out.println("Could not process your transfer. ");
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}


//    private Map<String , List<TransferDto>> mapTransfers(List<TransferDto> transfers){
//        Map<String, List<TransferDto>> mapTransferByParticipant = new HashMap<>();
//
//        for(TransferDto dto: transfers){
//
//            // Get the ID of the other person involved in the transfer.
//            int transferParticipantId = dto.getSenderAccountId() != currentAccount.get().getAccountId() ?
//                    dto.getRecipientAccountId(): dto.getSenderAccountId();
//
//
//            // stores the user fields for the transferParticipant
//            Optional<User> user = clientUserService.getUserById(
//                    clientAccountService.getAccountById( transferParticipantId).get().getUserId() );
//
//            String participantUsername = user.get().getUsername();
//
//            // Check if the other person is in the map.
//            if( mapTransferByParticipant.containsKey(participantUsername) ){
//                mapTransferByParticipant
//                        .get( participantUsername )
//                        .add(dto);
//            }else{
//                // Check if map contains the other user, if not, give the key a new list then add the dto to the list.
//                mapTransferByParticipant.put( participantUsername, new ArrayList<>() );
//                mapTransferByParticipant
//                        .get(participantUsername)
//                        .add(dto);
//            }
//
//        }
//
//        return mapTransferByParticipant;
//    }


}
