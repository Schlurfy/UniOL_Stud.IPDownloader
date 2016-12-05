package GUI;

import java.util.List;

import model.*;
import service.ManagementService;

public class Events
{ 

	public static void test()
	{
		System.out.println("Test - GUI Events");
	}
	
	/*
	public static void loginClick()
	{
		String authURL = ManagementService.login();
		//dialog = new nichtMehrBenoetigt_BrowserLogin(authURL);
	}
*/

	public static boolean loadAccessTokenClick() {
		return ManagementService.loadUser();
	}
	
	/**
	 * Gibt alle Semester zurueck
	 * @return Semesters
	 * @author Dominik
	 */
	public static Semesters getSemesters()
	{
		Semesters semesters = ManagementService.getSemesters();
		return semesters;
	}
	
	/**
	 * Gibt alle Kurse eines Semesters zurueck
	 * @param semesterID
	 * @return Courses
	 * @author Dominik
	 */
	public static Courses getSemesterCourses(String semesterID)
	{
		return ManagementService.getSemesterCourses(semesterID);
	}
	
	/**
	 * Gibt alle Kurse zurueck
	 * @param semesterID
	 * @return Courses
	 * @author Felix
	 */
	public static Courses getCourses()
	{
		return ManagementService.getCourses();
	}
	
	/**
	 * Gibt alle Documente eines Kurses zurueck
	 * @param courseID
	 * @return Documents
	 * @author Dominik
	 */
	public static Documents getDocuments(String courseID)
	{
		return ManagementService.getDocumentsOfCourse(courseID);
	}
	
	/**
	 * Gibt eine Liste mit allen bereits gedownloadeten Dateien zurueck
	 * @return list documen_ids
	 * @author Dominik
	 */
	public static List<String> getDownloadedFiles()
	{
		return service.ManagementService.getUser().isDownloaded;
	}
	
	/**
	 * Verweist zum Download
	 * @param pDoc Document
	 * @param pPath Pfad
	 * @author Dominik
	 */
	public static void downloadFile(Document pDoc, String pPath)
	{
		service.ManagementService.getDownload(pDoc.document_id, pPath+"/"+pDoc.filename);
		service.ManagementService.addIsDownloaded(pDoc.document_id);
	}

	/**
	 * Prueft auf Aenderungen der Pfade und speichert gegebenfalls
	 * @param docPath
	 * @param logPath
	 * @author Dominik
	 */
	public static void setPathes(String docPath,String logPath)
 	{
 		boolean changes = false;
 		
 		if(!service.ManagementService.getDocPath().equals(docPath))    	
    	{	
    		service.ManagementService.setDocPath(docPath);
    		changes = true;
    	}
    	if(!service.ManagementService.getLogFilePath().equals(logPath))    	
    	{
    		service.ManagementService.setLogFilePath(logPath);
    		changes = true;
    	}
    	if(changes)
    		service.ManagementService.saveUser();
    	
 	}
	
	/**
	 * Gibt Pfad der Log-Datei zurueck
	 * @return String LogFilePath
	 * @author Dominik
	 */
	public static String getLogFilePath()
	{
		return service.ManagementService.getLogFilePath();
	}
	
	/**
	 * Gibt Pfad in den gespeichert werden soll zurueck
	 * @return String DownloadPath
	 * @author Dominik
	 */
	public static String getDocPath()
	{
		return service.ManagementService.getDocPath();
	}
	
	/**
	 * Gibt den gesamten Log-Text zurueck
	 * @return String LogText
	 * @author Dominik
	 */
	public static String getLogText()
	{
		return service.ManagementService.getLogText();
	}

	/**
	 * Schnittstelle zur GUI, setzt gesammten LogText - TODO
	 * @param log String mit gesammten Log
	 * 
	 * @author Fabian
	 */
	public static void setLogText(String log){
		
		// dummy Funktion:
		System.out.print("Setze gesammten Log:\n"+log);
		
		/* Hier Soll der Text an die Gui weitergegeben werden, 
		 * also irgendwas wie 
		 * view.OverviewController.setLogText(log); 
		 */
	}

	/**
	 * Schnittstelle zur GUI, fuegt an LogText an
	 * @param log String
	 * @author Dominik
	 */
	public static void appendLogText(String log)
	{		
		//für debugging
		//GUI.callAlert("Test", log, log);
		ManagementService.addLog(log);	
	}
}
