package com.kit.cn.library.db.ORM;


import android.content.Context;

/**
 * @author zhouwen
 * @version 0.1
 * @since 2016/07/26
 */
public class BuilderOrmInstance {

    private static final String BASE_DB = "base_db";

    public static LightORM getORMSQLite(Context context) {
        if (LightORM.getInstance(BASE_DB) == null)
            new SQLiteBuilder()
                    .configDBName(BASE_DB)
                    .configVersion(1)
                    .build(context);

        return LightORM.getInstance(BASE_DB);
    }
}
