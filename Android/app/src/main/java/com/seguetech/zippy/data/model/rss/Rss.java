package com.seguetech.zippy.data.model.rss;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


@SuppressWarnings("unused")
public class Rss {
	@Attribute(required=true)
	public String version;

	@Element
	public Channel channel;
}