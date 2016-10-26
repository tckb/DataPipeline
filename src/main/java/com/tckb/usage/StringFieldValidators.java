package com.tckb.usage;

import com.tckb.data.basetypes.StringData;
import com.tckb.data.parser.FieldValidator;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * Created by tckb on 26/09/16.
 */
public interface StringFieldValidators {
	FieldValidator<StringData> UTF8_VALIDATOR = new FieldValidator<StringData>() {
		public boolean isValidData(final StringData fieldValue) {
			return true;
		}

		public StringData validationFallbackValue(final StringData fieldValue) {
			try {
				return new StringData(fieldValue.toSerializableString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				return fieldValue;
			}
		}
	};
	// an empty validator
	FieldValidator<StringData> EMPTY_VALIDATOR = new FieldValidator<StringData>() {
		public boolean isValidData(final StringData fieldValue) {
			return true;
		}

		public StringData validationFallbackValue(final StringData fieldValue) {
			return fieldValue;
		}
	};
	// extracted from AOSP
	String URL_PATTERN = "((?:(http|https|Http|Https|rtsp|Rtsp):" +
			"\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)" +
			"\\,\\;\\?\\&amp;\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_" +
			"\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&amp;\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?" +
			"((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+" +   // named host
			"(?:" +   // plus top level domain
			"(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])" +
			"|(?:biz|b[abdefghijmnorstvwyz])" +
			"|(?:cat|com|coop|c[acdfghiklmnoruvxyz])" +
			"|d[ejkmoz]" +
			"|(?:edu|e[cegrstu])" +
			"|f[ijkmor]" +
			"|(?:gov|g[abdefghilmnpqrstuwy])" +
			"|h[kmnrtu]" +
			"|(?:info|int|i[delmnoqrst])" +
			"|(?:jobs|j[emop])" +
			"|k[eghimnrwyz]" +
			"|l[abcikrstuvy]" +
			"|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])" +
			"|(?:name|net|n[acefgilopruz])" +
			"|(?:org|om)" +
			"|(?:pro|p[aefghklmnrstwy])" +
			"|qa" +
			"|r[eouw]" +
			"|s[abcdeghijklmnortuvyz]" +
			"|(?:tel|travel|t[cdfghjklmnoprtvwz])" +
			"|u[agkmsyz]" +
			"|v[aceginu]" +
			"|w[fs]" +
			"|y[etu]" +
			"|z[amw]))" +
			"|(?:(?:25[0-5]|2[0-4]" + // or ip address
			"[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]" +
			"|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]" +
			"[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}" +
			"|[1-9][0-9]|[0-9])))" +
			"(?:\\:\\d{1,5})?)" + // plus option port number
			"(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&amp;\\=\\#\\~" +  // plus option query params
			"\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?" +
			"(?:\\b|$)";
	FieldValidator<StringData> URL_VALIDATOR = new FieldValidator<StringData>() {
		public boolean isValidData(final StringData fieldValue) {
			return Pattern.matches(URL_PATTERN, fieldValue.toSerializableString());

		}

		public StringData validationFallbackValue(final StringData fieldValue) {
			return new StringData(fieldValue.toSerializableString());
		}
	};
	String RATING_PATTERN = "[0-5]";
	FieldValidator<StringData> RATING_VALIDATOR = new FieldValidator<StringData>() {
		public boolean isValidData(final StringData fieldValue) {
			return Pattern.matches(RATING_PATTERN, fieldValue.toSerializableString());
		}

		public StringData validationFallbackValue(final StringData fieldValue) {
			int rating = 0;
			try {
				rating = Integer.parseInt(fieldValue.toSerializableString());
				// boundary conditions
				if (rating > 5) {
					rating = 5;
				} else {
					if (rating < 0) { rating = 0; }
				}
			} catch (NumberFormatException exception) {
				// bump it! skip it!
			}
			return new StringData("" + rating);
		}
	};

}
