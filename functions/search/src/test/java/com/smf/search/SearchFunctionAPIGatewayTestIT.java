package com.smf.search;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class SearchFunctionAPIGatewayTestIT {
	private static RequestSpecification spec;

	@BeforeClass
	public static void initSpec() {
		spec = new RequestSpecBuilder()
				.setContentType(ContentType.JSON)
				.setAccept(ContentType.JSON)
				.setBaseUri("http://localhost:3000/")
				.addFilter(new ResponseLoggingFilter())//log request and response for better debugging. You can also only log if a requests fails.
				.addFilter(new RequestLoggingFilter())
				.build();
	}

	@Test
	public void testGet403() throws Exception {
		given().spec(spec)
				.when()
				.get("smf/instrument/")
				.then()
				.statusCode(403);
	}

	@Test
	public void testGet() throws Exception {
		final String response = given().spec(spec)
				.when()
				.get("smf/instrument/ABCDEFG/")
				.then()
				.statusCode(200)
				.extract()
				.asString();
		assertThat(response).isEqualToIgnoringCase("Output from GetProcessor: symbol=ABCDEFG");
	}

	@Test
	public void testGet2() throws Exception {
		final String response = given().spec(spec)
				.when()
				.get("smf/instrument/ABCDEFG")
				.then()
				.statusCode(200)
				.extract()
				.asString();
		assertThat(response).isEqualToIgnoringCase("Output from GetProcessor: symbol=ABCDEFG");
	}

	@Test
	public void testPost403() throws Exception {
		given().spec(spec)
				.when()
				.post("smf/instrument/ABCDEF/")
				.then()
				.statusCode(403);
	}

	@Test
	public void testPost() throws Exception {
		final String response = given().spec(spec)
				.when()
				.post("smf/instrument/")
				.then()
				.statusCode(200)
				.extract()
				.asString();
		assertThat(response).isEqualToIgnoringCase("Output from PostProcessor");
	}


	@Test
	public void testPostWithPayload() throws Exception {
		final String response = given().spec(spec)
				.body("Post payload")
				.when()
				.post("smf/instrument/")
				.then()
				.statusCode(200)
				.extract()
				.asString();
		assertThat(response).isEqualToIgnoringCase("Output from PostProcessor. body: Post payload");
	}
}
