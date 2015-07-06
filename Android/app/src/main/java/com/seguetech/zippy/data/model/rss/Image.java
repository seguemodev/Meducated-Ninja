package com.seguetech.zippy.data.model.rss;

import org.simpleframework.xml.Element;


@SuppressWarnings("unused")
public class Image {
	@Element(required=false)
	public String title;

	@Element(required=false)
	public String url;

	@Element(required=false)
	public String link;

	@Element(required=false)
	public String width;

	@Element(required=false)
	public String height;
}
