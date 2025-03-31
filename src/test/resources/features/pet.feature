Feature: https://petstore.swagger.io/#/pet/...

  Scenario: Uploads an image to pet
    When Upload an image to pet with id "417389", additionalMetadata "png" and file name "Pet.png"
    Then Response with status 200