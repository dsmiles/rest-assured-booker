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

    /**
     * Initializes the container hostname and port for the REST API testing. As this is done at runtime.
     *
     * @param host The hostname of the container.
     * @param port The port of the container.
     */
    public static void init(String host, int port) {
        containerHostname = host;
        containerPort = port;
    }

    /**
     * Creates a RequestSpecification with predefined settings.
     *
     * @return The RequestSpecification object.
     */
    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept("application/json")
            .setBaseUri("http://" + containerHostname)
            .setPort(containerPort)
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();
    }

    public static RequestSpecification requestSpecXml() {
        return new RequestSpecBuilder()
            .setContentType("application/xml")
            .setAccept("application/xml")
            .setBaseUri("http://" + containerHostname)
            .setPort(containerPort)
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();
    }

    /**
     * Creates a ResponseSpecification with predefined settings.
     *
     * @return The ResponseSpecification object.
     */
    public static ResponseSpecification responseSpec() {
        return new ResponseSpecBuilder()
            .expectStatusCode(HttpStatus.SC_OK)
            .expectContentType(ContentType.JSON)
            .build();
    }

    public static ResponseSpecification responseSpecXml() {
        return new ResponseSpecBuilder()
            .expectStatusCode(HttpStatus.SC_OK)
//            .expectContentType(ContentType.XML)
            .build();
    }
}
