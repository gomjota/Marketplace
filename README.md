# Marketplace

This project shows the implementation of a marketplace in Kotlin using Rx. There are some particularities:

- If you buy more than two "Copper" products, a two-for-one offer applies
- If you purchase 3 or more "Commander 2" products, a volume offer applies

## Architecture
The project is divided into different modules following the Clean Architecture approach, each one with its tests:

- presentation: Android specific module which includes the UI logic
- domain: Business logic module with no Android specific dependency (pure Kotlin)
- data: Responsible for providing the data required by the application
- remote: Responsible for connecting with API and getting info. I have chosen Retrofit as Http client
- persistence: Responsible for storing and retrieving info. I have chosen Room due to its simplicity and flexibility

## To Do
Due to lack of time certain aspects have not been included:
- Products storage with some cache policy
- Delete cart with outdated products. If you have a cart with outdated products you cannot be able to purchase them
- Move to coroutines (in progress)

## Technical questions
- How long did you spend on the coding test? About 20h from scratch. Although, it is a project that I am iterating and improving little by little
- What would you add to your solution if you had more time? All included in previous To Do section
- If an offline mode it's required for this app, how will you implemet it? The unique call to network is when the app needs to download the products. Storing products with a cache policy when they are received solves this problem
- How would you track down a performance issue in production? Have you ever had to do this? Logging and tracking events adapting external libs such as Google Analytics, Facebook Analytcs... Plus I've used libs like "FlowUp" from Karumi, that help a lot to know what happens in production environments.
- How would you ask for a "dangerous" permission to the user, for example, if location is required? It's important asking for permissions just when the user is going to use the functionality related. The user has to know why the permission we are asking for is a must if he wants to use the app.
- Could you list the five S.O.L.I.D principles? Single responsability, Open/Closed, Liskov substitution, Interface segregation, Dependency inversion