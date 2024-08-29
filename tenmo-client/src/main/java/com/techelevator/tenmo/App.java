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

	}


	private void viewTransferHistory() {
		// TODO Auto-generated method stub

        consoleService.printTransferScreen();

        consoleService.printLine(15, '-');
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub

	}

	private void sendBucks() {
		// TODO Auto-generated method stub

	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
