# Order Management System (OMS) Web Service

## How to Run the App using Docker
* Run git clone like so: https://github.com/paultofunmi/oms-app.git
* cd into OMS directory and run: docker-compose up -d
* Check that the app is running: docker ps
* Test the app http://0.0.0.0:80/api/swagger-ui.html#!

### Proposed Authentication
My proposed authentication method is JWT. I would equally state that HTTPS protocal should be used (and should be the starting point) instead of HTTP protocol. 
Using HTTPS ensures that there is degree of secure channel of communication between the server and its clients. It also prevent sniffing and ensures confidentiality of data exchanged.

#### My justification for using JWT (JSON Web Tokens)
JWT has numerous benefits when compared to other alternatives such as Basic Authentication and server-side sessions for the following reasons:
* There is less pressure on the database because an app does not have to constantly store & retrieve session id & data for each request.
* Ease of Scalability: services using JWT can be easily scaled as the servers do not maintain sessions for each user. This can have a big hit on servers when there are many sessions.
* Self-contained JWTs have claims containing user role, what the user can access, issue and expiry date of the JWT. As a result, resource servers with public key or secret keys can decode JWT to read these claims without hitting the database.
* Easy of use across load-balancers. When we have multiple API servers, there is no need to configure server to route session to the same server or share session data because a request with JWT can be authenticated and authorized without hitting the same server or sharing session across redundant services.
* When compared to the Basic Authentication which can contains encoded username and password combination, it is harder to tamper with JWT when a strong key is used.   
* Lastly, REST APIs has been primarily designed to keep the server stateless and JWT conforms to that concept with each request coming with self-contained JWT Authorization token. 

### How to make the Service Redundant
A service can be made redundant with Load Balancing. Increase in the number of users on an application increases strain. As a result, a single server fails to support full workload.
To meet demand from upsurge of users, workload is spread over multiple servers. This practice is called Load Balancing.

Possible Considerations for redundancy are: 
* Time frame of spike or upsurge in traffic. For example, for an ecommerce website likely to experience upsurge during promotional periods, redundant services at these times.
* Location of Most Active Users. Users of applications with global reach can be better served when load balancers are added to locations (or regions) were majority of the users of the application reside. 


### How I solved this task?
This task was addressed by creating resource controllers, models (entities), services (logic) and repositories. 
To cater for possible exceptions in the app, I have created BadRequestException and NotFoundException. 

* The app runs on port 80
* API Documentation can be accessed using this url: http://0.0.0.0:80/api/swagger-ui.html#!

#### Approach to Error Handling
To ensure that error responses adhere to standard and are easy to update, I have created RestExceptionHandler Advicer. This advicer picks up NotFound, BadRequest and General Exception.
This approach aids maintainability and code reuse as exception thrown in the app are handled by the advicer.

#### Approach to CRUD
While the instruction said basic CRUD but Delete operation was absent, I implemented Delete operation using Soft Delete.

#### Running the App in Docker
     

#### Controlllers
* Product Controller (http://0.0.0.0:80/api/products)
* Order Controller (http://0.0.0.0:80/api/orders)

#### Services
* Product Service: The service class contains logic for processing products 
* Order Service: This Service class contains logic for processing order.

#### Repository
* Product Repo: This Repository extends JPARepository for performing Product CRUD
* Order Repo: This Repository extends JPARepository for performing Order CRUD

#### Utils
* CreatePageable: This file creates Sort property based on String sort value passed.
* DateUtil: This file receives date string and returns a valid parsed Date or null.

#### Config
* SwaggerConfig: This file contains configuration for Swagger (the plugin used for creating documentation)


#### Advicers
* RestExceptionHandler: This class intercepts exceptions and returns exception in a standardised format.

#### Exception
* BadRequestException: This exception is thrown for validation errors e.g.: if a product has no name or price at the point of creation, this exeception with array of errors is thrown
* NotFoundException: This exception is thrown when an entity with a specified id is not found.
