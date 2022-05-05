# Lawcubator-Takehome-Assignment

## Contents

- Introduction
- Features
- Technologies Used
- How to run Project

## Introdution

- Backend Application where Users can work with their Projects
- Users can view other users' projects if they are declared as PUBLIC

## Features

- A User can login or signup into the application
- A User can view all their own projects
- A User can view other users' Public Projects
- A User can view any Project by its Id, if the Id is valid. If the Project belongs to some other user then it needs to be a Public Project
- A User can create, modify and delete a project for himself

- The application uses JWT Authentication and Authorization
- A User will receive a JWT Token upon Signup and Login that he will use to access all the protected resources
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
