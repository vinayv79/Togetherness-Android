package com.togetherness.communication;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.togetherness.entity.UserTogetherMap;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.net.*;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Prashanth
 * Date: 4/1/12
 * Time: 2:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class TogethernessServerCommUtil extends AsyncTask<Void, Void, Void>{


//http://localhost:8080/api/together/oAuthToken||UserId||Friend1!!Friend2||yyMMddhhmmss||CheckInStatus(I/O)||Message
    
    private final  String SERVER_URL = "http://togetherness-beta.appspot.com/api/together";
    private final  String FIELD_DELIMETER = "||";
    private  final String BASE_URL = "/";
    private final  String FRIEND_LIST_DELIMETER = "!!";

    public  final String HTTP_GET = "GET";
    public final String HTTP_POST = "POST";
    private  final String CONTENT_TYPE = "application/json";

    private long fbUserToken;
    private List<UserTogetherMap> userTogetherMapList = null;
    
    public TogethernessServerCommUtil(long fbUserToken, List<UserTogetherMap> userTogetherMapList){

        this.fbUserToken = fbUserToken;
        this.userTogetherMapList = userTogetherMapList;
        
    }

    protected Void doInBackground(Void... param){

          postMessageToServer();
          return null;
    }
    public void postMessageToServer(){

        HttpClient client = new DefaultHttpClient();
        
        
        //HttpGet httpGet = new HttpGet(URLEncoder.encode(formatRequestStr(fbUserToken, userTogetherMapList)));
        String requestPath = URLEncoder.encode(formatRequestStr(fbUserToken, userTogetherMapList)
        );


        HttpURLConnection conn =  null;

        try {

            URL srcUrl = new URL(SERVER_URL + BASE_URL + requestPath);
            System.out.println("Connecting to " + srcUrl.getProtocol() + "://" + srcUrl.getHost()
                    + ":" + srcUrl.getPort() + srcUrl.getPath());
           conn = (HttpURLConnection) srcUrl.openConnection();

            conn.setRequestMethod(HTTP_POST);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false); // no user interact [like pop up]

            conn.setDoOutput(true); // want to send
            conn.setDoInput(true);  // want to receive
            conn.setRequestProperty("Content-type", CONTENT_TYPE);

            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

    }
    
    private  String formatRequestStr(long fbUserToken, List<UserTogetherMap> userTogetherMapList){

        StringBuffer sb = new StringBuffer();
        sb.append(fbUserToken);
        //sb.append("AAACEdEose0cBAL7uYl7DlJsPXknjeXdtGjrSXvprxGOL2SJqyzuE0X1SLgfFl2HVydUXnrD2KEZAQXthUZClkqWtZCkGyB0FlmTbUSqoAZDZD");
        sb.append(FIELD_DELIMETER);

        for(int i =0; i< userTogetherMapList.size(); i++){
            UserTogetherMap userTogetherObj = userTogetherMapList.get(i);
            
            if(i == 0){
                sb.append(userTogetherObj.getFbUserId());
              //  sb.append("748084126");
                sb.append(FIELD_DELIMETER);
            }
            sb.append(userTogetherObj.getTogetherUserId());
           // sb.append("100000308132823");
            if(userTogetherMapList.size() > 1){
                sb.append(FRIEND_LIST_DELIMETER);
            }else{
                sb.append(FIELD_DELIMETER);
            }

            if(i == (userTogetherMapList.size() -1)){
                 sb.append(formatTimestamp(userTogetherObj.getTogetherTimestamp()));
                sb.append(FIELD_DELIMETER);
                sb.append(userTogetherObj.getTogetherStatus());
                sb.append(FIELD_DELIMETER);
                sb.append(userTogetherObj.getTogetherMessage());
                sb.append(FIELD_DELIMETER);
                if(userTogetherObj.getTogetherDuration() != null){
                     sb.append(userTogetherObj.getTogetherDuration().doubleValue());
                }else{
                    sb.append(0);
                }

            }
        }
        
        return sb.toString();
    }
    
    private  String formatTimestamp(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
        return simpleDateFormat.format(date);
    }
}

