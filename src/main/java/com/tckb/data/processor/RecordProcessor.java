package com.tckb.data.processor;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataRecord;

import java.util.Collection;

/**
 * A non-mutating DataRecord processor
 * Created by tckb on 28/09/16.
 *
 * @author tckb
 */
public interface RecordProcessor<T extends SerializableData> {

	/**
	 * Add the next processor
	 *
	 * @param nextInChain
	 * 		the processor which will be called next once it has processed the data
	 */
	void setNext(RecordProcessor<T> nextInChain);

	/**
	 * process the data records and returns a copy of the processed data
	 *
	 * @param inputDataRecords
	 * 		the data records to be processed
	 * @return the processed the data
	 */
	Collection<DataRecord<T>> doProcess(Collection<DataRecord<T>> inputDataRecords);

}
