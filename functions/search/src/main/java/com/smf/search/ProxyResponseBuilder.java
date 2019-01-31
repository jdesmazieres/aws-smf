package com.smf.search;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ProxyResponseBuilder {
	private final JSONObject out;
	private final JSONArray headers;

	public static ProxyResponseBuilder get() {
		return new ProxyResponseBuilder();
	}

	public static ProxyResponseBuilder wrap(final JSONObject jsonResponse) {
		return new ProxyResponseBuilder(jsonResponse);
	}

	private ProxyResponseBuilder() {
		// default values
		out = new JSONObject();
		out.put("isBase64Encoded", false);
		out.put("statusCode", 200);
		headers = new JSONArray();
	}

	private ProxyResponseBuilder(final JSONObject out) {
		this.out = out;
		if (out.get("statusCode") == null) {
			out.put("statusCode", 200);
		}
		if (out.get("isBase64Encoded") == null) {
			out.put("isBase64Encoded", false);
		}
		if (out.get("headers") == null) {
			headers = new JSONArray();
		} else {
			headers = (JSONArray) out.get("headers");
		}
	}

	public JSONObject build() {
		if (!headers.isEmpty()) {
			out.put("headers", headers);
		}
		return out;
	}

	public String toJSONString() {
		return build().toJSONString();
	}

	public ProxyResponseBuilder withStatusCode(final int status) {
		out.put("statusCode", status);
		return this;
	}

	public ProxyResponseBuilder withContentType(final String contentType) {
		out.put("content-type", contentType);
		return this;
	}

	public ProxyResponseBuilder withBody(final Object body) {
		out.put("body", body);
		return this;
	}

	public ProxyResponseBuilder withHeader(final String name, final String value) {
		final JSONObject header = new JSONObject();
		header.put(name, value);
		headers.add(header);
		return this;
	}

	public ProxyResponseBuilder with(final String name, final Object value) {
		out.put(name, value);
		return this;
	}

	public ProxyResponseBuilder withCorsHeaders() {
		final JSONObject corsJson = new JSONObject();
		corsJson.put("Access-Control-Allow-Origin", "*");
		corsJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		corsJson.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
		out.put("headers", corsJson);
		return this;
	}
}
