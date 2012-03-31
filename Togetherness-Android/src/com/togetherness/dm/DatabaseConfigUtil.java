package com.togetherness.dm;


import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.togetherness.entity.UserTogetherMap;


public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            UserTogetherMap.class
    };
    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }
}