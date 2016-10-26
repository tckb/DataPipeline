package com.tckb.data.writer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataRecord;
import com.tckb.data.writer.AbstractRecordWriter;

import java.util.Collection;

/**
 * A concrete writer to serialize data into JSON
 * <p>
 * Creatd by tckb on 27/09/16.
 *
 * @author tckb
 */
public class JsonRecordWriter<T extends SerializableData> extends AbstractRecordWriter<T> {
	public JsonRecordWriter(String rootName, String fileExtension) {super(rootName, fileExtension);}

	@Override
	public String serialize(final Collection<DataRecord<? extends SerializableData>> dataRecords) {
		if (dataRecords.isEmpty()) {
			return "{}";
		} else {

			final ArrayNode arrayNode = new ObjectMapper().createArrayNode();
			for (DataRecord<?> record : dataRecords) {
				final ObjectNode objectNode = new ObjectMapper().createObjectNode();
				if (!record.isHeaderRow()) {

					record.getDataFields()
							.forEach(dataField -> {
								try {
									objectNode.put(dataField.getFieldName(), dataField.getFieldValue().toSerializableString());
								} catch (Exception e) {
									objectNode.put(dataField.getFieldName(), "");
								}
							});
					arrayNode.add(objectNode);
				}
			}
			return arrayNode.toString();
		}
	}
}
