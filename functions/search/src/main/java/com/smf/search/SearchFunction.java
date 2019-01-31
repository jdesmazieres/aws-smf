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

	@Override
	public void handleRequest(final InputStream inputStream, final OutputStream outputStream, final Context context) throws IOException {
		log.info("Executing SearchFunction.handleRequest");

		final ProxyResponseBuilder responseJson = ProxyResponseBuilder.get();
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			final JSONObject event = (JSONObject) parser.parse(reader);
			log.info("  + incoming event: \n{}", event.toJSONString());
			responseJson.withCorsHeaders();
			if ("GET".equals(event.get("httpMethod"))) {
				new GetProcessor().process(event, responseJson, context);
			} else if ("POST".equals(event.get("httpMethod"))) {
				new PostProcessor().process(event, responseJson, context);
			} else if ("OPTIONS".equals(event.get("httpMethod"))) {
				// no content, only headers
			} else {
				responseJson.withStatusCode(404)
						.with("exception", "method not allowed on this url");
			}
		} catch (final ParseException pex) {
			responseJson.withStatusCode(400)
					.with("exception", pex);
		}

		log.info("\n-----------------------------\n  + output response:\n{}\n-----------------------------\n", responseJson.toJSONString());
		final OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
		writer.write(responseJson.toJSONString());
		writer.close();
	}
}
