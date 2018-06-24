package com.example.usersystem.controller;


import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Tests for {@link HelloWorldController}. Modify the tests in order to support your use case as you build your project.
 */
public class HelloWorldControllerTest {

    private static final String EXPECTED_RESPONSE_VALUE = "Hello AWS CodeStar!";
    private static final String INPUT_NAME = "AWS CodeStar";

    private final HelloWorldController controller = new HelloWorldController();

    /**
     * Initializing variables before we run the tests.
     * Use @BeforeAll for initializing static variables at the start of the test class execution.
     * Use @BeforeEach for initializing variables before each test is run.
     */
    @Before
    public void setup() {
        // Use as needed.
    }

    /**
     * De-initializing variables after we run the tests.
     * Use @AfterAll for de-initializing static variables at the end of the test class execution.
     * Use @AfterEach for de-initializing variables at the end of each test.
     */
    @After
    public void tearDown() {
        // Use as needed.
    }

    /**
     * Basic test to verify the result obtained when calling {@link HelloWorldController#helloWorldGet} successfully.
     */
    @Test
    public void testGetRequest() {
        ResponseEntity responseEntity = controller.helloWorldGet(INPUT_NAME);

        // Verify the response obtained matches the values we expect.
        JSONObject jsonObjectFromResponse = new JSONObject(responseEntity.getBody().toString());
        Assert.assertEquals(EXPECTED_RESPONSE_VALUE, jsonObjectFromResponse.get("Output"));
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    /**
     * Basic test to verify the result obtained when calling {@link HelloWorldController#helloWorldPost} successfully.
     */
    @Test
    public void testPostRequest() {
        ResponseEntity responseEntity = controller.helloWorldPost(INPUT_NAME);

        // Verify the response obtained matches the values we expect.
        JSONObject jsonObjectFromResponse = new JSONObject(responseEntity.getBody().toString());
        Assert.assertEquals(EXPECTED_RESPONSE_VALUE, jsonObjectFromResponse.get("Output"));
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}