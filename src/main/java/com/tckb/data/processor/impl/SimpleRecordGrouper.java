package com.tckb.data.processor.impl;

import com.tckb.data.basetypes.SerializableData;
import com.tckb.data.parser.impl.DataField;
import com.tckb.data.parser.impl.DataRecord;
import com.tckb.data.processor.RecordProcessor;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Created by tckb on 27/09/16.
 *
 * @author tckb
 */
public class SimpleRecordGrouper<T extends SerializableData> implements RecordProcessor<T> {

	private final int groupingColumn;
	private final GROUPING_STRATEGY groupingOrder;
	private RecordProcessor<T> nextOne = null;

	/**
	 * @param strategy
	 * 		the grouping strategy to apply. see @{@link SimpleRecordGrouper.GROUPING_STRATEGY}
	 * @param groupingColumn
	 * 		the column to be used for grouping entries. index starts with 1
	 */

	public SimpleRecordGrouper(GROUPING_STRATEGY strategy, int groupingColumn) {
		this.groupingColumn = groupingColumn;
		this.groupingOrder = strategy;
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

	// this probably not the best way to group!
	private Collection<DataRecord<T>> doGrouping(final Collection<DataRecord<T>> inputDataRecords) {
		// for invalid grouping column
		if (groupingColumn < 0 || inputDataRecords.size() < groupingColumn) {
			return inputDataRecords;
		}


		return inputDataRecords.stream()
				.sorted((record1, record2) -> {
					int result = 0;

					if (record1.isHeaderRow() || record2.isHeaderRow()) {
						return result;
					}

					// not the ideal way!
					int ix = 0;
					Iterator<DataField<T>> record2Iterator = record2.getDataFields().iterator();
					for (Iterator<DataField<T>> record1Iterator = record1.getDataFields().iterator(); record1Iterator.hasNext() && record2Iterator.hasNext(); ix++) {
						final DataField<T> field1 = record1Iterator.next();
						final DataField<T> field2 = record2Iterator.next();
						if (ix == (groupingColumn - 1)) {
							result = field1.getFieldValue().compareTo(field2.getFieldValue());

							if (groupingOrder.equals(GROUPING_STRATEGY.DSC)) {
								result = result * -1;
							}

						}
					}
					return result;

				}).collect(Collectors.toList());
	}

	/**
	 * the grouping strategy used
	 */
	public enum GROUPING_STRATEGY {
		// ascending
		ASC,
		// descending
		DSC
	}
}
