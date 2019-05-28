# vertx_example_service

<h2>Simple vertx-based service. Can be used as sandbox for testing tools investigations.</h2>

To run the server use:

    Start your redis db.
    
    To do it with the help of docker image, you can run `redis_start.sh`
    
    mvn clean install exec:java -DredisHost={your redis host}
    
    If you're using Docker Toolbox for windows, use docker-machine host - "192.168.99.100"
    If you want to use a custom redis port,  you can provide it via system property:
    
    mvn clean install exec:java -DredisHost={your redis host} -DredisPort={your port}
    
To run tests you can use:

    mvn test -DredisHost={your redis host}

    You don't need redis for it. Tests raise on own container wen needed.

<h2>Exposed endpoints:</h2>
    
    1. GET http://{your host}/user/{id}
    
    2. DELETE http://{your host}/user/{id}
    
    3. POST http://{your host}/user + body {"id":"{id}", "name":"{name}"}
        
