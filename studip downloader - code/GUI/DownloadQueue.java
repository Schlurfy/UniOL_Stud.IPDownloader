package GUI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Document;

/**
 * Klasse fuer DownloadQueue
 * @author Dominik
 */
public class DownloadQueue implements Runnable
{
    //Download
	private LinkedBlockingQueue<Document> queue;
    private List<String> path = new ArrayList<String>();
   
    //Progress
    private int counterAll = 0;
    private int counterDownloaded = 0;
    public DoubleProperty processProperty = new SimpleDoubleProperty(0.0);

    /**
     * Instanziert DownloadQueue
     * @param pQueue
     * @author Dominik
     */
    public DownloadQueue(LinkedBlockingQueue<Document> pQueue) 
    {
    	this.queue = pQueue;
    }
    
    /**
     * Fuegt Element der DownloadQueue hinzu 
     * @param pDoc
     * @param pPath
     * @author Dominik
     */
    public void addElement(Document pDoc, String pPath)
    {
    	try 
    	{
			queue.put(pDoc);
			path.add(pPath);
			counterAll++;
		} 
    	catch (InterruptedException e) 
    	{
			e.printStackTrace();
		}
    }
    
    /**
     * Fuehrt DownloadQueue (endlos) aus
     * @author Dominik
     */
    @Override
    public void run() 
    {
		this.processProperty.set(-1.0);
    	while(true)
    	{
    		if(!queue.isEmpty())
    		{
    			Document curDoc = (Document)queue.poll();
    			Events.downloadFile(curDoc,path.get(0));
    			path.remove(0);
    			this.counterDownloaded++;
    			this.processProperty.set((double)counterDownloaded/counterAll);
    		}
    		else
    		{    		
    			if(counterAll > 0 && (counterAll-counterDownloaded)==0 ){
    				Alert alert = new Alert(AlertType.INFORMATION);
	    			alert.setTitle("Info");
	    			alert.setHeaderText("Der Download ist abgeschlossen");
	    			alert.setContentText("Die Datei befinden sich im Ordner \"Download\" im selben Verzeichnisch in der diese Anwendung gestartet wurde.");
	    			alert.show();
    			}
    			this.processProperty.set(0.0);
    		}
    	}
    }
}