package com.hamza.digitalbank;

import com.hamza.digitalbank.domain.Account;
import com.hamza.digitalbank.domain.User;
import com.hamza.digitalbank.repository.*;
import com.hamza.digitalbank.service.AccountService;
import com.hamza.digitalbank.service.UserService;

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

                    if(loggedInUserOptional.isPresent()){
                        User loggedInUser = loggedInUserOptional.get();
                        showUserMenu(loggedInUser,scanner);
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

    private static void showUserMenu(User loggedInUser, Scanner scanner) {
        Optional<Account> userAccountOptional = accountRepository.findAll().stream().filter(acc -> acc.getOwnerUserId().equals(loggedInUser.getId())).findFirst();

        if (userAccountOptional.isEmpty()){
            System.out.println("Aucun compte bancaire n'est associé a cet utilisateur .");
            return;
        }

        Account userAccount = userAccountOptional.get();
        while (true) {
            System.out.println("\n--- Menu Utilisateur ---");
            System.out.println("Connecté en tant que : " + loggedInUser.getFullName());
            System.out.println("1. Voir mon profile");
            System.out.println("2. changer mon mot de pass");
            System.out.println("3. Consulter mon solde");
            System.out.println("4. Effectuer un dépôt");
            System.out.println("5. Effectuer un retrait");
            System.out.println("6. Effectuer un virement");
            System.out.println("7. Voir l'historique des transactions");
            System.out.println("8. Crée un autre Account");
            System.out.println("9. Se déconnecter");
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
                    return;
                case "3":
                    System.out.println("Compte n° : " + userAccount.getAccountId() + " | solde : " + userAccount.getBalance() + " €");
                    break;
                case "4":
                    System.out.println("compte n° : " + userAccount.getAccountId() + " | solde : " + userAccount.getBalance() + " €");
                    accountService.deposit(userAccount.getAccountId(),scanner);
                    break;
                case "5":
                    System.out.println("compte n° : " + userAccount.getAccountId() + " | solde : " + userAccount.getBalance() + " €");
                    accountService.withdraw(userAccount.getAccountId(),scanner);
                    break;
                case "6":
                    System.out.println("Compte n° : " + userAccount.getAccountId() + " | Solde : " + userAccount.getBalance() + " €");
                    System.out.print("Veuillez saisir le numéro de compte du destinataire : ");
                    String destinataireId = scanner.nextLine();
                    accountService.transfer(userAccount, destinataireId, scanner);
                    break;
                case "7":
                    accountService.printTransactionHistory(userAccount.getAccountId());
                    break;
                case "8":

                case "9":
                    System.out.println("Déconnexion réussie.");
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
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
