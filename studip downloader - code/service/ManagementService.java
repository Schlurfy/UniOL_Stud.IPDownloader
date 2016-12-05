package service;


import java.io.File;
import java.io.IOException;






import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import model.*;
import service.IO.IO;
import service.OAuth.OAuth;
import service.REST.REST;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Klasse koordiniert komunikation zwischen den Modulen OAuth, REST, StudIPUser, IO und Events
 * 
 * Zudem werden die Objekte zu StudIPUser und OAuth angelegt.
 * 
 * @author Felix, Fabian
 *
 */
public class ManagementService
{ 
	/**
	 * Genutztes StudIPUSer-Objekt
	 * 
	 * @author Fabian
	 */
	private static StudIPUser user;
	
	public static StudIPUser getUser(){
		if(user==null){
			user = new StudIPUser();
		}
		
		return user;
	}
	
	
	/**
	 * Genutztes OAuth-Objekt
	 * 
	 * @author Fabian
	 */
	private static OAuth oauth;
	
	public static OAuth getOAuth(){
		if(oauth==null){
			oauth = new OAuth();
		}
		return oauth;
	}
	
	/**
	 * Genutztes REST-Objekt
	 * 
	 * @author Fabian
	 */
	private static REST rest;
	
	public static REST getREST(){
		if(rest==null){
			rest = new REST();
		}
		return rest;
	}
	
	/**
	 * Genutztes IO-Objekt
	 * 
	 * @author Fabian
	 */
	private static IO io;
	
	public static IO getIO(){
		if(io==null){
			io = new IO();
		}
		return io;
	}
	
	public static void OAuthTest()
	{
		service.OAuth.OAuthTmpTest.login();
	}
	
	/**
	 * Genutztes StudIPUSer-Objekt
	 * @return return oauth url 
	 * @author Fabian
	 */
	public static String login()
	{
		addLog("Beginne Login des Users ...");
		return getOAuth().login();
	}
	
	public static boolean loginReady(String url)
	{
		boolean retVal = getOAuth().loginReady(url);
				
		if(retVal)
			getUser().setAccessToken(user.getAccessToken());			
		
		return retVal;
	}
	

	/**
	 * Gibt die Kurse des Studenten des aktuellen Semester zurueck
	 * @return Courses[] Kurse des Studentens
	 */
	public static Semesters getSemesters()
	{		
		addLog("Gibt Semester zurueck");
		return getREST().getSemester(getUser().getAccessToken(), getOAuth().oauthService);
	}
	
	/**
	 * Gibt die Kurse des Studenten des aktuellen Semester zurueck
	 * @param semesterID Semester zu dem die Kurse zurueck gegebn werden sollen
	 * @return Courses[] Kurse des Studentens
	 */
	public static Courses getSemesterCourses(String semesterID)
	{		
		addLog("Gibt die Kurse des Semesters: " + semesterID + " zurueck");
		return getREST().getSemesterCourses(getUser().getAccessToken(), getOAuth().oauthService,semesterID);
	}
		
	/**
	 * Gibt die Kurse des Studenten  zurueck
	 * @return Courses[] Kurse des Studentens
	 */
	public static Courses getCourses()
	{		
		addLog("Gibt die Kurse zurueck");
		return getREST().getCourses(getUser().getAccessToken(), getOAuth().oauthService);
	}
	
	/**
	 * Gibt die Datei zu einer Veranstaltung zurueck
	 * @param courseId ID des Kurses
	 * @return Document[] Dokumente des Kurses
	 */
	public static Documents getDocumentsOfCourse(String courseId)
	{	
		addLog("Gebe Dateien der Veranstaltung: "+ courseId + " zurueck");		
		return getREST().getDocumentsOfCourse(getUser().getAccessToken(), getOAuth().oauthService,courseId);	
	}

	/**
	 * Laded die Datei mit der angegeben Document Id herunter
	 * @param documentId ID des Dokuments
	 * @param filename Dateiname des Dokuments
	 * 
	 * @author Felix, Fabian
	 */
	public static void getDownload(String documentId, String filename)
	{
		addLog("Starte Download von "+filename+" ... ");
		
		Response response = getREST().getDocument(getUser().getAccessToken(), getOAuth().oauthService,documentId);
		
		String fname = getIO().download( response.getStream(), filename);
		
		addIsDownloaded(documentId);
		
		addLog("... fertig und als "+fname+" gespeichert");
		
	}
	
