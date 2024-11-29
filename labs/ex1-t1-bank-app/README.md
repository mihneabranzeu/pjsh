# Exercise 1
## Task 1
1. Created `ClassPathXmlApplicationContext` in BankApplication class.
2. Set up `MapClientRepository` and `BankingImpl` in `application-context.xml`.This allows Spring to manage these beans 
and inject dependencies automatically.
    ```xml
    <beans>
        <bean id="repository" class="com.luxoft.bankapp.service.storage.MapClientRepository"/>
        <bean id="banking" class="com.luxoft.bankapp.service.BankingImpl" autowire="byType"/>
    </beans>
    ```
3. Updated the `initialize` method in `BankApplication` to accept an `ApplicationContext` parameter. This allows us to
retrieve the `Banking` bean from the Spring context, which will have its dependencies injected.
4. Replaced the `BankingImpl` object initialization with the Spring context bean retrieval. This ensures that the
`Banking` bean is properly configured by Spring.
5. Ran the tests in `BankApplicationTask1Tests` to ensure everything is working correctly.

## Task 2
1. Added the bean definition for `BankReportServiceImpl` to allow Spring to manage this bean and inject dependencies
automatically.
   ```xml
   <bean id="bankReport" class="com.luxoft.bankapp.service.BankReportServiceImpl" autowire="byType"/>
   ```
2. Updated the `bankReportsDemo` method in `BankApplication` to accept an `ApplicationContext` parameter. 
This allows us to retrieve the `BankReportService` bean from the Spring context, which will have its dependencies injected.
3. Replaced the `BankReportServiceImpl` object initialization with the Spring context bean retrieval.

## Task 3
1. Created and added `test-clients.xml` to the `resources` folder.
2. Added `test-clients.xml` to the `ClassPathXmlApplicationContext`.
3. Imported `application-context.xml` into `test-clients.xml` to reuse the bean definitions.
4. Configured beans for `client1` and `client2` in `test-clients.xml`.
5. Replaced `client_1` and `client_2` object initializations with beans retrieved from the Spring context.
6. Created `clients.properties` file.
7. Configured `PropertyPlaceholderConfigurer` bean.
8. Updated `test-clients.xml` to use placeholders for client names.

# Exercise 2
## Task 1
Moved all dependency configurations form `application-context.xml` to annotations, to achieve annotation-based DI.
1. Enabled annotation processing
    ```xml
    <context:annotation-config/>
    ```
2. Removed `autowire` attribute from `banking` bean.
3. Updated `BankingImpl` class to use `@Autowired` for the `repository` field.