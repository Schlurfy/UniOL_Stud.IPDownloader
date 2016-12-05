package model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Courses {

		public List<Course> courses = new ArrayList<Course>();
		
		public Courses(){
			
		}
		
		public List<Course> getCourses()
		{
			return this.courses;
		}
		
		public void addCourse(Course c)
		{
			this.courses.add(c);
		}
		
		public boolean removeElement(Course c)
		{
			if(this.courses.contains(c))
			{
				this.courses.remove(c); 
				return true;
			}
			else				
				return false;
		}
}
