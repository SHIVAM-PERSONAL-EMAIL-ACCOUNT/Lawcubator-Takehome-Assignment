# Project Management Application Backend

## Contents

- Introduction
- Features
- Technologies Used
- How to run Project
- Sample API Endpoints

## Introdution

- Backend Application where Users can work with their Projects
- Users can view other Users' projects if they are declared as PUBLIC

## Features

- A User can login or signup into the application
- A User can view all their own projects
- A User can view other users' PUBLIC Projects
- A User can view any Project by its Id, if the Id is valid. If the Project belongs to some other User then it needs to be a PUBLIC Project
- A User can create, modify and delete a project for himself

- The application uses JWT Authentication and Authorization
- A User will receive a JWT Token upon Signup and Login that he/she will use that to access all the protected resources
- Requests with Invalid Token will not be processed

## Technologies Used

- Spring Boot
- Spring Data JPA
- Spring Security
- Hibernate
- H2 In-Memory Database

## How to run Project

- Install Eclipse, IntelliJ (Ultimate Edition) or any other IDE that supports Web projects
- Clone `main` branch to your local system
- Open the project through your IDE
- Run the project as a Java Application

## Sample API Endpoints

User API

- Signup

```
curl --location --request POST 'localhost:8080/userApi/v1/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "Username 4",
    "password": "Password 4"
}'
```

- Login

```
curl --location --request POST 'localhost:8080/userApi/v1/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "Username 3",
    "password": "Password 3"
}'
```

#### Both the requests will respond with a JSON Web Token that has to be provided in every other request's header 

<br>

Project API

<b>Assumption</b>: The Username provided in any requests' params belongs to the same User whose JSON Web Token is being used

- Add new Project

```
curl --location --request POST 'localhost:8080/projectApi/v1/project/new?user=Username 3' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VybmFtZSAzIiwiZXhwIjoxNjUxODUyNjg0LCJpYXQiOjE2NTE4MTY2ODR9.-nyhq3cHRS5xqu6raMu9UdAn5FtjiyalKrVbXZxMMPE' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name" : "Project 6",
    "description" : "Project description 6",
    "visibility" : "PUBLIC",
    "user" : {
        "id" : 3,
        "username" : "Username 3",
        "password" : "Password 3"
    }
}'
```
- Remove Project by Id

```
curl --location --request DELETE 'localhost:8080/projectApi/v1/project/remove?projectId=5&user=Username 3' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VybmFtZSAzIiwiZXhwIjoxNjUxODUyNjg0LCJpYXQiOjE2NTE4MTY2ODR9.-nyhq3cHRS5xqu6raMu9UdAn5FtjiyalKrVbXZxMMPE'
```

- Modify Project

```
curl --location --request PUT 'localhost:8080/projectApi/v1/project/modify?user=Username 3' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VybmFtZSAzIiwiZXhwIjoxNjUxODUyNjg0LCJpYXQiOjE2NTE4MTY2ODR9.-nyhq3cHRS5xqu6raMu9UdAn5FtjiyalKrVbXZxMMPE' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id" : "5",
    "name" : "Project 5 - Modified",
    "description" : "Project description 5 - Modified",
    "visibility" : "PUBLIC",
    "user" : {
        "id" : 3,
        "username" : "Username 3",
        "password" : "Password 3"
    }
}'
```

- Get Project of Requested User

```
curl --location --request GET 'localhost:8080/projectApi/v1/projects/all?currentUser=Username 3&requestedUser=Username 3' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VybmFtZSAzIiwiZXhwIjoxNjUxODUyNjg0LCJpYXQiOjE2NTE4MTY2ODR9.-nyhq3cHRS5xqu6raMu9UdAn5FtjiyalKrVbXZxMMPE'
```

- Get Project by Id

```
curl --location --request GET 'localhost:8080/projectApi/v1/project?currentUser=Username 3&projectId=6' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VybmFtZSAzIiwiZXhwIjoxNjUxODUyNjg0LCJpYXQiOjE2NTE4MTY2ODR9.-nyhq3cHRS5xqu6raMu9UdAn5FtjiyalKrVbXZxMMPE'
```

- Get Public Projects of All Other Users

```
curl --location --request GET 'localhost:8080/projectApi/v1/other-projects/all?currentUser=Username 3' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VybmFtZSAzIiwiZXhwIjoxNjUxODUyNjg0LCJpYXQiOjE2NTE4MTY2ODR9.-nyhq3cHRS5xqu6raMu9UdAn5FtjiyalKrVbXZxMMPE' \
--data-raw ''
```
