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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseDesktop extends Database{
    
	protected Connection db_connection;
    protected Statement stmt;
    protected boolean nodatabase=false;
    
    public DatabaseDesktop() {
        loadDatabase();
        if (isNewDatabase()){
            onCreate();
            upgradeVersion();
        } else if (isVersionDifferent()){
        	System.out.println(">> VERSION DIFFERENT");
            onUpgrade();
            upgradeVersion();
        }

    }

    public void execute(String sql){
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void close(){
    	try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public int executeUpdate(String sql){
        try {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public Result query(String sql) {
        try {
            return new ResultDesktop(stmt.executeQuery(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
	private void loadDatabase(){
        File file = new File (DATABASE_NAME + ".db");
        if(!file.exists())
            nodatabase=true;
        try {
            Class.forName("org.sqlite.JDBC");
            db_connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME + ".db");
            stmt = db_connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void upgradeVersion() {
        execute("PRAGMA user_version="+database_version);
    }
    

    private boolean isNewDatabase() {
        return nodatabase;
    }
    
    
    private boolean isVersionDifferent(){
        Result q = query("PRAGMA user_version");
        if (!q.isEmpty()){
        	//System.out.println("CURRENT user_version: " + q.getString(1));
            return (Integer.parseInt(q.getString(1))!=database_version);
        }else {
            return true;
        }

    }
    
    
    
    public class ResultDesktop implements Result{

        ResultSet res;
        boolean called_is_empty=false;

        public ResultDesktop(ResultSet res) {
            this.res = res;
        }

        public boolean isEmpty() {
            try {
                if (res.getRow()==0){
                    called_is_empty=true;
                    return !res.next();
                }
                return res.getRow()==0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean moveToNext() {
            try {
                if (called_is_empty){
                    called_is_empty=false;
                    return true;
                } else
                    return res.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public int getColumnIndex(String name) {
            try {
                return res.findColumn(name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public float getFloat(int columnIndex) {
            try {
                return res.getFloat(columnIndex);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

		@Override
		public int getInt(int i) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getString(int i) {
			try {
			return res.getString(i);
	        } catch (SQLException e) {
	                e.printStackTrace();
	        }
	         return null;
		}


    }
    

}
