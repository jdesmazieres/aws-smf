package com.smf.search;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public final class AwsLambdaUtils {
	private AwsLambdaUtils() {
	}

	public static AWSLambda buildClient() {
		return buildClient("127.0.0.1", 3001);
	}

	public static AWSLambda buildClient(final String host, final int port) {
		return AWSLambdaClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://" + host + ":" + port + "/", Regions.EU_WEST_3.getName()))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("fake", "fake")))
				.build();
	}

	public static InvokeResult invokeFunction(final String functionName, final String payload, final AWSLambda client) {
		return invokeFunction(functionName, InvocationType.RequestResponse, payload, client);
	}

	public static InvokeResult invokeFunction(final String functionName, final InvocationType invocationType, final String payload, final AWSLambda client) {
		// Construct the Lambda Client
		final InvokeRequest invokeRequest = new InvokeRequest();
		invokeRequest.setInvocationType(invocationType);
		//invokeRequest.setLogType(LogType.Tail);
		invokeRequest.setFunctionName(functionName);
		if (payload != null) {
			invokeRequest.setPayload(payload);
		}

		// Execute and process the results
		return client.invoke(invokeRequest);
	}

}
