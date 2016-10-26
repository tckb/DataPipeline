package com.tckb.data.processor.impl;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataRecord;
import com.tckb.data.processor.RecordProcessor;
import com.tckb.data.processor.impl.SimpleRecordGrouper.GROUPING_STRATEGY;

import java.util.Collection;

/**
 * Created by tckb on 28/09/16.
 *
 * @author tckb
 * @apiNote This is not fully implemented yet.
 */
public class MultiGroupingGrouper<T extends SerializableData> implements RecordProcessor<T> {
	private final int[] groupingColumns;
	private final GROUPING_STRATEGY groupingOrder;
	private final boolean retainHeaderIfPresent;
	private RecordProcessor<T> nextOne = null;

	/**
	 * @param strategy
	 * @param groupingColumns
	 * @param retainHeaderIfPresent
	 */
	public MultiGroupingGrouper(final GROUPING_STRATEGY strategy, final boolean retainHeaderIfPresent, final int... groupingColumns) {
		this.groupingColumns = groupingColumns;
		this.groupingOrder = strategy;
		this.retainHeaderIfPresent = retainHeaderIfPresent;
	}

	@Override
	public void setNext(final RecordProcessor<T> nextInChain) {
		this.nextOne = nextInChain;
	}

	@Override
	public Collection<DataRecord<T>> doProcess(final Collection<DataRecord<T>> inputDataRecords) {
		Collection<DataRecord<T>> groupedDataRecords = doGrouping(inputDataRecords);

		if (nextOne != null) {
			return nextOne.doProcess(groupedDataRecords);
		}
		return groupedDataRecords;
	}

	private Collection<DataRecord<T>> doGrouping(final Collection<DataRecord<T>> inputDataRecords) {
		// for invalid grouping column
		if (groupingColumns.length == 0 || inputDataRecords.size() < groupingColumns.length) {
			return inputDataRecords;
		}

		// this is just a thought!
		// TODO: do a multi-level grouping here!

		return inputDataRecords;
	}
}
