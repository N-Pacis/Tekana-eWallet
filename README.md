# Tekana eWallet

## I. Running the App

### 1. Technologies used
 - Java: Spring Boot
 - PostgresSQL database
 - Swagger UI

### 2. Clone the Repository

Open your terminal or command prompt and run the following command to clone the repository:
`git clone https://github.com/N-Pacis/Tekana-eWallet.git`

### 3. Create a PostgresSQL Database

Open your PostgresSQL client (e.g., pgAdmin, psql, etc.) and create a new database named `tekana_ewallet`.

### 4. Navigate to the Project Directory

In your terminal or command prompt, navigate to the directory where you cloned the repository.

### 5. Update Database Credentials

1. In the project directory, locate the `src/main/resources/application-dev.properties` file.
2. Open the file in a text editor.
3. Update the database credentials (`spring.datasource.username` and `spring.datasource.password`) according to your PostgresSQL setup.

### 6. Option 1: Using IntelliJ IDEA (Recommended)

If you have IntelliJ IDEA installed:

1. Open the project in IntelliJ IDEA.
2. Wait for the project to load and the dependencies to be resolved.
3. Click the "Run" button to run the application.

### 7. Option 2: Using Maven (If IntelliJ IDEA is not installed)

If you don't have IntelliJ IDEA installed:

1. Ensure that you have Maven installed on your system.
2. In the project directory, open your terminal or command prompt.
3. Run the following command to clean and install the project dependencies: `mvn clean install`
4. After the installation is complete, run the following command to start the application:`mvn spring-boot:run`

### 8. Access the Application

Once the application is running, open your web browser and navigate to `http://localhost:8080/swagger-ui/index.html#/`. You should see the Swagger UI documentation for the application.

### 9. Explore and Use the Application

In the Swagger UI, you can:

- Explore the available endpoints and their documentation.
- Enjoy using the Tekana eWallet application!

## II. Application Flow

### 1. On First Application Run

- On the first run of the application, a default admin account is created with the following credentials:
    - Email: `admin@tekana.rw`
    - Password: `Qwerty@570`

### 2. Admin Roles and Responsibilities

- The admin can log in and create additional admin accounts.
- The primary role of an admin is to:
    - Oversee and monitor transactions.
    - Create wallets for approved customers and provide an initial deposit.
    - Activate or deactivate customer accounts.

### 3. Registration and Approval

1. A customer registers by providing necessary unique personal information.
2. By default, a new customer account is set to a "pending" status after registration.
3. An admin must approve the customer's registration (activate their account) before the customer can log in.

### 4. Wallet Creation and Management

1. After an admin approves a customer's registration, the customer can log in.
2. However, the customer cannot perform transactions until an admin creates a wallet for them with an initial deposit.
3. A customer can have multiple wallets, and each wallet has a unique identifier called a "wallet ID."
4. The wallet ID is used for transactions, and the sender must know the receiver's wallet ID to initiate a transaction.

### 5. Transactions

1. To perform a transaction, the sender must have a sufficient balance in their wallet.
2. During a successful transaction:
    - The money is deducted from the sender's wallet.
    - The money is added to the receiver's wallet.
    - The transaction details are saved in the system.

### 6. Transaction History

- Customers can view the history of their transactions for a specific wallet.

## III. Unique Features

As this is a highly sensitive system that will be dealing with money transactions, it needs to be secure and high-performing. Therefore, we introduced the following features to ensure the system’s security and reliability:

### 1. User Authentication

   - We use the JSON Web Token (JWT) authentication mechanism as it is highly reliable and secure.
   - By default, JWT is stateless, but this poses a challenge in controlling user sessions, which is necessary for sensitive systems. To address this, we added a session ID among the information that is digitally encrypted when creating the JWT.
   - When verifying the JWT before proceeding with the request, we check if the session ID in the token matches the session ID stored in the user's record in the database. If they don't match, we throw a 401 Unauthorized error exception.
   - This mechanism ensures that a user can only be logged in on one device at a time, reducing vulnerabilities introduced by multiple concurrent logins.
   - Additionally, we keep a record of user login history and information such as user-agent and device type to assist us in sending an email to the client about an unrecognized login attempt.
   - Furthermore, if a user account is deactivated or their password is reset, we invalidate their sessions, automatically logging them out.
   - When a user logs out, we invalidate their token to ensure it cannot be used anymore. We achieved this by implementing a sign-out API.

