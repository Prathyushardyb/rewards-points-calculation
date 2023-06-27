# Rewards Points Calculation Service is implemented for the given customer purchases during certain period
#The rest API's are implemented to
- create/get customers (customer-controller)
- handle purchases (purchase-controller)
- calculate rewards based on customer Id (rewards-controller)
- TestCases are provided for main business logic (Rewards calculations)
- Integration Test provided to show that HttpStatus handled appropriately and Rest API's are testable
- Spring boot Application uses in memory Data base to store the customers, purchases.
- Exception is thrown if customer, purchase does not exists.
- Rest API's Documentation is provided with swagger UI .

# Setup

- Check out from the Git Repo and import the project in Intellij
- from intellij terminal run the below command 
  ```
  ./gradlew bootRun
  ```
  OR

  Intellij right side menu we can choose these tasks to build

  ![image](https://github.com/Prathyushardyb/rewards-points-calculation/assets/136513365/541ebd9b-8608-41d5-b52a-34310f11d128)

  - Once the build is success, we can Start RewardCalculationServiceApplication(which has main method in it) OR 

  We can directly run the application with below command in command line
```
  rewards-calculation-service\build\libs>java -jar rewards-calculation-service.jar
 ```
- Open the browser and use below URL which allows you to test all controllers and the business logic

```
 http://localhost:8080/swagger-ui/#/
```
- To view the saved entries ,Below is the H2 database console (db url, username ,password can be found in application.properties)
```
http://localhost:8080/h2-console

```

#Sample Run O/p from UI [ http://localhost:8080/swagger-ui/#/ ]-
- Create a customer
  ![image](https://github.com/Prathyushardyb/rewards-calculation/assets/136513365/b71ed6a7-dec3-4f45-86dd-5d4049ff8624)

- Do a Purchase
  ![image](https://github.com/Prathyushardyb/rewards-calculation/assets/136513365/c9497767-a44f-459f-b2ca-000da1eb9c22)

- Check rewards for the customer
  ![image](https://github.com/Prathyushardyb/rewards-calculation/assets/136513365/3e25e710-21f7-45f8-8f4d-b92af529860f)


