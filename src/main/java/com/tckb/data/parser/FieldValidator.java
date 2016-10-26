package com.tckb.data.parser;

/**
 * A validator for datafield
 * Created by tckb on 26/09/16.
 *
 * @author tckb
 */
public interface FieldValidator<T> {
	/**
	 * defines if the fieldValue is valid
	 *
	 * @param fieldValue
	 * 		the fieldValue to be valuated
	 * @return true - if valid, false otherwise
	 */
	boolean isValidData(T fieldValue);

	/**
	 * incase the data is not valid, supply a fallback in order to avoid data corruption
	 *
	 * @param fieldValue
	 * @return
	 */
	T validationFallbackValue(T fieldValue);
}
