package com.hamza.digitalbank;

import com.hamza.digitalbank.repository.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        UserRepository userRepository = new InMemoryUserRepository();
        AccountRepository accountRepository = new InMemoryAccountRepository();
        TransactionRepository transactionRepository = new InMemoryTransactionRepository();

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
                    System.out.println("Fonctionnalité d'inscription à venir...");
                    break;
                case "2" :
                    System.out.println("Fonctionnalité de connexion à venir...");
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
}
