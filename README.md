# Credit Limit Application

  Table of contents

  * [**Version history**](#version-history)
  * [**About the application**](#about-the-application)
  * [**Assumptions**](#assumptions)
  * [**Operation manual**](#operation-manual)
    + [**Pre-requisites**](#pre-requisites)
    + [**Steps**](#steps)
  * [**Architecture**](#architecture)
  * [**Technology stack**](#technology-stack)

## **Version history**

  | Version| Date | Description | 
  | --- | --- | --- | 
  | 1.0.0 | 8 Feb 2021 | Initial release |

## **About the application**

  The Credit Limit Application is an application for generating credit limit report.

  The report provides the following insights:
  * Whether there is a credit limit breach
  * Information about the credit limit breach, if there is any
  * Whether the combined sub-entity limits are higher than the limits at the parent group(s)

  <br/>

  <img src="https://bn1301files.storage.live.com/y4mbOyCrPSqRsBRzvLcqFBB67WORz4SLzRp1sQrWI1-yLMpzdOCxQbfwwvoCIlhUcqZa0LrhEtUSCUGZk1UlSFxyL3W3KZQzESl7W7_XQa0jJXk9gWQaYwv6vrkp2pWSGCCg2HTVPN-s9a83AL1P6jUy8sH2A098aUwtc3Yx-tjJG5HlLncWbSg6nxH3QDa-H8r?width=1994&height=536&cropmode=none" alt="Report">

## **Assumptions**

The following assumptions are being made for the application:
* Input CSV file is located under in an accessible location in local system
* The output report is printed in file as well as console
* The input CSV file is well-formatted
* Input file fields data formats:
    * Data are alpha-numeric only and does not contain special characters (e.g. quoting or including commas) as values
    * Numeric fields (e.g. limit) contain numbers only
*  The entity relationship described in the file is valid, e.g. there will not be scenarios like
    * Dead loop, e.g. entity E's parent is F and entity F's parent is E
* The credit limit and utilization fields are integers
* The combined credit limit and combined utilization of each entity group will not exceed the magnitude of 2^31 -1 and hence can be represented by Java Integer type
* In the second requirement for reporting where the combined sub-entity limits are higher than the limits at the parent group(s), the **"parent group"** means the **root entity** of each entity group
* House-keeping of the generated report is not taken into account

## **Operation manual**

  ### Pre-requisites

  * Maven
  * JDK8

  ### **Steps**

  #### Input CSV file and output report locations

   The current input CSV file is set to be referenced at following path

    src/main/resources/prod-data.csv

   The current output CSV file is set to be referenced at following path

    src/main/resources/prod-report.txt

   If you would like to change the locations of these files, you can override the properties with JVM arguments<br/>
   e.g.

    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DdataPath=<PATH> -DreportPath=<PATH>"

  or

    java -DdataPath=<PATH> -DreportPath=<PATH> -jar credit-limit-1.0.0.jar 


  #### Building

  Before beginning, clear the content inside the report at the report location to prevent confusion later.

  The applcation can be started directly by running the following command at the project root folder

    mvn spring-boot:run

   If you would like to create a JAR file from the source, run the following command

    mvn clean package

   A JAR file will be created in the "target" folder and can be executed by the following command

    java -jar credit-limit-<artifact version>.jar

  ### **Running**

  It is assumed that there is a consumer (another application or REST controller) injecting the CreditLimtService and utilizing its functions.

  The following API endpoint is created to demonstrate the usage:

  | Verb | Path | URL param  | Request param | Request body | Description 
  | --- | --- | --- | --- | --- |  --- |
  | GET | /api/creditLimit/report | N/A | N/A | N/A | Generate credit limit report

  Swagger UI path (assuming you are running the application in local environment):<br/>
  http://localhost:8080/swagger-ui.html#/credit-limit-controller

  Sample call:

    GET /api/creditLimit/report HTTP/1.1
    User-Agent: PostmanRuntime/7.26.8
    Accept: */*
    Cache-Control: no-cache
    Postman-Token: d3d87a78-94f1-4461-ac06-d3d60d9911f7
    Host: localhost:8080
    Accept-Encoding: gzip, deflate, br
    Connection: keep-alive
    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 58
    Date: Sun, 07 Feb 2021 23:34:42 GMT
    Keep-Alive: timeout=60
    Connection: keep-alive
    {"message": "Successfully generated credit limit report."}


## **Architecture**

There are 5 main operations required to carry out the credit limit report generation:

* Read CSV file
* Create entities from the read-in data
* Create entity relationship among the entities
* Calculation of combined utilizations and limits
* Print the report

2 services were created to handle these requirements.<br/>
Reading of CSV file is put in UtilService, which can be reused later.

The operations pertaining to generating credit limit report are grouped in CreditLimitService.<br/>
3 major iteations were used to carry out the opeations

 * The first loop creates entities from the string data read-in from CSV file
 * The second loop creates the entity relationships, and performs the required calculations
 * The third loop prints the report

## **Technology stack**

* Java 8
* Spring Boot v2.4.2
  * Dependencies:
    * spring-boot-starter-web (Proivdes a Tomcat web container)
    * springfox-swagger2 (Provides Swagger support)
    * springfox-swagger-ui (Provides Swager UI)
    * lombok (Provides getters/setters to entity/model class properties, and provides slf4j support for logging)
    * spring-boot-starter-test (Provides JUnit support)
    * spring-boot-maven-plugin (Provides Maven support)
    * spring-boot-starter-actuator (For development)
    * spring-boot-devtools (For development)
