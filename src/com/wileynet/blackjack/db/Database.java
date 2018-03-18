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

//General class that needs to be implemented on Android and Desktop Applications
public abstract class Database {

  public final String DATABASE_NAME = "blackjackwileynet1";
  protected static Database instance = null;
  public final int database_version=1;
  
  
  //all tables
  private static final String KEY_ROWID = "_id";
  //blackjack table
  private static final String DB_BLACKJACK_TABLE = "blackjackscore";
  private static final String PLAYER_ID = "player_id";
  private static final String PLAYER_SCORE = "player_score";
  private static final String DB_BLACKJACK_CREATE = 
  	"CREATE TABLE IF NOT EXISTS " + DB_BLACKJACK_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + PLAYER_ID + " INTEGER NOT NULL, " + PLAYER_SCORE + " VARCHAR NOT NULL);";
  
  
  //Runs a sql query like "create".
  public abstract void execute(String sql);

  //Identical to execute but returns the number of rows affected (useful for updates)
  public abstract int executeUpdate(String sql);

  //Runs a query and returns an Object with all the results of the query. [Result Interface is defined below]
  public abstract Result query(String sql);
  
  public void onCreate(){
	  execute(DB_BLACKJACK_CREATE);
  }
  
  public void close(){}
  
  /*
  public void onCreate(){
      //Example of Highscore table code (You should change this for your own DB code creation)
      execute("CREATE TABLE 'highscores' ('_id' INTEGER PRIMARY KEY  NOT NULL , 'name' VARCHAR NOT NULL , 'score' INTEGER NOT NULL );");
      execute("INSERT INTO 'highscores'(name,score) values ('Cris',1234)");
      //Example of query to get DB data of Highscore table
      Result q=query("SELECT * FROM 'highscores'");
      if (!q.isEmpty()){
          q.moveToNext();
          System.out.println("Highscore of " + 
          q.getString(q.getColumnIndex("name")) + 
          ": " + q.getString(q.getColumnIndex("score")));
      }
  }
  */

  public void onUpgrade(){
	  //http://stackoverflow.com/questions/3424156/upgrade-sqlite-database-from-one-version-to-another
      execute("DROP TABLE IF EXISTS 'blackjackscore';");
      onCreate();
      System.out.println("DB Upgrade made because I hard-coded database_version in Database.java");
  }
  
  //Interface to be implemented on both Android and Desktop Applications
  public interface Result{
      public boolean isEmpty();
      public boolean moveToNext();
      public int getColumnIndex(String name);
      public float getFloat(int columnIndex);
	  public int getInt(int i);
	  public String getString(int i);
  }
  
}
