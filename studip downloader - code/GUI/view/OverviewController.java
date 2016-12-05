package GUI.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import GUI.DownloadQueue;
import GUI.Events;
import GUI.GUI;
import model.*;
import service.ManagementService;
import javafx.stage.DirectoryChooser;

/**
 * Klasse in der die GUI initialisiert wird.
 * Sie handelt desweiteren verschiedene Stages.
 * 
 * @author Dominik
 */
public class OverviewController 
{
	/**
	 * ID-Vergabe an Objekte in XML
	 * @author Dominik
	 */
	//Downloader
	@FXML
    private ProgressBar progress;
	@FXML
	private TreeView<Object> coursesOverview;
	
	//Preferences
	@FXML
    private TextField downloadDirectory;
	@FXML
    private TextField logDirectory;
	@FXML
    private TextArea log;
	
	/**
	 * Anderweitige Vairablen
	 * @author Dominik
	 */
    //Variablen
    private List<CheckBoxTreeItem<Object>> allTreeItems = new ArrayList<CheckBoxTreeItem<Object>>();
    private LinkedBlockingQueue<Document> queue = new LinkedBlockingQueue<Document>();
	private DownloadQueue dq = new DownloadQueue(queue);

	/**
     * Leerer Konstruktor, der ausgefuehrt wird, bevor intitialisiert wird
     * @author Dominik
     */
    public OverviewController() 
    {
    }

    /**
     * Initialisiert Daten fuer die Oberflaeeche.
     * Wird nach fxml ausgefuehrt.
     * @author Dominik
     */
    @FXML
    private void initialize() 
    {
        Thread downloadThread = new Thread(dq);
        downloadThread.start();
        
        this.progress.progressProperty().bind(dq.processProperty);
        
     	this.loadCoursesinView();
     	this.loadDirectories();
     	showLog();
    }
    
    //Downloader
    /**
     * Laedt alle Semster, Kurse und Dokumente in Tree
     * @author Dominik
     */
    @FXML
    private void loadCoursesinView() 
    {
    	
    	this.coursesOverview.setCellFactory(CheckBoxTreeCell.forTreeView());
    	Semesters semesters = Events.getSemesters();
    	CheckBoxTreeItem<Object> root = new CheckBoxTreeItem<Object>("StudIP");
    	root.setExpanded(true);
     	List<Course> uebungen = new ArrayList<Course>();
     	List<Course> vorlesungen = new ArrayList<Course>();
     	
     	Courses all = Events.getCourses();
     	
     	if(semesters != null)
    	for(Semester s : semesters.getSemesters())
    	{    		
    		//Wenn keine Kurse im Semester vorhanden soll nicht angezeigt werden
    		Courses allCoursesSem = new Courses();
    		
    		for(Course c: all.getCourses())
    			if(s.semester_id.contains(c.semester_id))
    				allCoursesSem.addCourse(c);
    		
    		if(allCoursesSem.getCourses() != null && !allCoursesSem.getCourses().isEmpty())
    		{
    			CheckBoxTreeItem<Object> semester = new CheckBoxTreeItem<Object>(s);
    			root.getChildren().add(semester);
        		allTreeItems.add(semester);
        		semester.setExpanded(true);
        		
        		if(allCoursesSem != null)        		
	    			for(Course c : allCoursesSem.getCourses())
	    			{
	    				if(!c.toString().toUpperCase().contains("UEBUNG") || !c.toString().toUpperCase().contains("TUTOR"))
	    					vorlesungen.add(c);
	    				else
	    					uebungen.add(c);    				
	    			}
    			
        		if(vorlesungen != null)
	    			for(Course c : vorlesungen)
	    			{
	    				List<Course> uebungVonVorl = this.GetUebungVonVorlesung(c, uebungen);
	    				CheckBoxTreeItem<Object> course = new CheckBoxTreeItem<Object>(c.title);	    				
	    				semester.getChildren().add(course);
	    				allTreeItems.add(course);
	    				course.setExpanded(false);
	    					
	    				if(uebungVonVorl.isEmpty())
	        			{
	    					Documents docs = Events.getDocuments(c.course_id);
	    	    			
	    					if(docs != null)
		    					for(Document d : docs.getDocuments())
		    					{
		    						CheckBoxTreeItem<Object> document = new CheckBoxTreeItem<Object>(d);            
		    						course.getChildren().add(document);
		    						allTreeItems.add(document);
		    					}
	    				}
	    				else
	    				{
	    					CheckBoxTreeItem<Object> vorl = new CheckBoxTreeItem<Object>("Vorlesung");            
							course.getChildren().add(vorl);
							allTreeItems.add(vorl);
							
							Documents docs = Events.getDocuments(c.course_id);
							
							if(docs != null) 
		    					for(Document d : docs.getDocuments())
		    					{
		    						CheckBoxTreeItem<Object> document = new CheckBoxTreeItem<Object>(d);            
		    						vorl.getChildren().add(document);
		    						allTreeItems.add(document);
		    					}
			    			if(uebungVonVorl != null)		
		    					for(Course ue : uebungVonVorl)
		    					{		    						
		    						CheckBoxTreeItem<Object> uebu = new CheckBoxTreeItem<Object>(ue.title+ue.course_id);            
		    						course.getChildren().add(uebu);
		    						allTreeItems.add(uebu);	
		    						
		    						Documents docsUe = Events.getDocuments(ue.course_id);
		        					
		    						for(Document d : docsUe.getDocuments())
		        					{
		        						CheckBoxTreeItem<Object> document = new CheckBoxTreeItem<Object>(d);            
		        						uebu.getChildren().add(document);
		        						allTreeItems.add(document);
		        					}
		        					
		    					}
	    				}    			
        		}
    		}
    		vorlesungen.clear();
    		uebungen.clear();
    	}
    	
    	this.coursesOverview.setRoot(root);
    	showLog();
    }
    
