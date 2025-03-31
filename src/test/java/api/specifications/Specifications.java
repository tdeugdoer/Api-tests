package api.specifications;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.experimental.UtilityClass;
import org.apache.http.HttpStatus;

@UtilityClass
public class Specifications {
    public void installSpecification(RequestSpecification requestSpec) {
        RestAssured.requestSpecification = requestSpec;
    }

    public void installSpecification(ResponseSpecification responseSpec) {
        RestAssured.responseSpecification = responseSpec;
    }

    public void installSpecification(RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
    }

    @UtilityClass
    public class Request {
        public RequestSpecification requestSpec(String url, ContentType contentType) {
            return new RequestSpecBuilder()
                    .setBaseUri(url)
                    .setContentType(contentType)
                    .build();
        }

        public RequestSpecification requestSpecJson(String url) {
            return requestSpec(url, ContentType.JSON);
        }

        public RequestSpecification requestSpecMultipart(String url) {
            return requestSpec(url, ContentType.MULTIPART);
        }

    }

    @UtilityClass
    public class Response {
        public ResponseSpecification responseSpec(int status) {
            return new ResponseSpecBuilder()
                    .expectStatusCode(status)
                    .build();
        }

        public ResponseSpecification responseSpecOK200() {
            return responseSpec(HttpStatus.SC_OK);
        }

        public ResponseSpecification responseSpecCreated201() {
            return responseSpec(HttpStatus.SC_CREATED);
        }

        public ResponseSpecification responseSpecError400() {
            return responseSpec(HttpStatus.SC_BAD_REQUEST);
        }

        public ResponseSpecification responseSpecError404() {
            return responseSpec(HttpStatus.SC_NOT_FOUND);
        }

    }

}
