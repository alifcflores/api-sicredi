## Technical challenge for Sicredi company using:
- Java
- RestAssured

### The challenge:
- Download the Project sent by the company in Java and run the application.
- Create test scenarios using the downloaded API, which is a credit analysis tool using the customers' CPF (cpf is an official document in Brazil)

### Available Routes:
- Consult a restriction by CPF
- Create a simulation
- Change a simulation
- Consult all registered simulations
- Consult a simulation using the CPF
- Remove a simulation

### The solution:
- My proposal was to generate complete coverage of all status codes provided in the documentation. 
- I created a function that randomly collects 1 cpf from each list at a time, to perform the query and simulation, performing the complete CRUD.
- I used a Faker library to generate random data for each test.
#### I created 2 functions that generate cpfs randomly:
- with restricted cpfs 
- with unrestricted CPFs.
