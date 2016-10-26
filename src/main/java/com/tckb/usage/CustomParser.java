package com.tckb.usage;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.RecordParser;
import com.tckb.data.parser.impl.DataField;
import com.tckb.data.parser.impl.DataRecord;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A custom parser for storing the hotels from different partners the following format:
 * <p>
 * partner_1_hotels[CR]
 * partner_2_hotels[CR]
 * ..
 * <p>
 * where partner_n_hotels = [base64_hotel_1_json]$$@@[base64_hotel_2_json]$$@@ ...
 * <p>
 * Created by tckb on 29/09/16.
 */
public class CustomParser<T extends SerializableData> extends RecordParser<T> {

	final String fieldSeperator = "\\$\\$@@";

	public CustomParser(final boolean isHeaderPresent, final Class<T> mClass) {
		super(isHeaderPresent, mClass);
	}

	@Override
	public Collection<DataRecord<T>> parse(final File file) throws Exception {
		List<DataRecord<T>> recordList = new LinkedList<>();

		if (file.exists()) {
			Files.lines(file.toPath())
					.forEachOrdered(line -> {
						List<DataField<T>> fieldList = new LinkedList<>();

						final String[] dataFields = line.split(fieldSeperator);
						for (String serializedData : dataFields) {
							try {
								T data = mClass.newInstance();
								data.fromSerializedString(new String(Base64.getDecoder().decode(serializedData)));
								fieldList.add(new DataField<T>("data", data));
							} catch (Exception e) {
								// ignore the data
							}
						}
						recordList.add(new DataRecord<T>(fieldList, false));
					});

			return recordList;
		}
		throw new IllegalArgumentException("File does not exist");
	}
}