    /**
     * Selektiert alle Dateien
     * @author Dominik
     */
    @FXML
    private void selectAll() 
    {
    	for(CheckBoxTreeItem<Object> o: this.allTreeItems)
    		o.setSelected(true);
    	showLog();
    }
    
    /**
     * Selektier alle noch nicht gedownloadeten Dateien
     * @author Dominik
     */
    @FXML
    private void selectNew() 
    {
    	CheckBoxTreeItem<Object> rootItem = (CheckBoxTreeItem<Object>)this.coursesOverview.getRoot();
    	rootItem.setSelected(true);
    	List<String> oldFiles = Events.getDownloadedFiles();
    	Document doc = new Document();
    	
    	for(CheckBoxTreeItem<Object> item : allTreeItems)
    	{
    		if(item.getValue().getClass() == Document.class)
        	{
    			doc = (Document)item.getValue();
        		
    			if(oldFiles.contains(doc.document_id))
    			{
    				item.setSelected(false);
    			}
            }
    		
    	}
    }
    
    /**
     * Uebergibt alle zu downloadenen Dateien an DownloadQueue
     * @author Dominik
     */
    @FXML
    private void downloadSelected() 
    {
    	GUI.callAlert("Download Start", "Der Download der ausgewählten Datein wurde gestartet!", "Am unteren Rand des Fensters kannt du den Fortschritt verfolgen. Der Pfad der runtergeladeten Datei ist im selben Verzeichen in dem das Programm gestartet wurde.");
    	String semester = "";
    	String course = "";
    	for(CheckBoxTreeItem<Object> cbti : allTreeItems)
    	{
    		if(cbti.isSelected()||cbti.isIndeterminate())
    		{
    			if(cbti.getValue().getClass() == Semester.class)
    			{
    				Semester sem = (Semester)cbti.getValue();
    				semester = sem.toDownloadString()+"/";
    			}
    			if(cbti.getValue().getClass() == Course.class)
    			{
    				course = (Course)cbti.getValue()+"";
    			}
    			if(cbti.getValue().getClass() == Document.class)
    			{
    				String tmpTest = cbti.getParent().getValue().toString()+"/";
    				dq.addElement((Document)cbti.getValue(),semester+tmpTest+course);
    			}
			}
    	}
    	
    	showLog();
    }
    
