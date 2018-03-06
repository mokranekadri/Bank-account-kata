# Bank-account-kata

### A SpringBoot application offering RestEndpoints in order to interract with for a basic bank account 

## This application offers 4 endpoints allowing :
 1. Deposit of a given amount of money of an account 
  PUT /api/accounts/{accountId}/deposit
 
 payload is as :
 
 {
   "amount":2000
  }
  
   Response preview :
  
    "balance": 18080000,
    "latestOperations": [
        {
            "id": 967,
            "date": "2018-03-06T13:48:07.776Z",
            "type": "DEPOSIT",
            "amount": 20000
        },
        
 2. withdrawal of a certain amount from an account
   PUT /api/accounts/{accountId}/withdrawal
   payload is as :
   {
    "amount":2000
   }
   
   Response preview :
   
   {
    "balance": 12080000,
    "latestOperations": [
        {
            "id": 970,
            "date": "2018-03-06T13:48:11.754Z",
            "type": "WITHDRAWAL",
            "amount": -2000000
        },...
   ]
 
  
 3. All operations (debit , credit) listing
    
    GET /api/accounts/{accountId}/history
   
   example of response :
   
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
 4. Account balance and last operations(5 most recent ones)
 
    GET /api/accounts/{accountId}
    
    example of response :
    
     {
    "balance": 12080000,
    "latestOperations": [
        {
            "id": 970,
            "date": "2018-03-06T13:48:11.754Z",
            "type": "WITHDRAWAL",
            "amount": -2000000
        },...
   ]
    
 
 ## Application endpoints could be accessed using Swagger2 Ui integrated to the Application with Apis documentation:
 To do so, visit http://localhost:8080/swagger-ui.html once the application in launched.
 
 ## This application is configured to run with a Postgresql Database:
 Setup database name,username and password in the properties.yml file
 
 ## Testing : 
 the application is tested using Junit and assertJ.
 1.Unit tests mainly for the services
 2.integration test for the restControllers allowing some kind of end to end testing
 
