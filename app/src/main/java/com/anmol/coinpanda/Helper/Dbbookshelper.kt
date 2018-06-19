package com.anmol.coinpanda.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteReadOnlyDatabaseException
import android.widget.Toast
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Sqlcoin
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Model.Tweet

val DB = "booksdb"
val TB = "books_table"
val TWEETID = "tweetid"
class Dbbookshelper (context: Context):SQLiteOpenHelper(context, DB,null,1){

    override fun onCreate(p0: SQLiteDatabase?) {
        val createtable = "CREATE TABLE " + TB + " (" +
                TWEETID + " INTEGER PRIMARY KEY NOT NULL UNIQUE)"

        p0?.execSQL(createtable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TB")
        onCreate(p0)
    }

    fun insertData(tweetid:String){
        try{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(TWEETID,tweetid)

            val result = db.insert(TB,null,cv)
            if(result == (-1).toLong())
                System.out.println("coinstatus is failed")
            else
                System.out.println("coinstatus is successs")
        }catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }

    }
    fun readbook():ArrayList<String>{
        val tweets :ArrayList<String> = ArrayList()
        try {
            val db = this.readableDatabase
            val query = "Select * from $TB"
            val result = db.rawQuery(query,null)
            if(result.moveToFirst()){
                do{
                    val mtweetid = result.getString(result.getColumnIndex(TWEETID))
                    tweets.add(mtweetid)
                }while (result.moveToNext())
            }
            result.close()
            db.close()

        }
        catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }
        catch (e:SQLiteReadOnlyDatabaseException){
            e.printStackTrace()
        }
        return tweets
    }

    fun deletebook(tweetid:String) {
        val db = this.writableDatabase
        db.delete(TB, "$TWEETID = ?", arrayOf(tweetid))
        db.close()
    }

}

