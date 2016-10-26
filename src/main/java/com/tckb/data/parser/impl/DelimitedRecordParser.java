package com.tckb.data.parser.impl;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.RecordParser;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * A parser for delimited data
 * Created by tckb on 26/09/16.
 *
 * @author tckb
 */
public class DelimitedRecordParser<T extends SerializableData> extends RecordParser<T> {
	private final char DELIM;
	private final char DOULBE_QUOTE = '"';
	private final char SINGLE_QUOTE = '\'';


	/**
	 * @param isHeaderPresent
	 * 		if the data contains header information in the first row!
	 * @param delim
	 * 		the delimter separating the data
	 * @param thisClass
	 * 		the type of data each data field represents
	 */
	public DelimitedRecordParser(final boolean isHeaderPresent, final char delim, Class<T> thisClass) {
		super(isHeaderPresent, thisClass);
		this.DELIM = delim;
		log.info("Using delimiter: " + delim);
	}


	@Override
	public List<DataRecord<T>> parse(final File file) throws Exception {
		List<DataRecord<T>> recordList = new LinkedList<>();


		if (file.exists()) {
			// this might not be ideal if file is too large but we are not concentrating on performance here!
			for (String line : Files.readAllLines(file.toPath(), Charset.forName("UTF-8"))) {
				line += ",";
				DataRecord<T> currentRow = new DataRecord<>(new LinkedList<>(), isHeaderPresent && recordList.isEmpty());

				// read the data
				StringBuilder data = new StringBuilder();
				boolean inQuoteData = false;
				String fieldData = null;
				int fieldIx = 0;
				for (char ch : line.toCharArray()) {

					if (ch == DOULBE_QUOTE) {
						if (!inQuoteData) {
							inQuoteData = true;
						} else {
							fieldData = data.toString();
							inQuoteData = false;
						}
					} else if (ch == DELIM && !inQuoteData) {
						if (fieldData == null) {
							fieldData = data.toString();
						}
						final DataField<T> dataField;
						// do not apply validator if this is a header row
						if (!currentRow.isHeaderRow()) {

							// create the instance
							final T instance = super.mClass.newInstance();
							// load the data from the raw input
							instance.fromSerializedString(fieldData);
							// check for fieldName
							String fieldName = super.getHeaderFieldName(fieldIx);
							// create a data field
							dataField = new DataField<T>(fieldName, instance)
									.applyValidator(getValidator(fieldIx));

						} else {
							// if this is a header row do just fill in the field id
							dataField = new DataField<T>(fieldData, null);
						}
						currentRow.pushRecord(dataField);
						// reset
						fieldData = null;
						data = new StringBuilder();
						// increment the ix
						fieldIx++;
					} else {
						data.append(ch);
					}
				}

				if (currentRow.isHeaderRow()) {
					log.info("adding header to list");
					super.setHeaderRecordFields((List<DataField<T>>) currentRow.getDataFields());
				} else {
					// no header present, create and add one!
					if (!currentRow.isHeaderRow() && recordList.isEmpty()) {
						log.info("header not found, creating");

						super.setEmptyHeaderRecordFields(currentRow.getDataFields().size());
						recordList.add(new DataRecord<T>(this.headerRecordFields, true));
					}
				}

				recordList.add(currentRow);
				log.info("parsing row: " + recordList.size() + "; nr fields: " + fieldIx);


			}
			return recordList;
		}
		throw new IllegalArgumentException("File does not exist");
	}


}

