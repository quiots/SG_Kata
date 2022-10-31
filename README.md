# SG_Kata par Steven QUIOT - CARBON
# SGCIB - Bank account kata
Le but de ce kata est de représenter un compte en banque avec des opérations de dépôt et de retrait, ces dernières pouvant être consultées via un relevé de compte formaté et écrit par notre programme.
​
# Sujet - Enoncé original


### <i>Requirements
- Deposit and Withdrawal
- Account statement (date, amount, balance)
- Statement printing
​

The expected result is a service API, and its underlying implementation, that meets the expressed needs.
Nothing more, especially no UI, no persistence.
​
### User Stories
### US 1
In order to save money  
As a bank client  
I want to make a deposit in my account
​
### US 2 
In order to retrieve some or all of my savings  
As a bank client  
I want to make a withdrawal from my account
​
### US 3
In order to check my operations  
As a bank client  
I want to see the history (data, date, amount, balance) of my operations
</i>
# Structure
    
<b>exception</b> : Contient les exceptions métiers liées à un montant négatif et à une balance insuffisante.
- NegativeAmountException
- InsufficientBalanceException
    
<b>operation</b> : 
- <b>data</b> : OperationDAO qui est l'interface d'accès aux données pour nos opérations
- Operation, un record contenant les informations de notre opération bancaire
- OperationType, un enum relatant les différentes opérations possibles, en l'occurrence un dépôt et un retrait.
- OperationService, une classe service assurant les opérations de traitement sur nos données métiers
    
<b>account</b> : 
- AccountStatement, un record spécifiant les éléments propres au relevé de compte de notre exercice

<b>writer</b> : Contient deux interfaces, l'une pour le formatage de notre relevé de compte et l'autre pour l'affichage de ce dernier. Deux implémentations respectives sont présentes; une classe d'implémentation pour formater les données sous forme de tableau et une classe d'implémentation pour afficher le relevé de compte formaté dans la console.
- FormatterStatement
- WriterStatement
- TableFormatterStatement
- ConsoleWriterStatement


Axes d'améliorations : Ajout d'une classe "Amount" permettant de gérer des montants pour la génération d'opérations avec les contraintes métiers adaptées (montant positif et au bon format pour traiter des valeurs monétaires). Affichage alternatif en utilisant PrintStream et non pas directement "System.print.out".

 # Environnement
  
L'exercice fut réalisé en Java 17 et testé sous JUnit 5 & Mockito 4.6.1.
  
Le projet fut produit sous Maven 4.
