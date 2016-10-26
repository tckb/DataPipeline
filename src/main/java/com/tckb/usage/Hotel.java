package com.tckb.usage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tckb.data.basetypes.SerializableData;
import lombok.Data;

/**
 * A custom hotel object
 * Created by tckb on 28/09/16.
 */
@Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Hotel implements SerializableData<Hotel> {
	private String hotelName;
	private String hotelAddress;
	private String contact;
	private String phone;
	private String url;
	private int stars;


	@Override
	public void fromSerializedString(final String serializedData) throws Exception {
		final Hotel readValue = new ObjectMapper().readValue(serializedData, this.getClass());
		this.hotelName = readValue.hotelName;
		this.hotelAddress = readValue.hotelAddress;
		this.contact = readValue.contact;
		this.phone = readValue.phone;
		this.url = readValue.url;
		this.stars = readValue.stars;
	}

	@Override
	public String toSerializableString() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

	@Override
	public String toString() {
		return "Hotel{" +
				"hotelName='" + hotelName + '\'' +
				", hotelAddress='" + hotelAddress + '\'' +
				", contact='" + contact + '\'' +
				", phone='" + phone + '\'' +
				", url='" + url + '\'' +
				", stars=" + stars +
				'}';
	}

	@Override
	public int compareTo(final Hotel o) {
		// a very simple comparator
		return this.stars - o.getStars();
	}
}
