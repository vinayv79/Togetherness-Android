package com.togetherness.communication;

import android.content.SharedPreferences;
import com.togetherness.entity.UserTogetherMap;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Prashanth
 * Date: 4/1/12
 * Time: 2:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class TogethernessServerCommUtil {


//http://localhost:8080/api/together/oAuthToken||UserId||Friend1!!Friend2||yyMMddhhmmss||CheckInStatus(I/O)||Message
    
    private final static String SERVER_URL = "http://togetherness-beta.appspot.com/api/together/";
    private final static String FIELD_DELIMETER = "||";
    private final static String FRIEND_LIST_DELIMETER = "!!";

    public static void postMessageToServer(long fbUserToken, List<UserTogetherMap> userTogetherMapList){

        HttpClient client = new DefaultHttpClient();
        
        
        HttpGet httpGet = new HttpGet(URLEncoder.encode(formatRequestStr(fbUserToken, userTogetherMapList)));
        try{
            client.execute(httpGet);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static String formatRequestStr(long fbUserToken, List<UserTogetherMap> userTogetherMapList){

        StringBuffer sb = new StringBuffer();
        sb.append(SERVER_URL);
        sb.append(fbUserToken);
       // sb.append("AAACEdEose0cBAMDJsMSGeZB78CwUTwKOFvCI1DVgdZArmC5j9ZCmEirGtx6qBAPP9tZCaStPBti4kLkt91cGYlZAt4Gv81yL1B3TDZBZAZARWAZDZD");
        sb.append(FIELD_DELIMETER);

        for(int i =0; i< userTogetherMapList.size(); i++){
            UserTogetherMap userTogetherObj = userTogetherMapList.get(i);
            
            if(i == 0){
                sb.append(userTogetherObj.getFbUserId());
                //sb.append("748084126");
                sb.append(FIELD_DELIMETER);
            }
            sb.append(userTogetherObj.getTogetherUserId());
           // sb.append("100000308132823");
            sb.append(FRIEND_LIST_DELIMETER);

            if(i == (userTogetherMapList.size() -1)){
                 sb.append(formatTimestamp(userTogetherObj.getTogetherTimestamp()));
                sb.append(FIELD_DELIMETER);
                sb.append(userTogetherObj.getTogetherStatus());
                sb.append(FIELD_DELIMETER);
                sb.append(userTogetherObj.getTogetherMessage());

                if(userTogetherObj.getTogetherDuration() != null){
                     sb.append(userTogetherObj.getTogetherDuration().doubleValue());
                }
            }
        }
        
        return sb.toString();
    }
    
    private static String formatTimestamp(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
        return simpleDateFormat.format(date);
    }
}
