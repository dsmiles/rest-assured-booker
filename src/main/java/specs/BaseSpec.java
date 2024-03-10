package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

/**
 * This class provides methods to create RequestSpecification and ResponseSpecification objects
 * with predefined settings for REST API testing.
 */
public class BaseSpec {

    private static String containerHostname;
    private static int containerPort;

    public static void init(String host, int port) {
        containerHostname = host;
        containerPort = port;
    }

    private static RequestSpecBuilder commonRequestSpecBuilder() {
        return new RequestSpecBuilder()
            .setBaseUri("http://" + containerHostname)
            .setPort(containerPort)
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter());
    }

    public static RequestSpecification requestSpec() {
        return commonRequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept("application/json")
            .build();
    }

    public static RequestSpecification requestSpecXml() {
        return commonRequestSpecBuilder()
            .setContentType("application/xml")
            .setAccept("application/xml")
            .build();
    }

    private static ResponseSpecBuilder commonResponseSpecBuilder() {
        return new ResponseSpecBuilder()
            .expectStatusCode(HttpStatus.SC_OK);
    }

    public static ResponseSpecification responseSpec() {
        return commonResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .build();
    }

    public static ResponseSpecification responseSpecXml() {
        return commonResponseSpecBuilder()
            .expectContentType(ContentType.XML)
            .build();
    }
}