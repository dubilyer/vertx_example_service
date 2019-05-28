# vertx_example_service

<h1>Simple vertx service. Can be used as sandbox for a testing tools investigations.</h1>

To run the server use:
    Start your redis db.
    To do it with the help of docker image, you can run "redis_start.sh"
    
    <b>mvn clean install exec:java</b>
    
    If you want to use a custom redis port,  you can provide it via system property:
    
    <b>mvn clean install exec:java -DredisPort={your port}<b>
    
To run tests you can use:

    <b>mvn test</b>

    You don't need redis for it. Tests raise on own container wen needed.

<h2>Exposed endpoints:</h2>
    1. GET http://{your host}/user/{id}
    2. DELETE http://{your host}/user/{id}
    3. POST http://{your host}/user + body {"id":"{id}", "name":"{name}"}
        
