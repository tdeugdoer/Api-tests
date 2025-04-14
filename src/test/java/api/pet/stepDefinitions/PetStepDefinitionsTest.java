package api.pet.stepDefinitions;

import api.ApiBaseTest;
import api.dtos.Category;
import api.dtos.Pet;
import api.dtos.Tag;
import api.specifications.Specifications;
import api.utils.Constant;
import api.utils.messages.fail.FailMessages;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                .extract().response();
    }

    @Then("Response with status {int}")
    public void responseWithStatusAndMessage(int responseStatus) {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.statusCode(), responseStatus, FailMessages.RESPONSE_STATUS_NOT_MATCH_EXPECTED);
        softAssert.assertNotNull(response.path("message"), FailMessages.MESSAGE_SHOULD_NOT_BE_NULL);
        softAssert.assertAll();
    }

    @When("Create pet with name {string}, category name {string}, tag {string}, status {string} and photoUrl {string}")
    public void createPetWithNameCategoryNameTagStatusAndPhotoUrl(String petName, String categoryName, String tag,
                                                                  String status, String photoUrl) {
        Specifications.installSpecification(Specifications.Request.requestSpecJson(Constant.Url.BASE_PETSTORE));

        Pet pet = Pet.builder()
                .name(petName)
                .photoUrls(List.of(photoUrl))
                .category(Category.builder()
                        .name(categoryName)
                        .build())
                .tags(List.of(Tag.builder()
                        .name(tag)
                        .build()))
                .status(status)
                .build();

        response = given()
                .body(pet)
                .post("pet")
                .then()
                .extract().response();
    }

    @Then("Response with response status {int} and pet with name {string}, category name {string}, tag {string}, status {string} and photoUrl {string}")
    public void responseWithStatusAndPetWithNameCategoryNameTagStatusAndPhotoUrl(int responseStatus, String petName, String categoryName,
                                                                                 String tag, String status, String photoUrl) {
        Assert.assertEquals(response.statusCode(), responseStatus, FailMessages.RESPONSE_STATUS_NOT_MATCH_EXPECTED);

        Pet pet = response.as(Pet.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(pet.getName(), petName, FailMessages.MESSAGE_NOT_MATCH_EXPECTED);
        softAssert.assertEquals(pet.getCategory().getName(), categoryName, FailMessages.MESSAGE_NOT_MATCH_EXPECTED);
        softAssert.assertEquals(pet.getTags().size(), 1, FailMessages.LIST_SHOULD_NOT_BE_EMPTY);
        softAssert.assertEquals(pet.getTags().getFirst().getName(), tag, FailMessages.MESSAGE_NOT_MATCH_EXPECTED);
        softAssert.assertEquals(pet.getStatus(), status, FailMessages.MESSAGE_NOT_MATCH_EXPECTED);
        softAssert.assertEquals(pet.getPhotoUrls().size(), 1, FailMessages.LIST_SHOULD_NOT_BE_EMPTY);
        softAssert.assertEquals(pet.getPhotoUrls().getFirst(), photoUrl, FailMessages.MESSAGE_NOT_MATCH_EXPECTED);
        softAssert.assertAll();
    }

    @When("Create pet without data")
    public void createPetWithoutData() {
        Specifications.installSpecification(Specifications.Request.requestSpecJson(Constant.Url.BASE_PETSTORE));

        response = given()
                .post("pet")
                .then()
                .extract().response();
    }

    @Then("Response with response status {int}")
    public void responseWithResponseStatus(int responseStatus) {
        Assert.assertEquals(response.statusCode(), responseStatus, FailMessages.RESPONSE_STATUS_NOT_MATCH_EXPECTED);
    }

    @When("Find pets with status {string}")
    public void findPetsWithStatus(String status) {
        Specifications.installSpecification(Specifications.Request.requestSpec(Constant.Url.BASE_PETSTORE));

        response = given()
                .param("status", status)
                .get("pet/findByStatus")
                .then()
                .extract().response();
    }

    @Then("Response with response status {int} and all pets has status {string}")
    public void responseWithResponseStatusAndAllPetsHasStatus(int responseStatus, String status) {
        Assert.assertEquals(response.statusCode(), responseStatus, FailMessages.RESPONSE_STATUS_NOT_MATCH_EXPECTED);
        Assert.assertEquals(new HashSet<>(response.jsonPath().getList("status")), Set.of(status), FailMessages.COLLECTION_NOT_MATCH_EXPECTED);
    }

    @When("Find pets without status")
    public void findPetsWithoutStatus() {
        Specifications.installSpecification(Specifications.Request.requestSpec(Constant.Url.BASE_PETSTORE));

        response = given()
                .get("pet/findByStatus")
                .then()
                .extract().response();
    }

    @Then("Empty response with response status {int}")
    public void emptyResponseWithResponseStatus(int responseStatus) {
        Assert.assertEquals(response.statusCode(), responseStatus, FailMessages.RESPONSE_STATUS_NOT_MATCH_EXPECTED);
        Assert.assertTrue(response.jsonPath().getList("").isEmpty(), FailMessages.LIST_SHOULD_BE_EMPTY);
    }

}
