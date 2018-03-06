# Bank-account-kata

### A SpringBoot application offering Rest Endpoints in order to interract with a basic bank account 

## Overview:

- The application offers the possibility to have multiple accounts. For each account multiple operations.
- A bank account has a unique identifier. It's represented by a balance and a list of operations which could be a deposit or a withdrawal.
- An operation is categorised(deposit or a withdrawal), have an amount, occurence date and a unique ID. Each operation is linked to a specific account.


## This application offers 4 Endpoints allowing:
 1. Deposit of a given amount of money on an account 
 ```json
 PUT /api/accounts/{accountId}/deposit
 ```
 Payload is as:
 
 ```json
 {
   "amount":2000
  }
 ```
  Response preview:
  ```json
  {
    "balance": 18080000,
    "latestOperations": [
        {
            "id": 967,
            "date": "2018-03-06T13:48:07.776Z",
            "type": "DEPOSIT",
            "amount": 20000
        }
        
        ]
  }
   ```
       
 2. Withdrawal of a given amount of money from an account:
  
 ```json 
 PUT /api/accounts/{accountId}/withdrawal
 ```
 payload is as :
    
 ```json
   {
    "amount":2000
   }
   ```
   Response preview :
    
 ```json
   {
    "balance": 12080000,
    "latestOperations": [
        {
            "id": 970,
            "date": "2018-03-06T13:48:11.754Z",
            "type": "WITHDRAWAL",
            "amount": -2000000
        },
   ]
  ```
  
 3. All operations (debit, credit) listing:
    
   ```json  
   GET /api/accounts/{accountId}/history
   ```
   
   Response preview:
   
   ```json
   [
    {
        "id": 962,
        "date": "2018-03-06T13:46:59.722Z",
        "type": "DEPOSIT",
        "amount": 20000
    },
    {
        "id": 963,
        "date": "2018-03-06T13:47:01.414Z",
        "type": "DEPOSIT",
        "amount": 20000
    }, ....
   ]
   ```
 4. Account balance and last operations (5 most recent ones):
 
   ```json 
    GET /api/accounts/{accountId} 
   ```  
   Response preview:
     
    
    {
                 "balance": 12080000,
                 "latestOperations": [
                    {
                        "id": 970,
                        "date": "2018-03-06T13:48:11.754Z",
                        "type": "WITHDRAWAL",
                        "amount": -2000000
                    },
                   ]
                         
     }
    

 ## Application Endpoints could be accessed using Swagger2 Ui integrated into the Application with API documentation:
 To do so, visit http://localhost:8080/swagger-ui.html once the application in launched.
 
 ## This application is configured to run with a Postgresql Database:
 Setup database name,username and password in the properties.yml file
 
 ## Testing: 
 The application is tested using Junit and assertJ.
 1. Unit tests mainly for the services
 2. Integration test for the restControllers allowing for lightweight end-to-end testing
 
 ## Data initialisation :
 Miration script(liquibase) allows the application to initialise with 4 bank accounts upon start(See changlog.xml file).
