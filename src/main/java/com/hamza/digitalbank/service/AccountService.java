package com.hamza.digitalbank.service;

import com.hamza.digitalbank.domain.Account;
import com.hamza.digitalbank.domain.Transaction;
import com.hamza.digitalbank.domain.TransactionType;
import com.hamza.digitalbank.repository.AccountRepository;
import com.hamza.digitalbank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
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
            System.out.println("Erreur : Montant invalide. Veuillez entrer un nombre .");
        }
    }

    public void transfer(Account userAccount ,String destinataireId,Scanner scanner){
        Optional<Account> accountOptional = accountRepository.findById(destinataireId);

        try {
            if (accountOptional.isPresent()){
                System.out.println("\n--- Effectuer un virement ---");
                System.out.println("Entrer le montant qu'est vous voulez de versé : ");
                BigDecimal montant = new BigDecimal(scanner.nextLine());

                if (userAccount.getBalance().compareTo(montant) < 0){
                    System.out.println("Solde insuffisant pour effectuer ce virement");
                    return;
                } else {
                    Account accountDestinataire = accountOptional.get();

                    accountDestinataire.setBalance(accountDestinataire.getBalance().add(montant));
                    accountRepository.save(accountDestinataire);

                    userAccount.setBalance(userAccount.getBalance().subtract(montant));
                    accountRepository.save(userAccount);

                    Transaction transaction = new Transaction(userAccount.getAccountId(),TransactionType.TRANSFER_OUT,montant,accountDestinataire.getAccountId(),"Faire un virement sur le compte .");
                    Transaction transaction1 = new Transaction(accountDestinataire.getAccountId(),TransactionType.TRANSFER_IN,montant,userAccount.getAccountId(),"reçue un virement sur le compte .");
                    System.out.println("Virement Effectuer avec succès .");
                    System.out.println("Nouveau solde : " + userAccount.getBalance() + " €");
                }
            }
        } catch (NumberFormatException e){
            System.out.println("Erreur : Montant invalide. veuillez entrer un nombre .");
        }
    }

    public void printTransactionHistory(String accountId){
        System.out.println("\n--- Historique des transactions pour le compte " + accountId + " ---");

        List<Transaction> transactions = transactionRepository.findAllByAccountId(accountId);

        if(transactions.isEmpty()){
            System.out.println("Aucun transaction à afficher pour ce compte .");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        transactions.stream().sorted(Comparator.comparing(Transaction::getTimestamp).reversed()).forEach(tx -> {
            String sign = (tx.getType() == TransactionType.DEPOSIT || tx.getType() == TransactionType.TRANSFER_IN) ? "+" : "-";
            System.out.println("--------------------------------------------------");
            System.out.printf("Date       : %s\n", tx.getTimestamp().atZone(java.time.ZoneId.systemDefault()).format(formatter));
            System.out.printf("Type       : %s\n", tx.getType());
            System.out.printf("Montant    : %s%s €\n", sign, tx.getAmount());
            System.out.printf("Description: %s\n", tx.getDescription());
        });
        System.out.println("--------------------------------------------------");
    }
}
