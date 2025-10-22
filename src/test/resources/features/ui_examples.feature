@UI
Feature: UI Examples - Common actions for web testing
  This feature contains example scenarios that exercise the reusable UI steps
  provided by the framework. Use these as templates when writing real tests.

  Background:
    Given open browser

  @navigation
  Scenario: Navigate to a site and verify content
    Given navigate to "https://example.com"
    Then page should contain text "Example Domain"
    When take screenshot "target/screenshots/example-home.png"

  @form @data
  Scenario: Fill a form, save values and assert
    When navigate to "https://www.w3schools.com/howto/howto_css_contact_form.asp"
    When wait for visible "#main" for 10 seconds
    When type "John" into "#fname"
    When type "Doe" into "#lname"
    When type "john.doe@example.com" into "#email"
    When save text of "#fname" as "firstName"
    Then saved variable "firstName" should not be null

  @dropdown @assertions
  Scenario: Interact with dropdowns and assert
    Given navigate to "https://www.w3schools.com/tags/tryit.asp?filename=tryhtml_select"
    When wait for visible "#main" for 5 seconds
    # The page is inside an iframe on W3Schools, these are example steps showing the pattern
    When execute js "document.querySelector('select').value = 'volvo'"
    Then element "select" attribute "value" should be "volvo"

  @checkbox @upload
  Scenario: Checkboxes, file upload and element screenshots
    Given navigate to "https://www.w3schools.com/howto/howto_css_file_upload_button.asp"
    When wait for visible "#main" for 5 seconds
    # Example: upload will depend on a real local file path
    When upload file "${sampleFilePath}" to "input[type=file]"
    When take element screenshot "input[type=file]" save as "target/screenshots/upload-element.png"

  @tabs @windows
  Scenario: Open a new tab and switch to it
    Given navigate to "https://example.com"
    When open new tab
    When switch to latest tab
    Then page should contain text "Example Domain"

  @variables @reuse
  Scenario: Save and reuse variables between steps
    Given navigate to "https://example.com"
    When save text of "h1" as "heading"
    Then saved variable "heading" should be "Example Domain"
    When navigate to "https://example.com#${heading}"

  @cleanup
  Scenario: Close browser at the end
    When close browser

