package de.jonashackt.restexamples;

import de.jonashackt.restexamples.controller.Controller;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = RestexamplesApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = "server.port = 8080"
)
public class RestexamplesApplicationTests {
    
	@Test
	public void testWithSpringRestTemplate() {
	    // Given
	    RestTemplate restTemplate = new RestTemplate();
	    
	    // When
	    String response = restTemplate.getForObject("http://localhost:8080/restexamples/hello", String.class);
	    
	    // Then
	    assertEquals(Controller.RESPONSE, response);
	}
	
	/**
	 * Using Restassured for elegant REST-Testing, see https://github.com/jayway/rest-assured
	 */
	@Test
    public void testWithRestAssured() {
	    
	    given() // can be ommited when GET only
        .when() // can be ommited when GET only
            .get("http://localhost:8080/restexamples/hello")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .assertThat()
				.body(is(Controller.RESPONSE));
    }

    @Test public void
	check_if_responded_branch_name_is_correct() {
		get("http://localhost:8080/restexamples/branchname")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.equals(Controller.BRANCH_RESPONSE + "master");
	}

}
