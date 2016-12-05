package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown	= true)
public class Document {

	public String document_id;
	public String name;
	public String filename;
	
	public Document()
	{
		
	}

	@Override
	public String toString()
	{
		return this.filename;
	}

}
