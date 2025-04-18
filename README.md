# RecipeMaster API

[![Frontend Live](https://img.shields.io/badge/frontend-live-blue)](https://recipemasterdevui-azdjewfuh4h0h2e4.australiacentral-01.azurewebsites.net/)

This is the backend service for the RecipeMaster app, built with Spring Boot, GraphQL, and Azure Cosmos DB.

## 🚀 Features

- GraphQL API using Spring GraphQL
- Reactive access to Azure Cosmos DB
- CRUD support for MealEvent entities used by the RecipeMaster Web frontend

## 🧱 Tech Stack

- Java 17
- Spring Boot 3
- Spring GraphQL
- Azure Cosmos DB (with reactive repository)
- Maven

## ⚙️ Environment Setup

### Prerequisites

- Java 17
- Maven
- Azure Cosmos DB (or local emulator)

### Environment Variables

You may use `application.properties` or `application.yml`:

```
spring.cloud.azure.cosmos.endpoint=<your-endpoint>
spring.cloud.azure.cosmos.key=<your-key>
spring.cloud.azure.cosmos.database=recipemaster
```

## 🧪 Running Locally

If the cosmos endpoint is pointing to a local emulator, please make sure it is up and running before running:

```
mvn spring-boot:run
```

## 🚀 Deployment

This repo is deployed to Azure App Service via GitHub Actions using the dev_recipemasterdevapi.yml workflow.

```
on:
  push:
    branches:
      - dev
```

## 🔗 Related Projects

👉 [RecipeMaster Web (Frontend)](https://github.com/XuSibo99/RecipeMasterWeb)

React + GraphQL app that consumes this API to provide the user interface.
