package com.smf.search;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


public class GetProcessorTest {

	@Test
	public void process() throws Exception {
		final JSONObject jsonProxyEvent = (JSONObject) new JSONParser().parse(new String(Files.readAllBytes(Paths.get("src/test/resources/Get.json"))));
		final ProxyResponseBuilder responseBuilder = ProxyResponseBuilder.get();

		new GetProcessor().process(jsonProxyEvent, responseBuilder, null);

		final JSONObject jsonResponse = responseBuilder.build();
		System.out.println(jsonResponse.toJSONString());
		assertThat(jsonResponse.get("content-type")).isEqualTo("application/json");
		assertThat(jsonResponse.get("body")).isNotNull();
		final String body = (String) jsonResponse.get("body");
		final JSONObject jsonBody = (JSONObject) new JSONParser().parse(body);
		assertThat(jsonBody.get("processor")).isEqualTo(GetProcessor.class.getSimpleName());
		assertThat(jsonBody.get("symbol")).isEqualTo("X12345X");
	}
}
