package com.smf.search;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @see <a href="https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-api-as-simple-proxy-for-lambda.html#api-gateway-proxy-integration-lambda-function-java">API Gateway proxy lambda </a>
 * @see <a href="https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html#api-gateway-simple-proxy-for-lambda-input-format">API gateway input format</a>
 * @see <a href="https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html#api-gateway-simple-proxy-for-lambda-output-format">API gateway output format</a>
 */
public class SearchFunction implements RequestStreamHandler {

	private static final Logger log = LoggerFactory.getLogger(SearchFunction.class);

	private final JSONParser parser = new JSONParser();
	private final GetProcessor getProcessor = new GetProcessor();
	private final PostProcessor postProcessor = new PostProcessor();

	@Override
	public void handleRequest(final InputStream inputStream, final OutputStream outputStream, final Context context) throws IOException {
		log.info("Executing SearchFunction.handleRequest");

		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		final JSONObject responseJson = new JSONObject();
		try {
			final JSONObject event = (JSONObject) parser.parse(reader);
			log.info("  + incoming event: \n{}", event.toJSONString());
			withCorsHeader(responseJson);
			responseJson.put("statusCode", 200);
			responseJson.put("isBase64Encoded", false);
			if ("GET".equals(event.get("httpMethod"))) {
				getProcessor.process(event, responseJson, context);
			} else if ("POST".equals(event.get("httpMethod"))) {
				postProcessor.process(event, responseJson, context);
			} else if ("OPTIONS".equals(event.get("httpMethod"))) {
				// no content, only headers
			} else {
				responseJson.put("statusCode", 404);
				responseJson.put("exception", "method not allowed on this url");
			}
		} catch (final ParseException pex) {
			responseJson.put("statusCode", 400);
			responseJson.put("exception", pex);
		}

		log.info("-----------------------------\n  + output response:\n{}\n-----------------------------\n", responseJson.toJSONString());
		final OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
		writer.write(responseJson.toJSONString());
		writer.close();
	}

	private JSONObject withCorsHeader(final JSONObject responseJson) {
		final JSONObject corsJson = new JSONObject();
		corsJson.put("Access-Control-Allow-Origin", "*");
		corsJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		corsJson.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
		responseJson.put("headers", corsJson);
		return responseJson;
	}
}
