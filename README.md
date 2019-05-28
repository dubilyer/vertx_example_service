# vertx_example_service

Simple vertx service. Can be used as sandbox for a testing tools investigations.

To run the server use:
    Start your redis db.
    To do it with the help of docker image, you can run "redis_start.sh"
    
    mvn clean install exec:java
    
To run tests you can use:
    mvn test

You don't need redis for it. Tests raise on own container wen needed.
