package com.tckb.data.writer.impl;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataRecord;
import com.tckb.data.writer.AbstractRecordWriter;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

import java.util.Collection;

/**
 * A concrete writer to serialize data into XML
 * <p>
 * Created by tckb on 27/09/16.
 *
 * @author tckb
 */
public class XmlRecordWriter<T extends SerializableData> extends AbstractRecordWriter<T> {
	public XmlRecordWriter(String rootName, String fileExtension) {super(rootName, fileExtension);}

	@Override
	public String serialize(final Collection<DataRecord<? extends SerializableData>> dataRecords) {
		if (dataRecords.isEmpty()) {
			return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n";
		} else {
			try {
				Directives root = new Directives().add("root");
				for (DataRecord<?> record : dataRecords) {
					if (!record.isHeaderRow()) {
						final Directives rootdata = root.add(super.rootName);
						record.getDataFields()
								.forEach(dataField -> {
									try {
										rootdata.add(dataField.getFieldName())
												.set(dataField.getFieldValue().toSerializableString())
												.up();
									} catch (Exception e) {
										rootdata.add(dataField.getFieldName())
												.set("")
												.up();
									}
								});
						rootdata.up();
					}
				}
				return new Xembler(root).xml();
			} catch (ImpossibleModificationException e) {
				return "";
			}
		}
	}
}
