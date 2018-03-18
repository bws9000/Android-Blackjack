/*
 * 
    Copyright (C) 2014  Burt Wiley Snyder
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or 
     any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
	armorsoft@gmail.com
	
*/

package com.wileynet.blackjack.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseAndroid extends Database{
    
	protected SQLiteOpenHelper db_connection;
    protected SQLiteDatabase stmt;

    public DatabaseAndroid(Context context) {
        db_connection = new AndroidDB(context, DATABASE_NAME, null, database_version);
        stmt = db_connection.getWritableDatabase();
    }
    
    public void execute(String sql){
        stmt.execSQL(sql);
    }
    
    public void close() {
        db_connection.close();
    }


    public int executeUpdate(String sql){
        stmt.execSQL(sql);
        SQLiteStatement tmp = stmt.compileStatement("SELECT CHANGES()");
        return (int) tmp.simpleQueryForLong();
    }

    public Result query(String sql) {
    	ResultAndroid result = new ResultAndroid(stmt.rawQuery(sql,null));
        return result;
    }

    public class AndroidDB extends SQLiteOpenHelper {

        public AndroidDB(Context context, String name, CursorFactory factory,
                int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            stmt=db;
            DatabaseAndroid.this.onCreate();
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            stmt=db;
            DatabaseAndroid.this.onUpgrade();
        }

    }

    public class ResultAndroid implements Result{
        
    	Cursor cursor;

        public ResultAndroid(Cursor cursor) {
            this.cursor=cursor;
        }

        public boolean isEmpty() {
            return cursor.getCount()==0;
        }

        public int getColumnIndex(String name) {
            return cursor.getColumnIndex(name);
        }

        public String[] getColumnNames() {
            return cursor.getColumnNames();
        }

        public float getFloat(int columnIndex) {
            return cursor.getFloat(columnIndex);
        }

		@Override
		public boolean moveToNext() {
			if(cursor.moveToNext()){
				return true;
			}
			return false;
		}

		@Override
		public int getInt(int i) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getString(int columnIndex) {
			
			String out="none";
			if(cursor.getCount() > 0){
				out = cursor.getString(columnIndex);
			}
			return out;
		}


    }

}
