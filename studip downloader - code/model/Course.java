package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {

	public String course_id;
	public String title;
	public String semester_id;
	
	public Course()
	{
		
	}
	
	@Override
	public String toString()
	{
		return this.title;
	}
	
	public String getCourseID()
	{
		return this.course_id;
	}
	
	public String getSemID()
	{
		return this.semester_id;
	}
}
