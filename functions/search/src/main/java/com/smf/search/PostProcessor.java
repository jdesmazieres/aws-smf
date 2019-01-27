package com.smf.search;

import com.amazonaws.services.lambda.runtime.Context;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostProcessor {
	private static final Logger log = LoggerFactory.getLogger(PostProcessor.class);

	public void process(final JSONObject jsonProxyEvent, final JSONObject responseJson, final Context context) {
		log.info("Executing PostProcessor.process");

		responseJson.put("body", "Output from PostProcessor");
	}
}
