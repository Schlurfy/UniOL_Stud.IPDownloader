package model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Semesters {

	public List<Semester> semesters;
	
	public Semesters(){
		
	}
	
	public List<Semester> getSemesters()
	{
		return this.semesters;
	}
}
