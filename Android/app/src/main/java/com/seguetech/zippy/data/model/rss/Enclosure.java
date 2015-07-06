package com.seguetech.zippy.data.model.rss;

import org.simpleframework.xml.Attribute;


@SuppressWarnings("unused")
public class Enclosure {
	@Attribute(required=false)
	public String url;

	@Attribute(required=false)
	public String length;

	@Attribute(name="type",required=false)
	public String contentType;

	public Enclosure() {}
}
