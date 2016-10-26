package com.tckb.data.processor.impl;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataRecord;
import com.tckb.data.processor.RecordProcessor;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * the record filter processor
 * Created by tckb on 27/09/16.
 *
 * @author tckb
 */
public class RecordFilter<T extends SerializableData> implements RecordProcessor<T> {

	private final Predicate<DataRecord<T>> filterCondition;
	private RecordProcessor<T> nextInChain = null;

	/**
	 * @param filterCondition
	 * 		the condition to be used for processing
	 */
	public RecordFilter(Predicate<DataRecord<T>> filterCondition) {
		this.filterCondition = filterCondition;
	}

	@Override
	public void setNext(final RecordProcessor<T> nextInChain) {
		this.nextInChain = nextInChain;

	}

	@Override
	public Collection<DataRecord<T>> doProcess(final Collection<DataRecord<T>> inputDataRecords) {
		Collection<DataRecord<T>> groupedDataRecords = doFilter(inputDataRecords);

		if (nextInChain != null) {
			return nextInChain.doProcess(groupedDataRecords);
		}
		return groupedDataRecords;
	}

	private Collection<DataRecord<T>> doFilter(final Collection<DataRecord<T>> inputDataRecords) {
		return inputDataRecords
				.stream()
				.filter(tDataRecord -> {
					// preserve the header record!
					if (!tDataRecord.isHeaderRow()) {
						return filterCondition.test(tDataRecord);
					}
					return true;
				})
				// to preserve the order
				.collect(Collectors.toList());
	}
}
