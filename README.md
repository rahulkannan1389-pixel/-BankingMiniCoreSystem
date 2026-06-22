
### Banking Mini Core System
 Spring Boot–based mini banking application that simulates real-world banking operations such as customer management, account handling, fund transactions, and reporting.
## REST API DOCUMENTATION
##  Base URL
http://localhost:7070/api
## Customer APIs

| Method | Endpoint |
|--------|----------|
| POST | `/customers` |
| GET | `/customers/{id}` |
| GET | `/customers` |
| PUT | `/customers/{id}` |
| PUT | `/customers/{id}/kyc` |
| DELETE | `/customers/{id}` |
##  Account APIs

| Method | Endpoint |
|--------|----------|
| POST | `/accounts` |
| GET | `/accounts/{id}` |
| GET | `/accounts/customer/{customerId}` |
| PUT | `/accounts/{id}` |
| DELETE | `/accounts/{id}` |

##  Transaction APIs
| Method | Endpoint |
|--------|----------|
| POST | `/transactions/deposit` |
| POST | `/transactions/withdraw` |
| POST | `/transactions/transfer` |
| GET | `/transactions/{accountId}` |

##  Reporting API
| Method | Endpoint |
|--------|----------|
| GET | `/reports/statement/{accountId}` |

## Swagger UI
http://localhost:7070/swagger-ui/index.html

#  DATABASE SCHEMA
##  Entities
## Customer
- id (Primary Key)
- firstName
- lastName
- email (Unique)
- phone
- aadharNumber
- customerType
- kycVerified
- isActive
## Account
- id (Primary Key)
- accountNumber
- customerId (Foreign Key)
- accountType
- balance
- overdraftLimit
- accountStatus
 ## Transaction
- id (Primary Key)
- accountId (Foreign Key)
- transactionType
- amount
- balanceAfterTransaction
- referenceNumber (Unique)
- transactionDate

## Entity Relationships
- One Customer can have multiple Accounts  
- One Account can have multiple Transactions

Customer (1) → (Many) Account  
Account (1) → (Many) Transaction

## SERVICE-LAYER UNIT TEST CASES
##  CustomerService
- Create Customer
- Duplicate Email validation
- Update Customer
- Get Customer by ID
##  AccountService
- Create Account
- Get Account by ID
- Close Account
- 
##  TransactionService
- Deposit
- Withdraw
- Fund Transfer
- Insufficient Balance handling
- Invalid Amount validation
- Inactive Account validation
  
## SAMPLE TEST DATA
### Customer
INSERT INTO CUSTOMER VALUES 
(1, 'Rahul', 'K', 'rahul@example.com', '9876543210', '123456789012', 'INDIVIDUAL', false, true);

### Account
INSERT INTO ACCOUNT VALUES 
(1, 'ACC001', 1, 'SAVINGS', 5000, 1000, 'ACTIVE');

### Transaction
INSERT INTO TRANSACTION VALUES 
(1, 1, 'DEPOSIT', 5000, 5000, 'TXN001', CURRENT_TIMESTAMP);

## Configure Database
- spring.datasource.url=jdbc:mysql://localhost:3306/banking
- spring.datasource.username=root
- spring.datasource.password=root
- spring.jpa.hibernate.ddl-auto=update

##  Transfer Flow
- The transfer operation follows a layered architecture where the request flows through controller, service, repository, and database layers.
- Client → Controller → Service → Repository → Database → Response
# Detailed Flow:
- Client initiates transfer API request  
- Controller receives and validates request  
- Service layer processes business logic  
- Repository layer fetches and updates data with locking  
- Transaction records are stored  
- Response returned to client 











