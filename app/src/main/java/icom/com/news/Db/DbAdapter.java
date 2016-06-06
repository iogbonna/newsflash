package icom.com.news.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Charlie on 12/08/2015.
 */
public class DbAdapter {

    private static MyDatabaseManager mDbManager;
    private Context mContext;


    private static final String CREATE_FAVOURITES_TABLE = "CREATE TABLE Favourites ( "
            + "Id INTEGER PRIMARY KEY AUTOINCREMENT, " + "link TEXT, "
            + "Title TEXT, " + "Description TEXT, "
            + "PubDate Text, " + "Category Text "
            + ")";

    private static final String CREATE_SEARCH_TABLE = "CREATE TABLE Search ( "
            + "Id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Value Text "
            + ")";

    private static final String CREATE_LATEST_TABLE = "CREATE TABLE  Latest( "
            + "Id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Value Text "
            + ")";


    public DbAdapter(Context context) {
        mContext = context.getApplicationContext();
    }

    public SQLiteDatabase openDb() {
        if (mDbManager == null) {
            mDbManager = new MyDatabaseManager(mContext);
        }
        return mDbManager.getWritableDatabase();
    }

    public void CloseDb() {
        mDbManager.close();
    }



    private static class MyDatabaseManager extends SQLiteOpenHelper {
        private static String DATABASE_NAME = "NewsDb";
        private static int DATABASE_VERSION = 2;

        public MyDatabaseManager(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_FAVOURITES_TABLE);
            db.execSQL(CREATE_LATEST_TABLE);
            db.execSQL(CREATE_SEARCH_TABLE);

            //db.close();

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("DROP TABLE IF EXISTS Favourites");
            db.execSQL("DROP TABLE IF EXISTS Latest");
            db.execSQL("DROP TABLE IF EXISTS Search");

            this.onCreate(db);

        }

    }
}