    //Preferences
    /**
     * Oeffnet DirectoryChooser fuer DownloadDirectory
     * @author Dominik
     */
    @FXML
    private void handleDownloadDirectory()
    {
    	String directory = this.getDirectory();
    	if(directory != null)
    	{
    		this.downloadDirectory.setText(directory+"\\");
    		Events.setPathes(this.downloadDirectory.getText(),this.logDirectory.getText());
    	} 	
    	
    	showLog();
    }
    
    /**
     * Oeffnet DirectoryChooser fuer LogFileDirectory
     * @author Dominik
     */
    @FXML
    private void handleLogDirectory()
    {
    	String directory = this.getDirectory();
    	if(directory != null)
    		this.logDirectory.setText(directory+"\\Log.txt");
    
    	showLog();
    }

    /**
     * Fuehrt Speicherung der Einstellungen aus (mit Sicherheitsabfrage)
     * @author Dominik
     */
    @FXML
    private void handlePreferencesSave()
    {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Warning");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure to save Changes?");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK)
    		Events.setPathes(this.downloadDirectory.getText(),this.logDirectory.getText());
    	
    	showLog();
    }
    
    /**
     * Fuehrt Reset der Einstellungen aus (mit Sicherheitsabfrage)
     * @author Dominik
     */    
    @FXML
    private void handleReset()
    {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Warning");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure to reset all Pathes?");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK)
    	{
    		this.logDirectory.setText("Log.txt");
    		this.downloadDirectory.setText("download/");
    		this.handlePreferencesSave();
    	} 
    	
    	showLog();
    }
    
    /**
     * Laedt die Textfelder der Pfade
     * @author Dominik
     */
    private void loadDirectories()
    {
    	this.downloadDirectory.setText(Events.getDocPath());
    	this.logDirectory.setText(Events.getLogFilePath());
    	
		showLog();
    }
    
    /**
     * Fuehrt Benutzerwechsel aus (mit Sicherheitsabfrage)
     * @author Dominik
     */
    @FXML
    private void handleUserChange()
    {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Warning");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure to change User?");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK)
    	{    		
    		File a = new File(ManagementService.getUserPath());
    		if(a.exists())
    			a.delete();
    	
    		//	Events.loginClick();
    		
    		Alert alertRestart = new Alert(AlertType.WARNING);
    		alertRestart.setTitle("Warning");
    		alertRestart.setHeaderText(null);
    		alertRestart.setContentText("You have to restart to save changes!");
    	}
    	
    	showLog();
    }
    
    /**
     * Setzt den Log-Text
     * @author Dominik
     */
    private void showLog()
    {
    	this.log.setText(Events.getLogText());
    	this.log.setScrollTop(Double.MAX_VALUE);
    }
    
    /**
     * Oeffnet DirectoryChooser und gibt ausgewaehlten Pfad zurueck
     * @return Directory Path
     * @author Dominik
     */
    private String getDirectory()
    {
    	DirectoryChooser a = new DirectoryChooser();
    	File d = a.showDialog(null);
    	if(d != null)
    		return d.getPath();
    	else
    		return null;
    }
    
    /**
     * Sucht anhand einer Vorlesung, die dazugehoerige/n Übung/en
     * @author Dominik
     */
    private List<Course> GetUebungVonVorlesung(Course vorlesung, List<Course> Uebungen)
    {
    	List<Course> retVal = new ArrayList<Course>();
    	
    	for(Course c : Uebungen)
    	{
    		if(c.title.contains(vorlesung.title))
    			retVal.add(c);
    	}
    	
		return retVal;
    }
}