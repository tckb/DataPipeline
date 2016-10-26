package com.tckb.data.basetypes;

import java.io.Serializable;

/**
 * the base type for any data to be parser - processed - written
 * Created by tckb on 27/09/16.
 *
 * @author tckb
 */
public interface SerializableData<T> extends Serializable, Comparable<T> {

	/**
	 * An implementation where the serialized data must be used to populate the
	 * internal states
	 *
	 * @param serializedData
	 * 		the string representation of this type
	 */
	void fromSerializedString(String serializedData) throws Exception;

	/**
	 * return a serialized version of the type
	 *
	 * @return a string `representation` of this custom type
	 */
	String toSerializableString() throws Exception;


}