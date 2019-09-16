# Marketplace

This project shows the implementation of a marketplace in Kotlin using Rx.

## Architecture
The project is divided into different modules following the Clean Architecture approach:

- presentation: Android specific module which includes the UI logic
- domain: Business logic module with no Android specific dependency (pure Kotlin)
- data: Responsible for providing the data required by the application
- remote: Responsible for connecting with API and getting info. I have chosen Retrofit as Http client
- persistence: Responsible for storing and retrieving info. I have chosen Room due to its simplicity and flexibility

## To Do
Due to lack of time certain aspects have not been included:
- Products storage with some cache policy
- Delete cart with outdated products. If you have a cart with outdated products you cannot be able to purchase them
- Instrumentation tests in presentation layer
