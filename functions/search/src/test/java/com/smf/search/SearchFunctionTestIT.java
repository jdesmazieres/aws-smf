package com.smf.search;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchFunctionTestIT {
	private final JSONParser parser = new JSONParser();

	@Test
	public void invokeLambdaInvalidProxyPayload() throws Exception {
		// Construct the Lambda Client
		final AWSLambda awsLambda = AwsLambdaUtils.buildClient();

		// Execute and process the results
		final InvokeResult result = AwsLambdaUtils.invokeFunction("SearchFunction", null, awsLambda);

		// Convert the returned result
		final ByteBuffer resultPayload = result.getPayload();
		final String resultJson = new String(resultPayload.array(), StandardCharsets.UTF_8);
		final JSONObject payload = (JSONObject) parser.parse(resultJson);

		System.out.println(payload);
		assertThat(payload.get("statusCode")).isEqualTo("404");
		assertThat(payload.get("exception")).isEqualTo("method not allowed on this url");

	}

	@Test
	public void invokeLambdaGetProxyPayload() throws Exception {
		final String inputJson = new String(Files.readAllBytes(Paths.get("src/test/resources/SearchFunction-Get-proxy-input.json")));
		// Construct the Lambda Client
		final AWSLambda awsLambda = AwsLambdaUtils.buildClient();

		// Execute and process the results
		final InvokeResult result = AwsLambdaUtils.invokeFunction("SearchFunction", inputJson, awsLambda);

		// Convert the returned result
		final ByteBuffer resultPayload = result.getPayload();
		final String resultJson = new String(resultPayload.array(), StandardCharsets.UTF_8);
		final JSONObject payload = (JSONObject) parser.parse(resultJson);

		System.out.println(payload);
		assertThat(payload.get("statusCode")).isEqualTo("200");
		assertThat(payload.get("body")).isEqualTo("Output from GetProcessor");

	}
}
