import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class BaseTest {

    // Docker container configuration
    protected static final DockerImageName BOOKER_IMAGE = DockerImageName.parse("mwinteringham/restfulbooker:latest");
    protected static final int BOOKER_EXPOSED_PORT = 3001;
    @Container
    protected static GenericContainer<?> bookerContainer = new GenericContainer<>(BOOKER_IMAGE)
        .withExposedPorts(BOOKER_EXPOSED_PORT);

    // REST-assured configuration
//    RequestSpecification requestSpec;
//    ResponseSpecification responseSpec;

    @BeforeAll
    static void setup() {

//        requestSpec = new RequestSpecBuilder()
//            .setContentType(ContentType.JSON)
//            .setAccept(ContentType.JSON)
//            .setBaseUri("http://" + bookerContainer.getHost())
//            .setPort(bookerContainer.getFirstMappedPort())
//            .addFilter(new RequestLoggingFilter())
//            .addFilter(new ResponseLoggingFilter())
//            .build();
//
//        responseSpec = new ResponseSpecBuilder()
//            .expectStatusCode(HttpStatus.SC_OK)
//            .expectContentType("application/json")
//            .build();
    }

    @AfterAll
    public static void tearDown() {
        if (bookerContainer != null) {
            bookerContainer.close();
        }
    }

    protected RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept("application/json")      // ContentType.JSON sets multiple content-types
            .setBaseUri("http://" + bookerContainer.getHost())
            .setPort(bookerContainer.getFirstMappedPort())
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();
    }

    protected ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder()
            .expectStatusCode(HttpStatus.SC_OK)
            .expectContentType(ContentType.JSON)
            .build();
    }

}
