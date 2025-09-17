package com.hamza.digitalbank;

import com.hamza.digitalbank.domain.User;
import com.hamza.digitalbank.repository.*;
import com.hamza.digitalbank.service.UserService;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        UserRepository userRepository = new InMemoryUserRepository();
        AccountRepository accountRepository = new InMemoryAccountRepository();
        TransactionRepository transactionRepository = new InMemoryTransactionRepository();

        UserService userService = new UserService(userRepository,accountRepository);

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
                        showUserMenu(loggedInUser,scanner,userService);
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

    private static void showUserMenu(User loggedInUser, Scanner scanner,UserService userService) {
        while (true) {
            System.out.println("\n--- Menu Utilisateur ---");
            System.out.println("Connecté en tant que : " + loggedInUser.getFullName());
            System.out.println("1. Modifier mon profile");
            System.out.println("2. changer mon mot de pass");
            System.out.println("3. Consulter mon solde");
            System.out.println("4. Effectuer un dépôt");
            System.out.println("5. Effectuer un retrait");
            System.out.println("6. Effectuer un virement");
            System.out.println("7. Voir l'historique des transactions");
            System.out.println("8. Se déconnecter");
            System.out.print("Votre choix : ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showMenuUpdateProfil(scanner,userService,loggedInUser);
                    break;
                case "2":
                case "3":
                case "4":
                case "5":
                    System.out.println("Fonctionnalité à venir...");
                    break;
                case "6":
                case "7":
                case "8":
                    System.out.println("Déconnexion réussie.");
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private static void showMenuUpdateProfil(Scanner scanner,UserService userService,User loggedInUser){
        while(true){
            System.out.println("\n--- Menu De Modification ---");
            System.out.println("1. Est-ce-que vous voulez de changer le nom");
            System.out.println("2. Est-ce-que vous voulez de changer l'email");
            System.out.println("3. Est-ce-que vous voulez de changer l'adresse");

            String choice = scanner.nextLine();

            switch (choice){
                case "1":
                    System.out.println("Entrez votre nouveau nom : ");
                    String newName = scanner.nextLine();
                    userService.updateName(loggedInUser,newName);
                    return;
                case "2":
                    System.out.println("Entrez votre nouveau email : ");
                    String newEmail = scanner.nextLine();
                    userService.updateEmail(loggedInUser,newEmail);
                    return;
                case "3":
                    System.out.println("Entrez votre nouveau addresse : ");
                    String newAddresse = scanner.nextLine();
                    userService.updateAddresse(loggedInUser,newAddresse);
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }
}
