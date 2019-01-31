package com.smf.search;

import com.amazonaws.services.lambda.runtime.Context;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetProcessor {
	private static final Logger log = LoggerFactory.getLogger(GetProcessor.class);

	public void process(final JSONObject jsonProxyEvent, final JSONObject responseJson, final Context context) {
		log.info("Executing GetProcessor.process");

		responseJson.put("body", "Output from GetProcessor: symbol=" + ((JSONObject) jsonProxyEvent.get("pathParameters")).get("symbol"));
	}
}
