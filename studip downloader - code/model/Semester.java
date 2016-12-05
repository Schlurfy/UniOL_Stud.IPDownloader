package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Semester {

	public String semester_id;
	public String title;
	public String end;
	public String begin;
	public Courses courses;
	
	public Semester()
	{	
		
	}

	@Override
	public String toString()
	{
		return this.title;
	}
	
	public String toDownloadString()
	{
		return this.title.replace("/", "-");
	}
	
	public String getID()
	{
		return this.semester_id;
	}
	
	public void addCourse(Course c)
	{
		this.courses.addCourse(c);
	}
}
