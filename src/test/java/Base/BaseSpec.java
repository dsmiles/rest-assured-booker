package Base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

public class BaseSpec {

    private static String containerHostname;
    private static int containerPort;

    public static void init(String host, int port) {
        containerHostname = host;
        containerPort = port;
    }

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept("application/json")      // ContentType.JSON sets multiple content-types
            .setBaseUri("http://" + containerHostname)
            .setPort(containerPort)
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();
    }

    public static ResponseSpecification responseSpec() {
        return new ResponseSpecBuilder()
            .expectStatusCode(HttpStatus.SC_OK)
            .expectContentType(ContentType.JSON)
            .build();
    }
}
