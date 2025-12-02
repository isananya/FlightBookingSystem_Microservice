# **Flight Booking Microservices System**

This project is an implementation of a flight booking platform built on a microservices architecture. It creates a separation of concerns by isolating Flight Inventory and Booking Operations into independent units. Key infrastructure components include:

* API Gateway: A single entry point for all client requests.
* Eureka Server: For dynamic service registration and discovery.
* Config Server: For externalized, central management of application properties.
* OpenFeign: To facilitate seamless communication between the microservices.

## **Overview**

The system consists of the following services:

### **1. Flight Service**

* Manages flights and flight schedules
* Provides flight availability and search
* Updates seat count after booking (via Feign call)

### **2. Booking Service**

* Handles ticket booking and cancellation
* Generates PNR numbers
* Retrieves tickets using PNR or email
* Cancels booking using PNR

### **3. API Gateway**

* Single entry point for routing all client requests
* Provides centralized routing and cross-cutting control

### **4. Eureka Server**

* Service registry for dynamic discovery
* Removes hardcoded URLs

### **5. Config Server**

* Hosts all configuration files
* Each microservice fetches config on startup

## **ER Diagram**

<img width="508" height="349" alt="ER Diagram" src="https://github.com/user-attachments/assets/56be8435-c579-4008-a63b-714d37c7be02" />

# **Tech Stack**

* Java 17
* Spring Boot 4.0.0
* Spring Cloud
* MySQL
* Maven
* JMeter
* SonarQube

# **Installation & Setup**

1. Clone the Repository

```sh
git clone https://github.com/isananya/FlightBookingSystem_Microservice.git
```
2. cd into the main directory and make 5 tabs

3. Run the following codes in different tabs in order :

* Config Server

```sh
cd config-server
mvn clean install
mvn spring-boot:run
```
* Eureka Server

```sh
cd eureka-server
mvn clean install
mvn spring-boot:run
```

* Flight Service

```sh
cd flight-service
mvn clean install
mvn spring-boot:run
```

* Booking Service

```sh
cd booking-service
mvn clean install
mvn spring-boot:run
```

* API Gateway

```sh
cd api-gateway
mvn clean install
mvn spring-boot:run
```

# **SonarQube Analysis**

![WhatsApp Image 2025-12-02 at 06 25 34_e264718b](https://github.com/user-attachments/assets/6b51dbab-6ecc-433d-8999-254779d35962)

