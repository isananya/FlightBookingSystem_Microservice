# **Flight Booking Microservices System**

This project is an implementation of a flight booking platform built on a microservices architecture. It creates a separation of concerns by isolating User management, Flight Inventory and Booking Operations into independent units. 

## **Architecture Diagram**

<img width="1375" height="574" alt="image" src="https://github.com/user-attachments/assets/64ae6a44-451a-4aad-8a92-b4def23ff003" />

## **Overview**

The system consists of the following services:

### **1. User Service**

* Handles user signin and login as admin and users
* Generates JWT Tokens
* Cookie based authentication

### **2. Flight Service**

* Manages flights and schedules
* Provides flight availability and search
* Updates seat count after booking (via Feign call)

### **3. Booking Service**

* Handles ticket booking and cancellation
* Generates PNR numbers
* Retrieves tickets using PNR or email
* Cancels booking using PNR
* Sends emails via RabbitMQ
* Uses circuit breaker

### **4. Email Service**

* Listens to RabbitMQ queue
* Sends booking/cancellation email notifications to users
* Runs independently
<img width="1280" height="383" alt="image" src="https://github.com/user-attachments/assets/579dcdae-8d51-429d-b64f-f8ccc2302248" />

### **5. API Gateway**

* Single entry point for routing all client requests
* Provides centralized routing and cross-cutting control

### **6. Eureka Server**

* Service registry for dynamic discovery
* Removes hardcoded URLs
![WhatsApp Image 2025-12-09 at 00 14 34_5c2a388f](https://github.com/user-attachments/assets/f15aeb1b-c779-4e9e-bc99-521323f82d06)

### **7. Config Server**

* Hosts all configuration files
* Each microservice fetches config on startup

### **8. RabbitMQ**

* Broker for sending asynchronous booking/cancellation email events
<img width="1280" height="629" alt="image" src="https://github.com/user-attachments/assets/7aacb0a2-7fe8-4ba0-9f12-b8c98ad04b26" />

### **9. Docker**

Used for containerizing:
* RabbitMQ
* MySQL instances per microservice
* dockerizing all services
<img width="1575" height="257" alt="image" src="https://github.com/user-attachments/assets/e0d0eb05-3807-4b3c-b16c-435565d0d7f6" />

<img width="1603" height="739" alt="image" src="https://github.com/user-attachments/assets/7ecfbd33-8ab6-4abb-9e6e-816840e13277" />

## **ER Diagram**

<img width="508" height="349" alt="ER Diagram" src="https://github.com/user-attachments/assets/56be8435-c579-4008-a63b-714d37c7be02" />

# **Tech Stack**

* Java 17
* Spring Boot 4.0.0
* Eureka Server
* Spring Cloud
* JWT + BCrypt + Cookie Session
* OpenFeign
* Resilience4j Circuit Breaker
* MySQL
* Maven
* RabbitMQ
* JMeter
* SonarQube
* Docker

# **Endpoints**

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/signup | Registers a new user. |
| POST | /auth/login | Authenticate user, generate JWT cookie |
| POST | /schedule/route | Adds a new flight route. Requires : `JWT Admin`. |
| POST | /schedule/inventory | Adds a new flight schedule. Requires : `JWT Admin`. |
| GET | /flights/search | Searches flights (one-way or round-trip). |
| POST | /booking | Creates a new booking and generates PNR. |
| DELETE | /booking/cancel/{pnr} | Cancels a booking if allowed. |
| GET | /ticket/{pnr} | Retrieves all tickets for a given PNR. |
| GET | /booking/history/{emailId} | Retrieves all tickets for a given email. |

# **Installation & Setup**

1. Clone the Repository

```sh
git clone https://github.com/isananya/FlightBookingSystem_Microservice.git
```
2. cd into the main directory

3. Make sure the docker engine is running and then run this command

```sh
docker-compose up --build
```


# **SonarQube Analysis**

![WhatsApp Image 2025-12-02 at 06 25 34_e264718b](https://github.com/user-attachments/assets/6b51dbab-6ecc-433d-8999-254779d35962)

