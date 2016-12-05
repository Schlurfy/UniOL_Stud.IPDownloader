package model;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;


/**
 * Klasse verwaltet StudIP Urls
 * 
 * @author Fabian
 *
 */
public class StudIPApi extends DefaultApi10a
{
	
  private static final String BASE_URL = "https://elearning.uni-oldenburg.de/plugins.php/restipplugin";
		  //"https://elearning.uni-hannover.de/plugins.php/restipplugin";
  
  private static final String AUTHORIZE_URL = BASE_URL+"/oauth/authorize?oauth_token=%s";
  private static final String REQUEST_TOKEN_RESOURCE = BASE_URL+"/oauth/request_token";
  private static final String ACCESS_TOKEN_RESOURCE = BASE_URL+"/oauth/access_token";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return ACCESS_TOKEN_RESOURCE;
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return REQUEST_TOKEN_RESOURCE;
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
  
  /**
   * Erzeugt Url zur angegebener StudIP Resource
   * @param recsourcePath ist Pfad zur Resoure(z.B. "/api/courses/semester")
   * @return gesamt Url zur Resource
   * 
   * @author Fabian
   */
  public String getRecourceUrl(String recsourcePath){
	  return BASE_URL+recsourcePath;
  }
  
  
}