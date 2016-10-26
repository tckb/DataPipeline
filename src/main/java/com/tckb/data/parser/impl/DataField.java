package com.tckb.data.parser.impl;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.FieldValidator;
import lombok.Data;

import java.util.logging.Logger;

/**
 * a basic field of a record
 * Created by tckb on 26/09/16.
 *
 * @author tckb
 */
@Data
public class DataField<T extends SerializableData> {
	protected Logger log = Logger.getLogger(getClass().getName());
	private T fieldValue;
	private String fieldName;


	/**
	 * Initialize the field with the data
	 *
	 * @param name
	 * 		a sensible name for this data. aka HeaderName
	 * @param data
	 * 		the data itself
	 */
	public DataField(String name, T data) {
		this.fieldName = name;
		this.fieldValue = data;
	}

	/**
	 * Applyes the the validator and if the validation fails, replaces the current value
	 * to the fallback
	 *
	 * @param validator
	 * 		the validator
	 * @return this object
	 */
	public DataField<T> applyValidator(FieldValidator<T> validator) {
		log.info("applying validator to the datafield");
		if (validator != null) {
			if (!validator.isValidData(fieldValue)) {
				fieldValue = validator.validationFallbackValue(fieldValue);
			}
		}
		return this;
	}

	@Override
	public String toString() {
		return "{" + this.fieldName + ":" + fieldValue + "}";
	}
}
