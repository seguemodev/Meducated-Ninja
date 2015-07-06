package com.seguetech.zippy.data.model.rss;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

@SuppressWarnings("unused")
public class Guid {
	@Attribute(required=false)
	public Boolean isPermaLink;

	@Text
	public String contents;
}