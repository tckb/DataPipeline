package com.tckb.usage;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataField;
import com.tckb.data.parser.impl.DataRecord;
import com.tckb.data.writer.AbstractRecordWriter;

import java.util.Base64;
import java.util.Collection;

/**
 * A custom writer for storing the hotels from different partners the following format:
 * <p>
 * partner_1_hotels[CR]
 * partner_2_hotels[CR]
 * ..
 * <p>
 * where partner_n_hotels = [base64_hotel_1_json]$$@@[base64_hotel_2_json]$$@@ ...
 * <p>
 * Created by tckb on 29/09/16.
 */
public class CustomerWriter<T extends SerializableData> extends AbstractRecordWriter<T> {

	public CustomerWriter(final String name, final String fileExtension) {
		super(name, fileExtension);
	}


	@Override
	public String serialize(final Collection<DataRecord<? extends SerializableData>> dataRecords) {

		StringBuilder rootData = new StringBuilder();
		dataRecords
				.forEach(dataRecord -> {
					for (DataField<?> dataField : dataRecord.getDataFields()) {
						try {
							rootData.append(Base64.getEncoder().encodeToString(dataField.getFieldValue().toSerializableString().getBytes("UTF-8"))).append("$$@@");
						} catch (Exception e) {
							// ignore the value if the serialization fails
						}
					}
					rootData.append("\n");
				});
		return rootData.toString();
	}

}