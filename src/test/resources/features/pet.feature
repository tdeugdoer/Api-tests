Feature: https://petstore.swagger.io/#/pet/...

  Scenario: Uploads an image to pet
    When Upload an image to pet with id "417389", additionalMetadata "png" and file name "Pet.png"
    Then Response with status 200

  Scenario: Success add a new pet to the store
    When Create pet with name "Doggie2024", category name "dog", tag "blond", status "available" and photoUrl "https://avatars.dzeninfra.ru/get-zen_doc/1581245/pub_5e85d07d99b22b07918975e8_5e85d71a549f6e4aecfb4fbb/scale_1200"
    Then Response with response status 200 and pet with name "Doggie2024", category name "dog", tag "blond", status "available" and photoUrl "https://avatars.dzeninfra.ru/get-zen_doc/1581245/pub_5e85d07d99b22b07918975e8_5e85d71a549f6e4aecfb4fbb/scale_1200"

  Scenario: Fail add a new pet to the store
    When Create pet without data
    Then Response with response status 405

  Scenario: Finds pets by status
    When Find pets with status "available"
    Then Response with response status 200 and all pets has status "available"

  Scenario: Finds pets without status
    When Find pets without status
    Then Empty response with response status 200