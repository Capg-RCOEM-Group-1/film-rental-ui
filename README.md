TO MAKE JAR:

Using terminal go to the 'film-rental-ui' folder:
```bash
mvn clean package
```

Then:

```bash
cd target
java -jar film-rental-ui-0.0.1-SNAPSHOT.jar --api.base-url=http://localhost:{YOUR_BACKEND_PORT}/
```
