package api.pet.stepDefinitions;

import api.ApiBaseTest;
import api.specifications.Specifications;
import api.utils.Constant;
import api.utils.messages.fail.FailMessages;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.given;

public class PetStepDefinitionsTest extends ApiBaseTest {
    private Response response;

    @When("Upload an image to pet with id {string}, additionalMetadata {string} and file name {string}")
    public void uploadAnImageToPetWithIdAdditionalMetadataAndFile(String id, String additionalMetadata, String fileName) {
        Specifications.installSpecification(Specifications.Request.requestSpecMultipart(Constant.Url.BASE_PETSTORE));

        response = given()
                .multiPart("additionalMetadata", additionalMetadata)
                .multiPart("file", new File(Constant.IMAGE_FOLDER, fileName))
                .pathParam("id", id)
                .post("pet/{id}/uploadImage")
                .then()
                .log().all()
                .extract().response();
    }

    @Then("Response with status {int}")
    public void responseWithStatusAndMessage(int status) {
        Assert.assertEquals(response.statusCode(), status, FailMessages.RESPONSE_STATUS_NOT_MATCH_EXPECTED);
        Assert.assertNotNull(response.path("message"), FailMessages.MESSAGE_SHOULD_NOT_BE_NULL);
    }

}
