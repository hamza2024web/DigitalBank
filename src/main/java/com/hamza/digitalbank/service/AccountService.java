package com.hamza.digitalbank.service;

import com.hamza.digitalbank.domain.Account;
import com.hamza.digitalbank.domain.Transaction;
import com.hamza.digitalbank.domain.TransactionType;
import com.hamza.digitalbank.repository.AccountRepository;
import com.hamza.digitalbank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository , TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void deposit(String accountId, Scanner scanner) {
        System.out.println("\n--- Effectuer un dépôt ---");
        System.out.print("Entrez le montant à déposer : ");

        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Erreur : Le montant du dépôt doit être positif.");
                return;
            }

            Optional<Account> accountOptional = accountRepository.findById(accountId);
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();

                account.setBalance(account.getBalance().add(amount));
                accountRepository.save(account);

                Transaction transaction = new Transaction(accountId, TransactionType.DEPOSIT, amount, null, "Dépôt sur le compte");
                transactionRepository.save(transaction);

                System.out.println("Dépôt de " + amount + " € effectué avec succès.");
                System.out.println("Nouveau solde : " + account.getBalance() + " €");
            } else {
                System.out.println("Erreur : Compte non trouvé.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Montant invalide. Veuillez entrer un nombre.");
        }
    }

    public void withdraw(String accountId, Scanner scanner) {
        System.out.println("\n--- Effectuer un retrait ---");
        System.out.println("Entrez le montant que vous voulez retirer : ");

        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Erreur : Le montant du retrait doit être positif.");
                return;
            }

            Optional<Account> accountOptional = accountRepository.findById(accountId);
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                BigDecimal balance = account.getBalance();

                if (balance.compareTo(amount) < 0) {
                    System.out.println("Erreur : Solde insuffisant pour effectuer ce retrait.");
                    System.out.println("Votre solde actuel : " + balance + " €");
                    return;
                }

                BigDecimal newBalance = balance.subtract(amount);
                account.setBalance(newBalance);
                accountRepository.save(account);

                Transaction transaction = new Transaction(accountId, TransactionType.WITHDRAW, amount, null, "Retrait sur le compte");
                transactionRepository.save(transaction);

                System.out.printf("Retrait de %s € effectué avec succès.%n", amount);
                System.out.println("Nouveau solde : " + newBalance + " €");
            } else {
                System.out.println("Erreur : Compte non trouvé.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Montant invalide. Veuillez entrer un nombre.");
        }
    }
}
