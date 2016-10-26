package com.tckb.data.basetypes;

/**
 * A wrapper implementation of String
 * Created by tckb on 27/09/16.
 *
 * @author tckb
 */
public class StringData implements SerializableData<StringData> {

	private String stringData;

	public StringData() {
	}

	public StringData(final byte[] bytes) {
		this.stringData = new String(bytes);
	}

	public StringData(final String stringData) {
		this.stringData = stringData;
	}

	@Override
	public void fromSerializedString(final String serializedData) {
		this.stringData = serializedData;
	}

	@Override
	public String toSerializableString() {
		return toString();
	}

	@Override
	public String toString() {
		return this.stringData;
	}

	@Override
	public int compareTo(final StringData otherStringData) {
		return this.stringData.compareTo(otherStringData.toSerializableString());
	}
}
