package model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Documents {

	public List<Document> documents;
	
	public Documents(){
		
	}
	
	public List<Document> getDocuments()
	{
		return this.documents;
	}
}
