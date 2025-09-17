package com.hamza.digitalbank.service;

import com.hamza.digitalbank.domain.Account;
import com.hamza.digitalbank.domain.User;
import com.hamza.digitalbank.repository.AccountRepository;
import com.hamza.digitalbank.repository.UserRepository;

import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserService (UserRepository userRepository,AccountRepository accountRepository){
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public void registerNewUser(Scanner scanner){
        System.out.println("\\n--- Création d'un nouveau compte utilisateur ---");

        System.out.print("Entrez votre nom complet : ");
        String fullName = scanner.nextLine();

        System.out.print("Entrez votre adresse email : ");
        String email = scanner.nextLine();

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()){
            System.out.println("Erreur : Un utilisateur avec cet email existe déjà. Veuillez vous connecter.");
            return;
        }

        System.out.print("Entrez votre addresse postale : ");
        String addresse = scanner.nextLine();

        System.out.print("Choisissez un mot de passe : ");
        String password = scanner.nextLine();

        User newUser = new User(fullName,email,addresse,password);
        userRepository.save(newUser);

        String accountId = "FR" + (100000 + new Random().nextInt(900000));
        Account newAccount = new Account(accountId, newUser.getId());
        accountRepository.save(newAccount);

        System.out.println("\nFélicitations, " + fullName + " ! Votre compte a été créé avec succès.");
        System.out.println("Votre numéro de compte est : " + accountId);
    }

    public Optional<User> login(Scanner scanner){
        System.out.println("\\n--- Connexion ---");

        System.out.println("Entrez votre addresse email : ");
        String email = scanner.nextLine();

        System.out.println("Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()){
            User user = userOptional.get();
            if (user.getPassword().equals(password)){
                System.out.println("\nConnexion réussie ! Bienvenue, " + user.getFullName() + ".");
                return userOptional;
            }
        }

        System.out.println("Erreur : Email ou mot de passe incorrect.");
        return Optional.empty();
    }

    public void updateName(User loggedInUser,String newName){
        loggedInUser.setFullName(newName);

        userRepository.save(loggedInUser);

        System.out.println("Nom mis à jour avec succès !");
    }
}
