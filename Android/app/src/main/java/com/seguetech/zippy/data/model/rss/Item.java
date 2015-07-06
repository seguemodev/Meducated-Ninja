package com.seguetech.zippy.data.model.rss;

import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Element;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@DatabaseTable(tableName="news")
public class Item {
	public static final String TAG = Item.class.getSimpleName();

	@DatabaseField(id = true,columnName = "_id")
	public Long id;

	@DatabaseField
	@Element(required=false)
	public String title;

	@DatabaseField
	@Element(required=false)
	public String description;

	@DatabaseField
	@Element(required=false)
	public String link;

	@DatabaseField(dataType=DataType.DATE_LONG)
	public Date pubDate;

	@DatabaseField
	public String dateline;

	@DatabaseField
	public String image;

	public Guid guid;

	public Enclosure enclosure;

	@Element(required=false)
	public Category category;

	public Item() {}

	@SuppressWarnings("unused")
	@Element(required=true)
	public void setGuid(Guid guid) {
		this.guid = guid;
		BigInteger bigInt = new BigInteger(guid.contents.getBytes());
		this.id = bigInt.longValue();
	}

	@SuppressWarnings("unused")
	@Element(required=true)
	public Guid getGuid() {
		return this.guid;
	}


	@SuppressWarnings("unused")
	@Element(required=false)
	public void setEnclosure(Enclosure enclosure) {
		this.enclosure = enclosure;
		if (enclosure != null && enclosure.url != null) {
			this.image = enclosure.url;
		}
	}

	@SuppressWarnings("unused")
	@Element(required=false)
	public Enclosure getEnclosure() {
		return this.enclosure;
	}


	@SuppressWarnings("unused")
	@Element(name="pubDate",required=false)
	public void setDateline(String dateline) {
		this.dateline = dateline;
		DateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
		DateFormat humanDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm zzz", Locale.US);
		humanDateFormat.setTimeZone(TimeZone.getDefault());
		try {
			this.pubDate = rssDateFormat.parse(dateline);
			this.dateline = humanDateFormat.format(this.pubDate);
		}
		catch (Exception e) {
			Log.e(TAG, "Error parsing date", e);
		}
		if (this.dateline != null) {
			this.dateline = "Published: " + this.dateline;
		}
	}

	@SuppressWarnings("unused")
	@Element(name="pubDate",required=false)
	public String getDateline() {
		return this.dateline;
	}


	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", link='" + link + '\'' +
				", pubDate=" + pubDate +
				", dateline='" + dateline + '\'' +
				", image='" + image + '\'' +
				", guid=" + guid +
				", enclosure=" + enclosure +
				", category=" + category +
				'}';
	}
}