### 2. Action Auditing

   - We maintain an audit log for every action performed by a user in the system.
   - In the audit log, we record information such as the time when the action was performed, the user who performed the action, and a snapshot of the record after being modified by the user.
   - This helps in tracking how records have been altered and identifying the users responsible for the changes.
   - No user is exempt from this action history; every user's actions are tracked.

### 3. Application Performance

   - We added indexes to the tables that we expect to receive high traffic, including the users table and the transactions table.
   - When fetching information from tables with a large number of records, we limit the user to a specific time frame to ensure that the feature is not misused, which could cause application downtime or slowness.
   - For example, when fetching transactions, we limit a user to a time range of one week.

### 4. Accessibility
   - With the help of Spring Boot's internationalization (i18n) and localization capabilities through message resource bundles, we externalized error messages and made them available in five languages: English, French, Kinyarwanda, Swahili, and Spanish.
   - This ensures that the system is usable by people regardless of their location and language preference.
   - With this structure, it is easier to add as many languages as needed.
   - The app also handles all exceptions and lets the client know what went wrong.

## IV. Future Considerations

Currently, we have developed the Minimum Viable Product (MVP) of our application, but there are things we need to consider before the system can go live:

### 1. Secrets and Sensitive Information Management

   - Currently, secrets like database credentials and token signing keys are exposed in the application source code, which is a totally bad practice.
   - Before going live, we need to consider using Kubeseal to seal the application's secrets and store them on the server, and the application can fetch them from there, which then ensures that the secrets are not disclosed in the application.
   - Alternatively, we can store the secrets on the server's environment variables, and the application can use the variable keys to pull the secrets and use them.

### 2. Application Performance Enhancement
  
   - Before going live, we should consider using multiple data sources for our project to ensure smooth app performance in case of high traffic.
   - We should consider using a NoSQL database like MongoDB to handle read operations for high-traffic use cases like getting transactions or getting users.
   - The way this would work is that whenever data is added or updated to our usual database tables in PostgreSQL, we would schedule an asynchronous operation to insert the data into our MongoDB table.
   - This would reduce the cost introduced by joins in an SQL database, hence increasing performance, especially for reading operations.
   - In case admin users need to generate reports of transactions over a longer time range, we should consider using Spring Batch to asynchronously handle report generation and notify the user once the report is available.
   - Using Spring Batch helps us to prevent query timeouts and memory overflow by processing data in chunks and asynchronously.

### 3. Load Balancing, Scaling, and Monitoring

   - We need to dockerize the application and create a Kubernetes cluster on the server to help us with horizontal scaling by creating multiple container nodes for the application.
   - We can utilize Kubernetes to horizontally scale the database replica service by increasing the number of database replicas when the available connections are exhausted on a given replica and the database connection pooling is unable to create any other connection.
   - We need to set up monitoring tools like Grafana or New Relic to help us track the application's health and monitor the application's response time, which is essential if we need to maintain 100% of the application's uptime.

### 4. Integration with Frontend and Tests

   - We need to work hand-in-hand with the frontend team to determine the data they need to display and then return data in respective DTOs instead of the whole Entities.
   - This ensures that there is no data leakage, hence reducing vulnerabilities.
   - We need to conduct integration tests to make sure that the application meets the business requirements and client satisfaction.
   - We need to set up multiple environments: DEV, UAT, and PROD.
   - The DEV environment will be used for internal tests, the UAT environment will be used for testing with third-parties and clients, and also for training. The PROD environment will be our go-live environment after exhaustive tests on DEV and UAT are done.

### 5. Go Live and Training 

  - We need to conduct trainings for admin users and a selected group of customers to ensure that they are familiar with the system before going live.
  - We need to create a dedicated support group available 24/7 for the first 6 months after going live to help clients in case they face any issues.
  - We need to set up email communications that will continue to be a source of communication between clients and our company.
  - We also need to restrict deployment to production and grant push access to a limited number of users to ensure that the environment is not altered easily.
  - We need to setup a CI/CD pipeline to handle automated deployment and also ensure the application's quality by running the tests before the changes are deployed.



### Done With ❤️ By Pacis Nkubito
#### Contact me at `pacisnkubito@gmail.com`
#### Find me on `https://www.linkedin.com/in/pacis-nkubito-986001201/`






