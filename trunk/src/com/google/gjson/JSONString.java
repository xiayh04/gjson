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
 * Represents a JSON string.
 */
public class JSONString extends JSONValue {

	static void escape(String s, StringBuilder sb) {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F')
						|| (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
	}

	static String escapeValue(String toEscape) {
		final StringBuilder lBuf = new StringBuilder();

		lBuf.append("\"");
		escape(toEscape, lBuf);
		lBuf.append("\"");
		return lBuf.toString();
	};

	private String value;

	/**
	 * Creates a new JSONString from the supplied String.
	 * 
	 * @param value
	 *            a String value
	 * @throws NullPointerException
	 *             if <code>value</code> is <code>null</code>
	 */
	public JSONString(String value) {
		if (value == null) {
			throw new NullPointerException();
		}
		this.value = value;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof JSONString)) {
			return false;
		}
		return value.equals(((JSONString) other).value);
	}

	@Override
	public int hashCode() {
		// Just use the underlying String's hashCode.
		return value.hashCode();
	}

	/**
	 * Returns <code>this</code>, as this is a JSONString.
	 */
	@Override
	public JSONString isString() {
		return this;
	}

	/**
	 * Returns the raw Java string value of this item.
	 */
	public String stringValue() {
		return value;
	}

	/**
	 * Returns the JSON formatted value of this string, quoted for evaluating in
	 * a JavaScript interpreter.
	 */
	@Override
	public String toString() {
		return escapeValue(value);
	}

}
