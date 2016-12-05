package service.OAuth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;


public class StudIPApi extends DefaultApi10a
{
	
  private static final String BASE_URL = "elearning.uni-oldenburg.de/plugins.php/restipplugin/oauth";
  
  private static final String AUTHORIZE_URL = "https://"+BASE_URL+"/authorize?oauth_token=%s";
  private static final String REQUEST_TOKEN_RESOURCE = BASE_URL+"/request_token";
  private static final String ACCESS_TOKEN_RESOURCE = BASE_URL+"/access_token";
  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://" + ACCESS_TOKEN_RESOURCE;
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://" + REQUEST_TOKEN_RESOURCE;
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }

  /**
   * Twitter 'friendlier' authorization endpoint for OAuth.
   */
  public static class Authenticate extends StudIPApi
  {
    private static final String AUTHENTICATE_URL = "https://elearning.uni-oldenburg.de/oauth/authenticate?oauth_token=%s";

    @Override
    public String getAuthorizationUrl(Token requestToken)
    {
      return String.format(AUTHENTICATE_URL, requestToken.getToken());
    }
  }
}