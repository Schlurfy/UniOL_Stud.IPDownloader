package service.REST;

import java.io.IOException;
import model.Courses;
import model.Documents;
import model.Semesters;
import model.StudIPApi;

import org.scribe.model.*;
import org.scribe.oauth.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class REST
{ 
	
	private StudIPApi api = new StudIPApi();	
	
	/**
	 * 
	 * @param accessToken Autorisierung
	 * @param service service gegen denn der call gerichtet wird
	 * @return Antwort in JSON
	 * 
	 * @author Felix
	 */
	public Semesters getSemester(Token accessToken, OAuthService service )
	{
		OAuthRequest request = new OAuthRequest(Verb.GET, api.getRecourceUrl("/api/semesters"));
					
		service.signRequest(accessToken, request);
			    
		Response response = request.send();

		Semesters semesters = null;		
	
		if(response.getCode() == 200)
		{
			ObjectMapper mapper = new ObjectMapper();
			
			String semesterJSON = response.getBody();
			try {
				semesters = mapper.readValue(semesterJSON, Semesters.class);
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return semesters;
	}
	
	/**
	 * 
	 * @param accessToken Autorisierung
	 * @param service service gegen denn der call gerichtet wird
	 * @param semesterId id des semester aus dem die Veranstaltungen zurueckgeben werden sollen
	 * @return Antwort in JSON
	 * 
	 * @author Felix
	 */
	public Courses getSemesterCourses(Token accessToken, OAuthService service, String semesterId)
	{
		OAuthRequest request = new OAuthRequest(Verb.GET, api.getRecourceUrl("/api/courses/semester/"+semesterId));
	
		service.signRequest(accessToken, request);
			    
		Response response = request.send();

		Courses courses = null;		
		
		if(response.getCode() == 200)
		{	
			ObjectMapper mapper = new ObjectMapper();
		
			String coursesJSON = response.getBody();
			try {
				courses = mapper.readValue(coursesJSON, Courses.class);				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
		
		return courses;
	}
	
	
	/**
	 * 
	 * @param accessToken Autorisierung
	 * @param service service gegen denn der call gerichtet wird
	 * @param semesterId id des semester aus dem die Veranstaltungen zurueckgeben werden sollen
	 * @return Antwort in JSON
	 * 
	 * @author Felix
	 */
	public Courses getCourses(Token accessToken, OAuthService service)
	{
		OAuthRequest request = new OAuthRequest(Verb.GET, api.getRecourceUrl("/api/courses"));
	
		service.signRequest(accessToken, request);
			    
		Response response = request.send();

		Courses courses = null;		
		
		if(response.getCode() == 200)
		{	
			ObjectMapper mapper = new ObjectMapper();
		
			String coursesJSON = response.getBody();
			
			try {
				courses = mapper.readValue(coursesJSON, Courses.class);				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
		
		return courses;
	}
	
	
	/**
	 * 
	 * @param accessToken Autorisierung
	 * @param service service gegen denn der call gerichtet wird
	 * @return Antwort in JSON
	 * 
	 * @author Felix
	 */
	public Documents getDocumentsOfCourse(Token accessToken, OAuthService service, String courseID)
	{
		OAuthRequest request = new OAuthRequest(Verb.GET, api.getRecourceUrl("/api/documents/"+courseID+"/new"));
		
		service.signRequest(accessToken, request);
	
		Response response = request.send();
		
		Documents retVal = null;
		
		if(response.getCode() == 200)
		{		
			String documentsJSON = response.getBody();	
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				if(documentsJSON == null)
					return null;
				retVal = mapper.readValue(documentsJSON, Documents.class);	
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		return retVal;
	}
	
	/**
	 * 
	 * @param accessToken Autorisierung
	 * @param service service gegen denn der call gerichtet wird
	 * @return Antwort in JSON
	 * 
	 * @author Felix
	 */
	public Response getDocument(Token accessToken, OAuthService service, String documentID)
	{
		OAuthRequest request = new OAuthRequest(Verb.GET, api.getRecourceUrl("/api/documents/"+documentID+"/download"));
		
		service.signRequest(accessToken, request);
	
		Response response = request.send();
		
		return response;
	}
}
