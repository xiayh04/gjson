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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an array of {@link JSONValue} objects.
 */
public class JSONArray extends JSONValue {

	private final List<JSONValue> array = new ArrayList<JSONValue>();

	/**
	 * Creates an empty JSONArray.
	 */
	public JSONArray() {

	}

	/**
	 * Returns <code>true</code> if <code>other</code> is a {@link JSONArray}
	 * wrapping the same underlying object.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof JSONArray)) {
			return false;
		}
		return array.equals(((JSONArray) other).array);
	}

	/**
	 * Returns the value at the specified index position.
	 * 
	 * @param index
	 *            the index of the array item to retrieve
	 * @return the value at this index, or <code>null</code> if this index is
	 *         empty
	 */
	public JSONValue get(int index) {
		return array.get(index);
	};

	@Override
	public int hashCode() {
		return array.hashCode();
	}

	/**
	 * Returns <code>this</code>, as this is a JSONArray.
	 */
	@Override
	public JSONArray isArray() {
		return this;
	}

	/**
	 * Sets the specified index to the given value.
	 * 
	 * @param index
	 *            the index to set
	 * @param value
	 *            the value to set
	 * @return the previous value at this index, or <code>null</code> if this
	 *         index was empty
	 */
	public JSONValue set(int index, JSONValue value) {
		if (index < array.size()) {
			JSONValue previous = get(index);
			array.set(index, value);
			return previous;
		} else if (index == array.size()) {
			array.add(value);
			return null;
		} else {
			throw new IllegalArgumentException("index hole not allowed.");
		}
	}

	/**
	 * Sets the specified index to the given value.
	 * 
	 * @param index
	 *            the index to set
	 * @param value
	 *            the value to set
	 * @return the previous value at this index, or <code>null</code> if this
	 *         index was empty
	 */
	public void add(JSONValue value) {
		array.add(value);
	}

	/**
	 * Returns the number of elements in this array.
	 * 
	 * @return size of this array
	 */
	public int size() {
		return array.size();
	};

	/**
	 * Create the JSON encoded string representation of this JSONArray instance.
	 * This method may take a long time to execute if the underlying array is
	 * large.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0, c = size(); i < c; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(get(i));
		}
		sb.append("]");
		return sb.toString();
	}

}
