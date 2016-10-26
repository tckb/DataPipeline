package com.tckb.data.writer;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataRecord;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by tckb on 27/09/16.
 */
public interface RecordWriter<T> {

	/**
	 * Serializes the data to a string
	 *
	 * @param dataRecords
	 * 		the datarecords to be serialized
	 * @return the serialzied data
	 */
	String serialize(Collection<DataRecord<? extends SerializableData>> dataRecords);

	/**
	 * Serializes the dataRecords to a file
	 *
	 * @param fileTosave
	 * 		the file to write the data.
	 * 		<p>
	 * 		<b>NOTE</b>: if the file already exists, then file is *not* overridden, instead
	 * 		a new file is created with the same name and the data is written to it.
	 * @param dataRecords
	 * @return the absolute path to the file that has been created
	 * @throws IOException
	 * @implSpec Please follow the above standard! if the file already exists, then file should *not* be overridden, instead a new file
	 * must be created with the same name and the data should be written to it.
	 */
	String serialize(File fileTosave, Collection<DataRecord<? extends SerializableData>> dataRecords) throws IOException;
}


