package de.jonashackt.restexamples;

import de.jonashackt.restexamples.controller.Controller;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = RestexamplesApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class RestexamplesApplicationTests {

	@LocalServerPort
	private int port;

	@Before
	public void init() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	public void testWithSpringRestTemplate() {
		get("/restexamples/hello")
				.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body(is(Controller.RESPONSE));
	}

	@Test
	public void testWithRestAssured() {

		get("/restexamples/hello")
				.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body(is(Controller.RESPONSE));
	}

	@Test public void
	check_if_responded_branch_name_is_correct() {
		get("/restexamples/branchname")
				.then()
				.statusCode(HttpStatus.SC_OK)
				.equals(Controller.BRANCH_RESPONSE + "master");
	}

}
