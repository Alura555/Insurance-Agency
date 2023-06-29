# Insurance agency

---
This project is an insurance agency web application.

An insurance agency is an intermediary between insurance companies and clients 
seeking insurance coverage. The main task of the insurance agency is to simplify the process of 
choosing an insurance offer and issuing a policy.
Insurance agencies typically work with multiple insurance companies, offering a range 
of insurance products.

The application supports various user roles:
* Clients view insurance offers and apply for an insurance policy, provide the necessary documents 
for insurance policies. The client can also monitor the status of their applications and issued policies.
* Managers review applications and process them (approve or reject).
* Insurance company managers add, edit and delete insurance offers.
* Administrators can manage all user accounts, roles, document types, insurance types, 
companies, insurance offers.


---

## Key Features:

* User Registration and Authentication.
* Role-Based Access Control.
* Insurance Policy Management.
* Document Management.
* Offer Management.
* Company Management.
* Error Handling.


---

## Technology Stack:

* Spring MVC 
* Spring Boot
* Spring Data
* Spring Security
* Maven
* MySQL
* JUnit 5 
* Mock
* Docker

---

### Getting Started

### Installation and Setup
1. Install Docker on your machine if it is not already installed. 
Visit the Docker website (https://www.docker.com/) and follow the 
installation instructions for your operating system. 

2. Clone the repository to your local machine:

```
git clone https://github.com/Alura555/Insurance-Agency.git
```

3. Navigate to the project directory: 
```
cd Insurance-Agency
```

### Building and Running
1. Build the Docker images using Docker Compose:

```
docker-compose build
```

2. Start the containers:


```
docker-compose up -d
```
3. Once the containers are up and running, 
you can access the application by opening 
a web browser and navigating to `http://localhost:8080`.

### Stopping the Application

To stop the application, run the following command:
```
docker-compose down
```


### Cleanup

If you want to remove the Docker containers and images associated with the application, run the following command:
```
docker-compose down --volumes --rmi all
```
