# Trade Store Strategy and Code Flow

## Overview
The `TradeService` and its associated test cases are designed to handle the validation, storage, and management of trade data. The primary goal is to ensure that trades adhere to specific business rules, such as rejecting trades with lower versions or past maturity dates, while allowing updates to existing trades with the same version.

This document outlines the strategy behind the implementation, the decisions made, and the overall code flow.

---

## Key Business Rules
1. **Reject Lower Version Trades**:
   - Trades with a version lower than the existing trade in the repository are rejected.
   - This ensures that only the latest version of a trade is stored.
   

2. **Reject Trades with Past Maturity Dates**:
   - Trades with a maturity date earlier than the current date are considered invalid and are rejected.
   - This prevents storing expired or outdated trades.

3. **Replace Trades with the Same Version**:
   - Trades with the same version as an existing trade are allowed but replace the existing trade.
   - This ensures that updates to trades are reflected in the repository.

4. **Mark Expired Trades**:
   - Trades with a maturity date in the past are automatically marked as expired.
   - This is handled as part of a scheduled job or batch process.

---

## Code Flow
### 1. **Trade Validation**
   - The `validateTrade` method in `TradeService` is responsible for enforcing business rules.
   - It checks:
     - If the trade version is lower than the existing trade.
     - If the maturity date is in the past.
   - If any rule is violated, a `TradeException` is thrown with an appropriate message.

### 2. **Trade Storage**
   - The `saveTrade` method in `TradeService` handles the storage of trades.
   - It first validates the trade using `validateTrade`.
   - If the trade passes validation:
     - It is saved to the SQL repository (`tradeSqlRepository`).
     - It is also saved to the MongoDB repository (`tradeMongoRepository`) for redundancy.

### 3. **Marking Expired Trades**
   - The `markExpiredTrades` method retrieves all trades from the repository.
   - Trades with a maturity date in the past are marked as expired (`expired = "Y"`) and updated in the repository.

---

## Testing Strategy
### 1. **Unit Tests**
   - Each business rule is tested independently using JUnit and Mockito.
   - Mock repositories (`tradeSqlRepository` and `tradeMongoRepository`) are used to simulate database interactions.

### 2. **Test Cases**
   - **`testRejectLowerVersionTrade`**:
     - Verifies that trades with a lower version are rejected.
     - Ensures that a `TradeException` is thrown with the correct message.
   - **`testRejectPastMaturityDateTrade`**:
     - Verifies that trades with a past maturity date are rejected.
     - Ensures that a `TradeException` is thrown with the correct message.
   - **`testReplaceSameVersionTrade`**:
     - Verifies that trades with the same version replace the existing trade.
     - Ensures that the updated trade is saved to both repositories.

Note: There are testcases for Kafka Producer service and expiredJobScheduler but marked disable due to mocking challenge
---

## Design Decisions
1. **Separation of Concerns**:
   - Validation logic is encapsulated in the `validateTrade` method.
   - Storage logic is handled by the `saveTrade` method.
   - Expiration logic is handled by the `markExpiredTrades` method.

2. **Use of Exceptions**:
   - `TradeException` is used to signal validation failures, making the code more readable and maintainable.

3. **Mocking in Tests**:
   - Mocking repositories ensures that tests are isolated and do not depend on actual database interactions.

4. **Dual Repository Storage**:
   - Trades are stored in both SQL and MongoDB repositories to ensure data redundancy and support different querying needs.

---
## Pipeline
1. **CI.yml**
   - Runs as Github Action on main, master branch commit and PRs
   - Builds and Tests the code
   - stages are Build, test, OWASP Vulnerability check
2. **Deploy.yml**
   - FakeDeploy using a self-hosted container on Github
   - Runs as Github Action on PR
3. **Dependabot.yml**
   - Weekly dependabot run on Github
   
## Future Enhancements
1. **Add Integration Tests**:
   - Test the interaction between `TradeService` and the actual repositories.
2. **Enhance Exception Messages**:
   - Include more details in exception messages, such as trade IDs and validation rules.

---
