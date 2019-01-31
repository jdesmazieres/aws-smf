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

		final String body = (String) jsonProxyEvent.get("body");

		if (StringUtils.isBlank(body)) {
			responseJson.put("body", "Output from PostProcessor");
		} else {
			responseJson.put("body", "Output from PostProcessor. body: " + body);
		}
	}
}
