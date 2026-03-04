# Travel Planner

A travel planning application with a Spring Boot backend API to manage trips, destinations, and users. Supports
authentication, trip creation, and destination management for personal travel planning.

## Features

- Stateless user registration and login with JWT authentication using Spring Security
- Manage user trips with multiple destinations
- Data persistence with PostgreSQL
- Input validation and structured API error responses
- Unit tests with JUnit 5 and Mockito
- CI/CD pipeline with GitHub Actions

## Tech Stack

- Java 25, Spring Boot 4
- PostgreSQL
- JUnit 5, Mockito
- GitHub Actions (CI/CD)
- Docker (for local development)
- Maven

## Prerequisites

- Java 25
- Maven 4.x
- Docker & Docker Compose
- PostgreSQL

## Getting Started
Follow these steps to run the Travel Planner backend locally:
1. Clone the repo:
   ```
   git clone git@github.com:gabytzulos/travel-planner.git
   ```
2. Create a `.env` file from the template `.env.template` and configure the environment variables for the PostgreSQL database
3. Configure the properties in `application.yml`
4. Build and run the project:
   ```
   cd backend             # move into backend directory
   docker compose up -d   # start the PostgreSQL container
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

## Backend endpoints

### User authentication

<details> 
<summary> POST /auth/register </summary> 

Request body

```json
{
  "email": "email@example.com",
  "password": "password123",
  "firstName": "FirstName",
  "lastName": "LastName"
}
```

Request response

```json
{
  "id": "748a7122-71ab-4f16-b5c3-590c0e5ed54d",
  "email": "email@example.com",
  "firstName": "FirstName",
  "lastName": "LastName"
}
```

</details>

<details> 
<summary> POST /auth/register with invalid fields </summary> 

Request body

```json
{
  "email": "invalid email",
  "password": "short",
  "firstName": "             ",
  "lastName": "LastName"
}
```

Request response

```json
{
  "firstName": "First name is required",
  "password": "Password must be at least 8 characters long",
  "email": "must be a well-formed email address"
}
```

</details>


<details> 
<summary> POST /auth/login </summary> 

Request body

```json
{
  "email": "email@example.com",
  "password": "password123"
}
```

Request response

```json
{
  "id": "748a7122-71ab-4f16-b5c3-590c0e5ed54d",
  "email": "email@example.com",
  "firstName": "FirstName",
  "lastName": "LastName"
}
```

> The server sets a Jwt as a Secure, HttpOnly cookie with SameSite=Lax. No token is returned in the response body.

</details>

### Trip management

> All `/trip` endpoints require authentication.

<details> 
<summary> POST /trip </summary> 

Request body

```json
{
  "title": "France 2026",
  "startDate": "2026-06-10",
  "endDate": "2026-06-20",
  "destinations": [
    {
      "city": "Paris",
      "country": "France"
    },
    {
      "city": "Colmar",
      "country": "France"
    }
  ],
  "notes": "Summer trip!"
}
```

Request response

```json
{
  "id": "58e30f00-c72b-4834-bf36-41bd724302b6",
  "userId": "748a7122-71ab-4f16-b5c3-590c0e5ed54d",
  "title": "France 2026",
  "startDate": "2026-06-10",
  "endDate": "2026-06-20",
  "notes": "Summer trip!",
  "destinations": [
    {
      "city": "paris",
      "country": "france"
    },
    {
      "city": "colmar",
      "country": "france"
    }
  ]
}
```

</details>

<details> 
<summary> POST /trip with invalid fields </summary> 

Request body

```json
{
  "title": "     ",
  "startDate": "2026-06-10",
  "endDate": "",
  "destinations": [],
  "notes": "Nothing for now"
}
```

Request response

```json
{
  "endDate": "End date is required",
  "destinations": "At least one destination is required",
  "title": "Title is required"
}
```

</details>


<details> 
<summary> GET /trip </summary> 

> Returns all trips for the authenticated user

Request response

```json
[
  {
    "id": "58e30f00-c72b-4834-bf36-41bd724302b6",
    "userId": "748a7122-71ab-4f16-b5c3-590c0e5ed54d",
    "title": "France 2026",
    "startDate": "2026-06-10",
    "endDate": "2026-06-20",
    "notes": "Summer trip!",
    "destinations": [
      {
        "city": "paris",
        "country": "france"
      },
      {
        "city": "colmar",
        "country": "france"
      }
    ]
  },
  {
    "id": "52c3d59b-7fa5-41a9-8102-ebfc9a08be85",
    "userId": "748a7122-71ab-4f16-b5c3-590c0e5ed54d",
    "title": "England 2026",
    "startDate": "2026-09-10",
    "endDate": "2026-09-20",
    "notes": "Nothing for now",
    "destinations": [
      {
        "city": "york",
        "country": "england"
      },
      {
        "city": "leeds",
        "country": "england"
      }
    ]
  }
]
```

</details>

<details> 
<summary> GET /trip/{id} </summary> 

Request response

```json
{
  "id": "58e30f00-c72b-4834-bf36-41bd724302b6",
  "userId": "748a7122-71ab-4f16-b5c3-590c0e5ed54d",
  "title": "France 2026",
  "startDate": "2026-06-10",
  "endDate": "2026-06-20",
  "notes": "Summer trip!",
  "destinations": [
    {
      "city": "paris",
      "country": "france"
    },
    {
      "city": "colmar",
      "country": "france"
    }
  ]
}
```

</details>

<details> 
<summary> DELETE /trip/{id} </summary> 

Request response

```json
{
    "message": "Trip '<UUID>' deleted successfully."
}
```

</details>


---
