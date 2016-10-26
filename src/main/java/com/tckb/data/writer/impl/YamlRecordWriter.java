package com.tckb.data.writer.impl;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataRecord;
import com.tckb.data.writer.AbstractRecordWriter;
import org.yaml.snakeyaml.Yaml;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A concrete writer to serialize data into JSON
 * <p>
 * Created by tckb on 27/09/16.
 *
 * @author tckb
 */
public class YamlRecordWriter<T extends SerializableData> extends AbstractRecordWriter<T> {
	public YamlRecordWriter(String rootName, String fileExtension) {super(rootName, fileExtension);}

	@Override
	public String serialize(final Collection<DataRecord<? extends SerializableData>> dataRecords) {
		if (dataRecords.isEmpty()) {
			return "";
		} else {
			// you need to back convert the data for YAML!
			List<Map<String, String>> data = new LinkedList<>();
			for (DataRecord<?> record : dataRecords) {
				if (!record.isHeaderRow()) {
					data.add(toMap(record));
				}
			}
			return new Yaml().dumpAsMap(data);
		}
	}


	/**
	 * Convert each record to a map
	 *
	 * @param dataRecord
	 * @return
	 */
	private Map<String, String> toMap(DataRecord<?> dataRecord) {
		Map<String, String> recordMap = new HashMap<>();
		dataRecord.getDataFields()
				.forEach(dataField -> {
					try {
						recordMap.put(dataField.getFieldName(), dataField.getFieldValue().toSerializableString());
					} catch (Exception e) {
						recordMap.put(dataField.getFieldName(), "");
					}
				});
		return recordMap;
	}

}
