package com.tckb.data.parser;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataField;
import com.tckb.data.parser.impl.DataRecord;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * An abstract implementation of a record parser
 * Created by tckb on 26/09/16.
 *
 * @author tckb
 */
public abstract class RecordParser<T extends SerializableData> {
	protected final boolean isHeaderPresent;
	protected Logger log = Logger.getLogger(getClass().getName());
	// we need this because java has a silly habit of forgetting types in runtime
	// i.e., type eraser!
	protected Class<T> mClass;
	protected FieldValidator<T>[] validators = null;
	protected List<DataField<T>> headerRecordFields;

	/**
	 * @param isHeaderPresent
	 * 		if the data contains header information in the first row!
	 * @param mClass
	 * 		the type of data each data field represents
	 */
	protected RecordParser(final boolean isHeaderPresent, final Class<T> mClass) {
		log.info("Parser initializing");
		this.mClass = mClass;
		this.isHeaderPresent = isHeaderPresent;
	}

	/**
	 * parse the data from the file and validate every data field.
	 *
	 * @param file
	 * 		the file to parse the data
	 * @param validators
	 * 		- must be in order! <b>null</b> values are allowed
	 * @return list of data records
	 * @throws Exception
	 */
	public Collection<DataRecord<T>> parseWithValidators(final File file, final FieldValidator<T>... validators) throws Exception {
		log.info("Parsing file: " + file.getName() + " with fieldValidators");
		this.validators = validators;
		return parse(file);
	}

	/**
	 * Parses the file without any field validations
	 *
	 * @param file
	 * @return list of data records
	 * @throws Exception
	 */
	public abstract Collection<DataRecord<T>> parse(File file) throws Exception;


	/**
	 * returns the header field name
	 *
	 * @param fieldIx
	 * @return
	 */
	protected String getHeaderFieldName(final int fieldIx) {
		// if there is no header information available for the data, `
		// assigns a default header, HEADER_{ID}
		if (headerRecordFields == null || fieldIx > headerRecordFields.size() || headerRecordFields.get(fieldIx).getFieldName().isEmpty()) {
			return "HEADER_" + (fieldIx + 1);
		} else {
			return headerRecordFields.get(fieldIx).getFieldName();

		}
	}

	protected FieldValidator<T> getValidator(final int i) {
		if (validators != null && i < validators.length) {
			return validators[i];
		}
		return null;
	}

	/**
	 * A fallback mechanism to cover the missing headers
	 *
	 * @param headerRecordFields
	 */
	protected void setHeaderRecordFields(final List<DataField<T>> headerRecordFields) {

		for (int i = 0, headerRecordFieldsSize = headerRecordFields.size(); i < headerRecordFieldsSize; i++) {
			final DataField<T> tDataField = headerRecordFields.get(i);
			if (tDataField == null || tDataField.getFieldName().isEmpty()) {
				log.info("header for column: " + i + " missing, adding one");

				tDataField.setFieldName("HEADER_" + i);
			}
		}
		this.headerRecordFields = headerRecordFields;

	}


	/**
	 * A fallback mechanism to cover the missing headers
	 *
	 * @param size
	 */
	protected void setEmptyHeaderRecordFields(int size) {
		List<DataField<T>> headerFields = new ArrayList<>();

		for (int i = 1; i <= size; i++) {
			headerFields.add(new DataField<T>("HEADER_" + i, null));
		}

		this.headerRecordFields = headerFields;

	}


}