	/**
	 * Wird aufgerufen, falls neuer Access Token bekannt ist
	 * Schriebt diesen in user und speichert diesen in einer Datei
	 * @param accessToken
	 * @author Fabian
	 */
	public static void newAccessToken(Token accessToken) {
		getUser().setAccessToken(accessToken);
		addLog("Neuer Access Token gesetzt");
		saveUser();
	}
	
	/**
	 * Schreibt Log-Eintrag
	 * @param log
	 * 
	 * @author Fabian
	 */
	public static void addLog(String log){
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		//TODO naechste zeile loeschen		
		System.out.println("\n" + sdf.format(new Date()) + ">>" + log);		

		getUser().addLogText("\n" + sdf.format(new Date()) + ">>" + log);	
	}
	
	/**
	 * Speicert den User (teile nach IO auslagern) 
	 * @return boolean Gibt wieder ob es Funktioniert hat.
	 * 
	 * @author Felix
	 */
	public static boolean saveUser()
	{
		//TODO im moment nicht unterstüzt
		return false;
		/*
		try{
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(new File(getUser().userPath), getUser());
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Speichere den Log
			getIO().saveLog(getUser().logText, getUser().logFilePath);
			
			addLog("Userdaten gespeichert");
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}*/
	}
	
	/**
	 * Laed den User
	 * @return boolean Gibt wieder ob es Funktioniert hat
	 * 
	 * @author Felix
	 */
	public static boolean loadUser()
	{
		try{
		if(getUser().userPath=="" || getUser().userPath == null)
			getUser().setUserPath("User.json");

		String json = getIO().read(getUser().userPath);
		
		ObjectMapper mapper = new ObjectMapper();
			
			try {
				user = mapper.readValue(json, model.StudIPUser.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			oauth = new OAuth();
			addLog("Userdaten Geladen");
			
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @return Gibt Log Text zurueck
	 * @author Fabian
	 */
	public static String getLogText()
	{
		return getUser().logText;
	}
	
	public static String getLogFilePath()
	{
		return getUser().logFilePath;
	}
	
	public static String getUserPath()
	{
		return getUser().userPath;
	}

	public static String getDocPath()
	{
		return getUser().docPath;
	}
	
	public static void setLogFilePath(String path)
	{
		getUser().setLogFilePath(path);
	}

	public static void setUserPath(String path)
	{
		getUser().setUserPath(path);
	}

	public static void setDocPath(String path)
	{
		getUser().setDocPath(path);
	}

	
	public static void setIsDownloaded(List<String> list)
	{
		getUser().setIsDownladed(list);
	}
	
	//TODO nicht lauffaehig implementiert
	public static void addIsDownloaded(String add)
	{
		
		//getUser().addIsDownloaded(add);		
	}
	
	public static List<String> getIsDownloaed()
	{
		return getUser().getIsDownloaded();
	}
	
	/*
	public static void setTmpUser(StudIPUser _user)
	{
		user = _user;
	}
	*/
	
	/*
	//TODO test methode -> sollte iwab geloescht werden
	public static void get()
	{

		StudIPApi api = new StudIPApi();	
		
		Courses tmp = getCourses();
		
		OAuthRequest request = new OAuthRequest(Verb.GET, api.getRecourceUrl("/api/documents/"+tmp.getCourses().get(0).course_id+"/folder/"));
		
		getOAuth().oauthService.signRequest(getUser().getAccessToken(), request);
			    
		Response response = request.send();

		Courses courses = new Courses();		
		
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
		
	}
	*/
	
	//TODO delete
	/**
	 * Laded den Access Token aus der hinterlegten Datei
	 * @author Fabian
	 *
	public static boolean loadAccessToken() {
		
		boolean retval = io.loadAccessToken(user);
		if(retval){
			addLog("Access Token geladen\n");
		}else{
			addLog("Access Token konnte nicht geladen werden\n");
		}
		return retval;
	}*/
}
