package com.tckb.usage;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataField;
import com.tckb.data.parser.impl.DataRecord;
import com.tckb.data.writer.AbstractRecordWriter;

import java.util.Collection;

/**
 * this writer writes the data into markdown format
 * <p>
 * Markdown specs imported from: https://guides.github.com/features/mastering-markdown/
 * Created by tckb on 28/09/16.
 *
 * @author tckb
 */
public class MarkdownWriter<T extends SerializableData> extends AbstractRecordWriter<T> {

	public MarkdownWriter(final String name, final String fileExtension) {
		// ignore the extension given
		super(name, "md");
	}


	@Override
	public String serialize(final Collection<DataRecord<? extends SerializableData>> dataRecords) {

		StringBuilder rootData = new StringBuilder();
		rootData.append("# Custom writer Demo").append("\n")
				.append("here's a how a sample data can be written ").append("\n");

		rootData.append("## ").append(super.rootName).append("\n\n");
		dataRecords
				.forEach(dataRecord -> {
					StringBuilder recordData = new StringBuilder();

					for (DataField<?> dataField : dataRecord.getDataFields()) {
						if (dataRecord.isHeaderRow()) {
							recordData.append("|").append(dataField.getFieldName());
						} else {
							try {
								recordData.append("|").append(dataField.getFieldValue().toSerializableString());
							} catch (Exception e) {
								// ignore the value if the serialization fails
								recordData.append("").append("|");
							}
						}
					}
					recordData.append("|\n");

					// write the header line
					if (dataRecord.isHeaderRow()) {
						for (int i = 0; i < dataRecord.getDataFields().size(); i++) {
							recordData.append("|").append("--");
						}
						recordData.append("\n");
					}
					rootData.append(recordData);
				});
		return rootData.toString();
	}

}