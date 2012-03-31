package com.togetherness.dm;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.togetherness.entity.Friends;
import com.togetherness.entity.UserTogetherMap;
import com.togetherness.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: Prashanth
 * Date: 2/22/12
 * Time: 7:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class TogethernessORMLiteHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "togetherness.db";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    private Dao<UserTogetherMap, Integer> userTogetherDAO = null;
    private RuntimeExceptionDao<UserTogetherMap, Integer> userTogetherRuntimeDAO = null;

    private Dao<Friends, Integer> friendDAO = null;
    private RuntimeExceptionDao<Friends, Integer> friendsRuntimeDAO = null;


    public TogethernessORMLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }



    public void onCreate(android.database.sqlite.SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource){
        try {
            Log.i(TogethernessORMLiteHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, UserTogetherMap.class);
            TableUtils.createTable(connectionSource, Friends.class);
        } catch (SQLException e) {
            Log.e(TogethernessORMLiteHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

        Log.i(TogethernessORMLiteHelper.class.getName(), "Created new entries in onCreate: " + System.currentTimeMillis());
    }

    public void onUpgrade(android.database.sqlite.SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1){
        try {
            Log.i(TogethernessORMLiteHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, UserTogetherMap.class, true);

            TableUtils.dropTable(connectionSource, Friends.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(TogethernessORMLiteHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    private RuntimeExceptionDao<UserTogetherMap, Integer> getUserTogetherRuntimeDao() throws SQLException {
        if (userTogetherRuntimeDAO == null) {
            userTogetherRuntimeDAO = getRuntimeExceptionDao(UserTogetherMap.class);
        }
        return userTogetherRuntimeDAO;
    }

    private Dao<UserTogetherMap, Integer> getDao() throws SQLException {
        if (userTogetherDAO == null) {
            userTogetherDAO = getDao(UserTogetherMap.class);
        }
        return userTogetherDAO;
    }

    private RuntimeExceptionDao<Friends, Integer> getFriendsRuntimeDao() throws SQLException {
        if (friendsRuntimeDAO == null) {
            friendsRuntimeDAO = getRuntimeExceptionDao(Friends.class);
        }
        return friendsRuntimeDAO;
    }

    private Dao<Friends, Integer> getFriendsDao() throws SQLException {
        if (friendDAO == null) {
            friendDAO = getDao(Friends.class);
        }
        return friendDAO;
    }

    public List<UserTogetherMap> fetchUserTogetherMapByID(String togetherUserId){
        List<UserTogetherMap> togethernessMap = null;

        try{
            userTogetherRuntimeDAO = getUserTogetherRuntimeDao();
            UserTogetherMap userTogetherMap = new UserTogetherMap();
            userTogetherMap.setTogetherUserId(togetherUserId);
            togethernessMap =  userTogetherRuntimeDAO.queryForMatching(userTogetherMap);
        }catch(SQLException sqe){
            sqe.printStackTrace();
        }

        return togethernessMap;
    }
    
    public void storeFriendPhoto(List<Friends> friendList){
        
        try{
            friendsRuntimeDAO = getFriendsRuntimeDao();

            for(Friends friend: friendList){
                friendsRuntimeDAO.createOrUpdate(friend);
            }
        }catch(SQLException sqe){
            sqe.printStackTrace();
        }
            
    }

    public void storeUserTogetherMap(List<UserTogetherMap> userTogetherMapList){

        try{
            userTogetherRuntimeDAO = getUserTogetherRuntimeDao();

            for(UserTogetherMap userTogetherMap: userTogetherMapList){
                userTogetherRuntimeDAO.createOrUpdate(userTogetherMap);
            }
        }catch(SQLException sqe){
            sqe.printStackTrace();
        }

    }
    
    public List<Friends> fetchFriendByID(String friendsFBID){
        List<Friends> friendList = null;
        try{
            friendsRuntimeDAO = getFriendsRuntimeDao();
            Friends friends = new Friends();
            friends.setFriendsFBID(friendsFBID);
            friendList =  friendsRuntimeDAO.queryForMatching(friends);
        }catch(SQLException sqe){
            sqe.printStackTrace();
        }

        return friendList;
    }
    
    public List<UserTogetherMap> updateAndComputeApartStatus(List<Friends> friendsList){
        List<UserTogetherMap> userTogetherMapList = new ArrayList<UserTogetherMap>();
        try{
            userTogetherRuntimeDAO = getUserTogetherRuntimeDao();
            userTogetherDAO = getDao();

            //Get Current Time
            Date currentDate = GregorianCalendar.getInstance().getTime();

            for(Friends friend: friendsList){
                UserTogetherMap userTogetherMap = new UserTogetherMap();
                userTogetherMap.setTogetherUserId(friend.getFriendsFBID());
                userTogetherMap.setFbUserId(friend.getLoggedInUserId());
                userTogetherMap.setTogetherStatus("I");
                List<UserTogetherMap> dbUserTogetherMapList = userTogetherRuntimeDAO.queryForMatching(userTogetherMap);

                if(dbUserTogetherMapList != null && dbUserTogetherMapList.size() > 0){
                    UserTogetherMap dbUserTogether = dbUserTogetherMapList.get(0);
                    Date checkedInDate = dbUserTogether.getTogetherTimestamp();
                    long togetherDuration = currentDate.getTime() - checkedInDate.getTime();
                    dbUserTogether.setTogetherDuration(new Double(togetherDuration));
                    dbUserTogether.setTogetherStatus("O");
                    userTogetherRuntimeDAO.update(dbUserTogether);
                    userTogetherMapList.add(dbUserTogether);
                }
            }
        }catch(SQLException sqe){
            sqe.printStackTrace();
        }

        return userTogetherMapList;
    }

    public String getTimeDiff(Date dateOne, Date dateTwo) {
        String diff = "";
        long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
        diff = String.format("%d hour(s) %d min(s)", TimeUnit.MILLISECONDS.toHours(timeDiff),
                TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));
        return diff;
    }
    
    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        userTogetherRuntimeDAO = null;
        friendsRuntimeDAO = null;
    }
}
