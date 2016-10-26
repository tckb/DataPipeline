package com.tckb.data.writer;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * An abstract writer to serialize data
 * <p>
 * Created by tckb on 27/09/16.
 */
public abstract class AbstractRecordWriter<T extends SerializableData> implements RecordWriter<T> {
	private final String fileExtension;
	protected String rootName;
	protected Logger log = Logger.getLogger(getClass().getName());


	/**
	 * @param name
	 * 		the name to be given for the data to be processed.
	 * @param fileExtension
	 * 		the extension to be used for the data when stored to file
	 */
	protected AbstractRecordWriter(final String name, final String fileExtension) {
		log.info("Writer initializing");
		this.rootName = name;
		this.fileExtension = fileExtension;
	}


	@Override
	public String serialize(File fileTosave, Collection<DataRecord<? extends SerializableData>> dataRecords) throws IOException {
		log.info("serializing " + dataRecords.size() + " records to disk @" + fileTosave);

		fileTosave = new File(fileTosave.getParent(), fileTosave.getName() + "." + fileExtension);

		if (fileTosave.exists()) {
			// if the file exists, then create a new file with the same name
			// with date at which it has been created
			fileTosave = new File(fileTosave.getParent(), fileTosave.getName() + "-" + System.currentTimeMillis() + "." + fileExtension);
			fileTosave.createNewFile();
		}

		final String data = serialize(dataRecords);

		return Files.write(fileTosave.toPath(), data.getBytes("UTF-8")).getFileName().toString();
	}

}
