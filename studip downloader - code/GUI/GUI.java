package GUI;

import java.io.File;
import service.ManagementService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Klasse in der die GUI initialisiert wird.
 * Sie behandelt desweiteren verschiedene Stages.
 * 
 * @author Dominik
 */
public class GUI extends Application
{
	private Stage primaryStage;
    private BorderPane rootLayout;
	private String lastUrl = "";
	private boolean ready = false;
	
	public static void initGUI()
	{
		//File a = new File(ManagementService.getUser().userPath);		 
		launch();	 
	}
	
	/**
	 * Startet Gui und instanziert Layout
	 * @author Dominik
	 */
	@Override
	public void start(Stage stage)
	{		
		Events.appendLogText("GUI Init startet");
		
		this.primaryStage = stage;
        this.primaryStage.setTitle("Stup.IP Downloader");
        initRootLayout();
        
        File a = new File(ManagementService.getUser().userPath);
        
        //active to get userdata feature
        if( true) //!a.exists())
        {	
        	callAlert("Info","Anmeldung"," Dies ist ein Downloader für das Stud.IP der Uni Oldenburg, mit dem Daten aus angemeldeten Veranstaltungen einfach heruntergeladen werden können."
        			+ "\n\nDu wirst nun zur Stud.IP Anmeldung weitergeleitet. Dort musst du dich Anmelden, um die Anwendung zu genehmigen. "
        			+ "(Es werden keine Accountinformationen zwischengespeichert oder getrackt.)");
        	LoadLogin();        	
        	Events.appendLogText("Lade Login");        	
        }
        else
        {
        	Events.loadAccessTokenClick();
    		Events.appendLogText("Access Token aus Datei geladen");
    		
    		//ManagementService.testClass();
    		callAlert("Info","Anwendung wird gestart.","Es dauert ein Moment bis deine Veranstaltungen geladen wurden. Bitte ein wenig Geduld!");
    		showOverview();
        	
        }  
        
        
        
        //Beendet die Java-Umgebung fuer das Programm bei Beenden
        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
        	
			@Override
			public void handle(WindowEvent event) 
			{
				Platform.exit();
		    	System.exit(0);
			}
        	
        });

	}
	
    /**
     * Initialisiert das RootLayout
     * @author Dominik
     */
    public void initRootLayout() 
    {         
        try{
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) 
        {
            e.printStackTrace();
            callAlert("Error","Ein Fehler im RootLayout ist aufgetreten.",e.toString());
        }
    }

    /**
     * Zeigt Overview in RootLayout
     * @author Dominik
     */
    public void showOverview() 
    {
        try 
        {
            // Load overview.
            FXMLLoader loader = new FXMLLoader();
            //../view/
            loader.setLocation(GUI.class.getResource("view/Overview.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set overview into the center of root layout.
            rootLayout.setCenter(overview);
        } catch (Exception e) 
        {
            e.printStackTrace();
            callAlert("Error","Ein Fehler im Overview ist aufgetretetn.","");
        }
    }
    
    /**
     * Zeigt Login innerhalb des RootLayouts
     * @author Dominik
     */
    public void LoadLogin() 
    {	  	
    	try {  		
	    	VBox vbox = new VBox(5);
	    	WebView browser;
	    	
			// Set Login into the center of root layout.
			String authURL = ManagementService.login();			
			
			vbox.setPrefSize(1280, 768);
			
			//Starte Webanzeige:
			browser = new WebView();
			
			browser.getEngine().load(authURL);
			
			browser.setPrefSize(1280,768);
		
			//Statusbalken fuers Laden
			ProgressBar progressBar = new ProgressBar(0);
			progressBar.setPrefWidth(browser.getPrefWidth());
			
			progressBar.progressProperty().bind(browser.getEngine().getLoadWorker().progressProperty());
	
			
			//Oauth Verifier in Url abfangen
			browser.getEngine().setOnStatusChanged((event) -> 
			{	
				if(!ready)
				{				
					if(!lastUrl.equals(browser.getEngine().getLocation()))
					{					
						lastUrl = browser.getEngine().getLocation();
						//Events.appendLogText("GUI - LoginUrl: " + lastUrl);
					}      
					
		        	if(lastUrl.contains("oauth_verifier"))
		        	{
		        		ready = true;
		        		
		        		Events.appendLogText("Found Verifier");
		        		
						if(ManagementService.loginReady(lastUrl))
						{	
							
							callAlert("Info","Du hast die Anwendung erfolgreich verifiziert!","Es dauert ein Moment bis deine Veranstaltungsübersicht geladen wurden. "
									+ "Bitte hab ein wenig Geduld!");
							this.showOverview();
						}
		        	}	        	
		    	}
			});		

			//Anzeige des WebView und der ProgressBar
			vbox.getChildren().addAll(browser,progressBar);
			
			AnchorPane ap = new AnchorPane();
			
			//Setzt Anker
			ap.setBottomAnchor(vbox, 0.0);
			ap.setLeftAnchor(vbox, 0.0);
			ap.setRightAnchor(vbox, 0.0);
			ap.setTopAnchor(vbox, 0.0);
			ap.getChildren().add(vbox);
			
	        rootLayout.setCenter(ap);
    	}
    	catch(Exception e)
    	{
    		callAlert("Fehler","Fehler beim Laden :(",e.toString());
    	}
    }

	public static void callAlert(String titel, String header, String content)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titel);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
    
    /**
     * Gibt Primaere Stage zurueck
     * @return Stage
     * @author Dominik
     */
    public Stage getPrimaryStage() 
    {
        return primaryStage;
    }
}