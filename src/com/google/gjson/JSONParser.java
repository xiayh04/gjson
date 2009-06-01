/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gjson;

/**
 * Parses the string representation of a JSON object into a set of
 * JSONValue-derived objects.
 */
public class JSONParser {

	/**
	 * Evaluates a trusted JSON string and returns its JSONValue representation.
	 * CAUTION! For efficiency, this method is implemented using the JavaScript
	 * <code>eval()</code> function, which can execute arbitrary script. DO NOT
	 * pass an untrusted string into this method.
	 * 
	 * @param jsonString
	 *            a JSON object to parse
	 * @return a JSONValue that has been built by parsing the JSON string
	 * @throws NullPointerException
	 *             if <code>jsonString</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>jsonString</code> is empty
	 */
	public static JSONValue parse(String jsonString) {
		if (jsonString == null) {
			throw new NullPointerException();
		}
		if (jsonString.length() == 0) {
			throw new IllegalArgumentException("empty argument");
		}
		JSONTokener tknr = new JSONTokener(jsonString);
		return createJSONObject(tknr);
	}

	protected static JSONValue createNull() {
		return JSONNull.getInstance();
	}

	protected static JSONValue createBoolean(boolean v) {
		return JSONBoolean.getInstance(v);
	}

	protected static JSONValue createNumber(double v) {
		return new JSONNumber(v);
	}

	protected static JSONValue createString(String v) {
		return new JSONString(v);
	}

	/**
	 * Not instantiable.
	 */
	private JSONParser() {
	}

	/**
	 * Construct a JSONObject from a JSONTokener.
	 * 
	 * @param x
	 *            A JSONTokener object containing the source string.
	 * @throws JSONException
	 *             If there is a syntax error in the source string or a
	 *             duplicated key.
	 */
	public static JSONObject createJSONObject(JSONTokener x)
			throws JSONException {
		JSONObject jo = new JSONObject();

		char c;
		String key;

		if (x.nextClean() != '{') {
			throw x.syntaxError("A JSONObject text must begin with '{'");
		}
		for (;;) {
			c = x.nextClean();
			switch (c) {
			case 0:
				throw x.syntaxError("A JSONObject text must end with '}'");
			case '}':
				return jo;
			default:
				x.back();
				key = x.nextValue().toString();
			}

			/*
			 * The key is followed by ':'. We will also tolerate '=' or '=>'.
			 */

			c = x.nextClean();
			if (c == '=') {
				if (x.next() != '>') {
					x.back();
				}
			} else if (c != ':') {
				throw x.syntaxError("Expected a ':' after a key");
			}
			putOnce(jo, key, x.nextValue());

			/*
			 * Pairs are separated by ','. We will also tolerate ';'.
			 */

			switch (x.nextClean()) {
			case ';':
			case ',':
				if (x.nextClean() == '}') {
					return jo;
				}
				x.back();
				break;
			case '}':
				return jo;
			default:
				throw x.syntaxError("Expected a ',' or '}'");
			}
		}
	}

	/**
	 * Put a key/value pair in the JSONObject, but only if the key and the value
	 * are both non-null, and only if there is not already a member with that
	 * name.
	 * 
	 * @param key
	 * @param value
	 * @return his.
	 * @throws JSONException
	 *             if the key is a duplicate
	 */
	private static void putOnce(JSONObject jsonObject, String key,
			JSONValue value) throws JSONException {
		if (key != null && value != null) {
			if (jsonObject.containsKey(key)) {
				throw new JSONException("Duplicate key \"" + key + "\"");
			}
			jsonObject.put(key, value);
		}
	}

	public static JSONValue createJSONArray(JSONTokener x) {
		JSONArray ja = new JSONArray();
		char c = x.nextClean();
		char q;
		if (c == '[') {
			q = ']';
		} else if (c == '(') {
			q = ')';
		} else {
			throw x.syntaxError("A JSONArray text must start with '['");
		}
		if (x.nextClean() == ']') {
			return ja;
		}
		x.back();
		for (;;) {
			if (x.nextClean() == ',') {
				x.back();
				ja.add(null);
			} else {
				x.back();
				ja.add(x.nextValue());
			}
			c = x.nextClean();
			switch (c) {
			case ';':
			case ',':
				if (x.nextClean() == ']') {
					return ja;
				}
				x.back();
				break;
			case ']':
			case ')':
				if (q != c) {
					throw x
							.syntaxError("Expected a '" + new Character(q)
									+ "'");
				}
				return ja;
			default:
				throw x.syntaxError("Expected a ',' or ']'");
			}
		}
	}

}