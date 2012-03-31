package com.togetherness.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class MTBaseNetworkUtilities {

	public static final String BASE_URL = "http://107.22.255.126";
	//public static final String USER_AGENT = "MTAuthenticationService/1.0";
    public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
	
    protected static HttpClient mHttpClient;
    
	/**
     * Configures the httpClient to connect to the URL provided
     */
    protected static void maybeCreateHttpClient()
    {
    	if(mHttpClient == null)
    	{
//    		mHttpClient = new DefaultHttpClient();
//    		final HttpParams params = mHttpClient.getParams();
//    		HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
//    		HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
//    		ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
    		
    		
    		HttpParams params = new BasicHttpParams();
    		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
    		HttpProtocolParams.setUseExpectContinue(params, true);

    		SchemeRegistry registry = new SchemeRegistry();
    		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    		registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

    		ClientConnectionManager connman = new ThreadSafeClientConnManager(params, registry);
    		mHttpClient = new DefaultHttpClient(connman, params);
    	}
    }
    
    
    /**
     * Executes the network requests on a separate thread.
     * 
     * @param runnable instance containing network operations to be executed.
     */
    public static Thread performOnBackgroundThread(final Runnable runnable)
    {
    	final Thread t = new Thread(){
    		
    		@Override
    		public void run()
    		{
    			try {
					runnable.run();
				} finally
				{
					
				}
    		}
    	};
    	
    	t.start();
    	return t;
    	
    }
    
    
    protected static ContentBody getContentBody(String param) throws UnsupportedEncodingException
	{
		if(param == null)
		{
			return null;
		}
		
		return new StringBody(param, Charset.forName("UTF-8"));
	}
    
    
    protected static String getFacebookAccessToken(final Context context)
    {
    	final SharedPreferences settings = context.getSharedPreferences("TogethernessSetting", 0);
		final String accessToken = settings.getString("fbAccessToken", null);
		
		return accessToken;
    }
}
