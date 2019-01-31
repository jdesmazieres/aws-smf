package com.smf.search;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


public class PostProcessorTest {

	@Test
	public void process() throws Exception {
		final JSONObject jsonProxyEvent = (JSONObject) new JSONParser().parse(new String(Files.readAllBytes(Paths.get("src/test/resources/Post.json"))));
		final ProxyResponseBuilder responseBuilder = ProxyResponseBuilder.get();

		new PostProcessor().process(jsonProxyEvent, responseBuilder, null);

		final JSONObject jsonResponse = responseBuilder.build();
		assertThat(jsonResponse.get("content-type")).isEqualTo("application/json");
		assertThat(jsonResponse.get("body")).isNotNull();
		final String body = (String) jsonResponse.get("body");
		final JSONObject jsonBody = (JSONObject) new JSONParser().parse(body);
		assertThat(jsonBody.get("processor")).isEqualTo(PostProcessor.class.getSimpleName());
		assertThat(jsonBody.get("payload")).isEqualTo("Post payload");
	}
}
