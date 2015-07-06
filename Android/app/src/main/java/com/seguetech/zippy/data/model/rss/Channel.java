package com.seguetech.zippy.data.model.rss;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;

import java.util.List;


@NamespaceList({
		@Namespace(reference="http://www.w3.org/2005/Atom",prefix="atom")
})
@SuppressWarnings("unused")
public class Channel {
	@Element(required=false)
	String title;

	@Element(required=false)
	String description;

	@ElementList(name="link",inline=true,required=false)
	public List<Link> links;

	@Element(required=false)
	public Image image;

	@Element(required=false)
	public String language;

	@Element(required=false)
	public String webMaster;

	@ElementList(inline=true,required=false)
	public List<Category> categorys;

	@ElementList(inline=true,required=false)
	public List<Item> items;

	@Element(required=false)
	String lastBuildDate;

}
