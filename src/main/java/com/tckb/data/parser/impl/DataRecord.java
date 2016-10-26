package com.tckb.data.parser.impl;

import com.tckb.data.basetypes.SerializableData;

import java.util.Collection;

/**
 * a basic data record
 * <p>
 * A data record is composed of DataField:<p>
 * <p>
 * Created by tckb on 26/09/16.
 *
 * @author tckb
 */
public class DataRecord<T extends SerializableData> {

	private final Collection<DataField<T>> dataFields;
	// this could be transient
	private final boolean isHeaderRow;

	/**
	 * Initializes the data record with the collection of the fields
	 *
	 * @param dataFields
	 * 		collection of fields
	 * @param isHeaderRow
	 * 		tells if this row is the header row, describing the fields
	 */
	public DataRecord(final Collection<DataField<T>> dataFields, final boolean isHeaderRow) {
		this.dataFields = dataFields;
		this.isHeaderRow = isHeaderRow;
	}

	/**
	 * pushes the datafield into the collection
	 *
	 * @param dataField
	 */
	void pushRecord(DataField<T> dataField) {
		dataFields.add(dataField);
	}

	/**
	 * return the datafiles
	 *
	 * @return internal data fields
	 */
	public Collection<DataField<T>> getDataFields() {
		//TODO: techinically, this should return an unmofiable collection!
		//return Collections.unmodifiableCollection(dataFields);
		return dataFields;
	}

	/**
	 * @return is this row a header row?
	 */
	public boolean isHeaderRow() {
		return isHeaderRow;
	}

	@Override
	public String toString() {
		return "[" + dataFields.toString() + "]";
	}
}
