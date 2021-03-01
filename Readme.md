## A warehoue application created for inventory management. The application is developed based on the assignment block below.

# Code Assignment

## Intro
This assignment will be used as a discussion during a technical interview.
The primary values for the code we look for are: simplicity, readability, maintainability, testability. It should be easy to scan the code, and rather quickly understand what it’s doing. Pay attention to naming.
 
You may choose any coding language, and we look forward to discussing your choice.

## The Task
The assignment is to implement a warehouse software. This software should hold articles, and the articles should contain an identification number, a name and available stock. It should be possible to load articles into the software from a file, see the attached inventory.json.
The warehouse software should also have products, products are made of different articles. Products should have a name, price and a list of articles of which they are made from with a quantity. The products should also be loaded from a file, see the attached products.json. 
 
The warehouse should have at least the following functionality;
* Get all products and quantity of each that is an available with the current inventory
* Remove(Sell) a product and update the inventory accordingly


# Application stack

Application runs on Java 8 + springboot + spring security (optional) + NoSQL MongoDB.

# Tools used
Intellij,Postman

# Application build and unit test execution
mvn clean install 

# Application start up

Run the spring boot application from the main class file WarehouseBootApplication.java.

#Test API
Postman collections to test API's available at /ikea/src/test/resources/collections/Warehouse Management.postman_collection.json
Test files used 
/ikea/src/test/resources/inventory.json
/ikea/src/test/resources/products.json
/ikea/src/test/resources/inventory - update.json
/ikea/src/test/resources/products-error.json