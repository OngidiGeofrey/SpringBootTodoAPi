# Spring Tasks API

This is an API  that manages user pofiles and assigned tasks

## Installation
 
Download and Install JDK 17 [download] (https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

Add the JDK 17 folder to your environment as JAVA_HOME

Download Maven Repository [download](https://maven.apache.org/download.cgi) extract and add the bin folder to the PATH environment variable

Set up your MaiadDB 10 Installation and create a database named 'profiletasks'

Add the connection details to application.settings

To run the app use

```bash
./mvnw spring-boot:run
```

## Usage

Use the application as a REST API

General paths:

POST **/register** - Create a New Account
POST **/login** - Authenticate User and Obtain JWT token fo Authorization
POST **/user/create** - Add New User Account
POST **/user/update** - Update User Account

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)