package com.hamza.digitalbank;

import com.hamza.digitalbank.domain.Account;
import com.hamza.digitalbank.domain.User;
import com.hamza.digitalbank.repository.*;
import com.hamza.digitalbank.service.AccountService;
import com.hamza.digitalbank.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final UserRepository userRepository = new InMemoryUserRepository();
    private static final AccountRepository accountRepository = new InMemoryAccountRepository();
    private static final TransactionRepository transactionRepository = new InMemoryTransactionRepository();

    private static final UserService userService = new UserService(userRepository,accountRepository);
    private static final AccountService accountService = new AccountService(accountRepository,transactionRepository);

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);

        System.out.println("=============================================");
        System.out.println("    Bienvenue sur la Digital Bank By Hamza   ");
        System.out.println("=============================================");

        while(true){
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Créer un nouveau compte (S'inscrire)");
            System.out.println("2. Se connecter");
            System.out.println("3. Quitter");
            System.out.print("Votre choix : ");

            String choice = scanner.nextLine();

            switch (choice){
                case "1" :
                    userService.registerNewUser(scanner);
                    break;
                case "2" :
                    Optional<User> loggedInUserOptional = userService.login(scanner);

                    if (loggedInUserOptional.isPresent()) {
                        User loggedInUser = loggedInUserOptional.get();

                        List<Account> userAccounts = accountRepository.findAllByOwnerUserId(loggedInUser.getId());

                        if (userAccounts.isEmpty()) {
                            System.out.println("Vous n'avez aucun compte bancaire.");
                            break;
                        }

                        boolean hasActiveAccount = userAccounts.stream().anyMatch(Account::isActive);

                        if (!hasActiveAccount) {
                            System.out.println("Tous vos comptes sont désactivés. Vous ne pouvez pas vous connecter.");
                            break;
                        }

                        showUserMenu(loggedInUser, scanner);
                    }
                    break;

                case "3":
                    System.out.println("Merci d'avoir utilisé nos services. À bientôt !");
                    scanner.close();
                    return;
                default:
                    System.out.println("choix invalide. veuillez réessayer.");
                    break;
            }
        }
    }

    private static void showUserMenu(User loggedInUser, Scanner scanner){
        while(true ){
            System.out.println("\n--- Menu Utilisateur : " + loggedInUser.getFullName() + " ---");

            List<Account> userAccounts = accountRepository.findAllByOwnerUserId(loggedInUser.getId());

            System.out.println("Vos Compte Bancaire :");
            if (userAccounts.isEmpty()){
                System.out.println("Vous n'avez actuellement aucun compte bancaire.");
            } else {
                for (int i = 0 ; i < userAccounts.size() ; i++){
                    Account acc = userAccounts.get(i);
                    System.out.printf("%d. compte n° %s | Solde : %s €\n", i+1 , acc.getAccountId(), acc.getBalance());
                }
            }

            System.out.println("--------------------------------------------------");
            System.out.println("1. Gérer un compte (Dépôt, Retrait, Virement...) .");
            System.out.println("2. Créer un nouveau compte bancaire .");
            System.out.println("3. lister mes compte bancaire .");
            System.out.println("4. Blocker un compte bancaire .");
            System.out.println("5. Se déconnecter");
            System.out.print("Votre choix : ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "1":
                    if (userAccounts.isEmpty()){
                        System.out.println("Vous devez d'abord crée un compte .");
                        break;
                    }
                    manageAccount(loggedInUser, (List<Account>) userAccounts, scanner);
                    break;
                case "2":
                    userService.createNewAccountForUser(loggedInUser);
                    break;
                case "3":
                    accountService.listAccounts(loggedInUser);
                    break;
                case "4":
                    System.out.println("Veuillez entrer le numéro de compte bancaire :");
                    String numberAccount = scanner.nextLine();
                    accountService.blockAccount(loggedInUser,numberAccount);
                    System.out.println("Merci d'avoir utilisé nos services. À bientôt !");
                    return;
                case "5":
                    System.out.println("Déconnexion réussie. ");
                    return;
                default:
                    System.out.println("Choix Invalide. veuillez réessayer.");
            }
        }
    }

    private static void manageAccount(User loggedInUser,List<Account> accounts ,Scanner scanner) {
        System.out.println("Entrez le numéro de compte à gérer ");
        try {
            int accountIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (accountIndex < 0 || accountIndex >= accounts.size()){
                System.out.println("Numéro de compte invalide. ");
                return;
            }
            Account selectedAccount = accounts.get(accountIndex);

            while (true) {
                selectedAccount = accountRepository.findById(selectedAccount.getAccountId()).get();
                System.out.println("\n--- Menu Utilisateur ---");
                System.out.println("Connecté en tant que : " + loggedInUser.getFullName());
                System.out.println("1. Voir mon profile");
                System.out.println("2. changer mon mot de pass");
                System.out.println("3. Consulter mon solde");
                System.out.println("4. Effectuer un dépôt");
                System.out.println("5. Effectuer un retrait");
                System.out.println("6. Effectuer un virement");
                System.out.println("7. Voir l'historique des transactions");
                System.out.println("8. Revenir au menu précédent");
                System.out.print("Votre choix : ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        String fullName = loggedInUser.getFullName();
                        System.out.println("Nom : " + fullName);

                        String email = loggedInUser.getEmail();
                        System.out.println("Email : " + email);

                        String adresse = loggedInUser.getAdresse();
                        System.out.println("Adresse : " + adresse);

                        showMenuUpdateProfil(scanner,loggedInUser);

                        break;
                    case "2":
                        System.out.println("Veuillez saiser l'ancien mot de pass");
                        String oldPassword = scanner.nextLine();
                        System.out.println("veuillez saiser le nouveau mot de pass");
                        String newPassword = scanner.nextLine();
                        userService.UpdatePassword(oldPassword,newPassword,loggedInUser);
                        break;
                    case "3":
                        System.out.println("Compte n° : " + selectedAccount.getAccountId() + " | solde : " + selectedAccount.getBalance() + " €");
                        break;
                    case "4":
                        System.out.println("compte n° : " + selectedAccount.getAccountId() + " | solde : " + selectedAccount.getBalance() + " €");
                        accountService.deposit(selectedAccount.getAccountId(),scanner);
                        break;
                    case "5":
                        System.out.println("compte n° : " + selectedAccount.getAccountId() + " | solde : " + selectedAccount.getBalance() + " €");
                        accountService.withdraw(selectedAccount.getAccountId(),scanner);
                        break;
                    case "6":
                        System.out.println("Compte n° : " + selectedAccount.getAccountId() + " | Solde : " + selectedAccount.getBalance() + " €");
                        System.out.print("Veuillez saisir le numéro de compte du destinataire : ");
                        String destinataireId = scanner.nextLine();
                        accountService.transfer(selectedAccount, destinataireId, scanner);
                        break;
                    case "7":
                        accountService.printTransactionHistory(selectedAccount.getAccountId());
                        break;
                    case "8":
                        return;
                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                        break;
                }
            }
        } catch (NumberFormatException e){
            System.out.println("Entrée invalide. Veuillez entrer un nombre. ");
        }

    }

    private static void showMenuUpdateProfil(Scanner scanner, User loggedInUser){
        while(true){
            System.out.println("\n--- Menu De Modification ---");
            System.out.println("1. Est-ce-que vous voulez de changer le nom");
            System.out.println("2. Est-ce-que vous voulez de changer l'email");
            System.out.println("3. Est-ce-que vous voulez de changer l'adresse");
            System.out.println("4. Exit .");
            String choice = scanner.nextLine();

            switch (choice){
                case "1":
                    System.out.println("Entrez votre nouveau nom : ");
                    String newName = scanner.nextLine();
                    Main.userService.updateName(loggedInUser,newName);
                    return;
                case "2":
                    System.out.println("Entrez votre nouveau email : ");
                    String newEmail = scanner.nextLine();
                    Main.userService.updateEmail(loggedInUser,newEmail);
                    return;
                case "3":
                    System.out.println("Entrez votre nouveau addresse : ");
                    String newAddresse = scanner.nextLine();
                    Main.userService.updateAddresse(loggedInUser,newAddresse);
                    return;
                case "4":
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }
}
