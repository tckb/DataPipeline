package com.tckb.usage;

import com.tckb.data.basetypes.StringData;
import com.tckb.data.parser.RecordParser;
import com.tckb.data.parser.impl.DataField;
import com.tckb.data.parser.impl.DataRecord;
import com.tckb.data.parser.impl.DelimitedRecordParser;
import com.tckb.data.processor.RecordProcessor;
import com.tckb.data.processor.impl.RecordFilter;
import com.tckb.data.processor.impl.SimpleRecordGrouper;
import com.tckb.data.processor.impl.SimpleRecordGrouper.GROUPING_STRATEGY;
import com.tckb.data.writer.RecordWriter;
import com.tckb.data.writer.RecordWriters;

import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Created by tckb on 29/09/16.
 */
public class TestUsage {

	/**
	 * Usage: --read-format [csv] --write-format [xml|json|md|sql|yam] /path/to/file
	 *
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Logger log = Logger.getLogger(TestUsage.class.getName());

		RecordParser<StringData> recordParser;
		RecordWriter recordWriter;
		String readFormat;
		String writeFormat;
		File readFile, writeFile;
		boolean isHeaderPresent = true;

		// a basic validation
		if (args.length < 5) {
			throw new IllegalArgumentException(printUsage());
		} else {
			if (args[0].equals("--read-format") && args[2].equals("--write-format")) {
				readFormat = args[1];
				writeFormat = args[3];
				readFile = new File(args[4]);
				writeFile = new File(readFile.getParent(), "output");
				if (args.length >= 6) {
					if (args[5].equals("--no-headers")) {
						log.info("Input assuming with no headers");
						isHeaderPresent = false;
					}
				} else {
					log.info("Input assuming headers");
				}

			} else {
				throw new IllegalArgumentException(printUsage());
			}
		}

		switch (readFormat) {
			case "csv":
				// we assume that the CSV file contains header name in the first row,
				// in other case, turn this to 'false'
				log.info("input data is in CSV");
				recordParser = new DelimitedRecordParser<>(isHeaderPresent, ',', StringData.class);
				break;
			default:
				throw new IllegalArgumentException(printUsage());
		}

		switch (writeFormat) {
			case "xml":
			case "json":
			case "sql":
			case "yaml":
				log.info("writing data into: " + writeFormat);
				recordWriter = RecordWriters.getWriter("data", writeFormat);
				break;
			case "md":
				recordWriter = new MarkdownWriter<>("data", "md");
				break;
			default:
				throw new IllegalArgumentException(printUsage());
		}


		// read the raw the data from the file
		final Collection<DataRecord<StringData>> allRecords = recordParser
				.parseWithValidators(readFile, StringFieldValidators.EMPTY_VALIDATOR, null, StringFieldValidators.RATING_VALIDATOR, StringFieldValidators.EMPTY_VALIDATOR, null, StringFieldValidators.URL_VALIDATOR);

		log.info("processing data");

		// optionally, group and filter the records
		// below processor will group the records based on 3rd column and filters the records
		// whose rating is less than 3

		// define the processors
		RecordProcessor<StringData> highRatingHotelsProcessor = new SimpleRecordGrouper<>(GROUPING_STRATEGY.DSC, 3);

		// daisy-chaining of the processor
		final boolean finalIsHeaderPresent = isHeaderPresent;
		highRatingHotelsProcessor.setNext(new RecordFilter<>(record -> {
			// filter the record if the rating is less than 3
			for (DataField<StringData> field : record.getDataFields()) {
				// this is field header name!
				if (finalIsHeaderPresent) {
					if (field.getFieldName().contains("stars")) {
						return Integer.parseInt(field.getFieldValue().toSerializableString()) > 3;
					}
				} else {
					// in case, there is no header provided in the file, parser creates one
					// in that case we need to look for column number in the header name
					if (field.getFieldName().contains("3")) {
						return Integer.parseInt(field.getFieldValue().toSerializableString()) > 3;
					}
				}


			}
			// if there is no rating column, remove it!
			return false;

		}));


		// apply the processor
		final Collection<DataRecord<StringData>> onlyHighRankingHotels = highRatingHotelsProcessor.doProcess(allRecords);

		log.info("processing finished");

		log.info("writing data");

		// save all records
		String fileSaved = recordWriter.serialize(writeFile, allRecords);
		System.out.println("File saved at: " + fileSaved);

		// save only highly rated hotels
		fileSaved = recordWriter.serialize(new File(writeFile + "_high_ranked"), onlyHighRankingHotels);
		System.out.println("File saved at: " + fileSaved);


	}

	public static String printUsage() {
		return "\nUsage:\njava -jar <this_jar_file>.jar --read-format [csv] --write-format [xml|json|md|sql|yam] /path/to/file [--no-headers]\n";
	}

}
