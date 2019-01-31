package com.smf.search;

import com.amazonaws.services.lambda.runtime.Context;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostProcessor {
	private static final Logger log = LoggerFactory.getLogger(PostProcessor.class);

	public void process(final JSONObject jsonProxyEvent, final JSONObject responseJson, final Context context) {
		log.info("Executing PostProcessor.process");

		final JSONObject body = new JSONObject();
		body.put("processor", getClass().getSimpleName());

		final String payload = (String) jsonProxyEvent.get("body");
		if (StringUtils.isNotBlank(payload)) {
			body.put("payload", payload);
		}
		responseJson.put("content-type", "application/json");
		responseJson.put("body", body.toJSONString());
	}
}
