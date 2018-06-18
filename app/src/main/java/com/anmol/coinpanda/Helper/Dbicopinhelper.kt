package com.anmol.coinpanda.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.anmol.coinpanda.Model.Icopin
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Model.Tweet

val ICOPIN_NAME = "icopindb"
val ICOPIN_TABLE_NAME = "icopin_table"
val COL_ICOPIN_NAME = "icopin_name"
val COL_ICOPIN_MESSAGE = "message"
val COL_ICOPIN_ID = "messageid"
val COL_ICOPIN_DATE = "messagedate"



class Dbicopinhelper (context: Context):SQLiteOpenHelper(context, ICOPIN_NAME,null,1){

    override fun onCreate(p0: SQLiteDatabase?) {
        val createtable = "CREATE TABLE " + ICOPIN_TABLE_NAME + " (" +
                COL_ICOPIN_ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE," +
                COL_ICOPIN_NAME + " VARCHAR(256)," +
                COL_ICOPIN_DATE + " VARCHAR(256)," +
                COL_ICOPIN_MESSAGE + " TEXT)"

        p0?.execSQL(createtable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $ICOPIN_TABLE_NAME")
        onCreate(p0)
    }

    fun insertData(icopin: Icopin){
        try{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ICOPIN_ID,icopin.cid)
        cv.put(COL_ICOPIN_NAME,icopin.icocoin_name)
        cv.put(COL_ICOPIN_DATE,icopin.pinneddate)
        cv.put(COL_ICOPIN_MESSAGE,icopin.pinned_messages)

            val result = db.insert(ICOPIN_TABLE_NAME,null,cv)
            if(result == (-1).toLong())
                System.out.println("sqlstatus is failed")
            else
                System.out.println("sqlstatus is successs")
        }catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }

    }
    fun readData(dataquery: String):MutableList<Icopin>{
        val icopins : MutableList<Icopin> = ArrayList()
        try {
            val db = this.readableDatabase
            val query = dataquery
            val result = db.rawQuery(query,null)
            if(result.moveToFirst()){
                do{
                    val cid = result.getString(result.getColumnIndex(COL_ICOPIN_ID))
                    val icopinname = result.getString(result.getColumnIndex(COL_ICOPIN_NAME))
                    val icopinmessage = result.getString(result.getColumnIndex(COL_ICOPIN_MESSAGE))
                    val icopindates = result.getString(result.getColumnIndex(COL_ICOPIN_DATE))

                    val icopin = Icopin(cid,icopinname,icopinmessage,icopindates)
                    icopins.add(icopin)
//                    if(booked.contains("0")){
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,false,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                    else{
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,true,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }

                }while (result.moveToNext())
            }
            result.close()
            db.close()
            System.out.println("inside:$icopins")

        }
        catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }
        System.out.println("outside:$icopins")
        return icopins
    }
//    fun readmyData():MutableList<Tweet>{
//        val tweets : MutableList<Tweet> = ArrayList()
//        try {
//            val db = this.readableDatabase
      //     val query = "Select * from " + TABLE_NAME + " where " + COL_COIN_SYMBOL + "=1 ORDER BY " + COL_ID + " DESC"
//            val result = db.rawQuery(query,null)
//            if(result.moveToFirst()){
//                do{
//                    val mcoin = result.getString(result.getColumnIndex(COL_COIN))
//                    val coin_symbol = result.getString(result.getColumnIndex(COL_COIN_SYMBOL))
//                    val mtweet = result.getString(result.getColumnIndex(COL_TWEET))
//                    val url = result.getString(result.getColumnIndex(COL_URL))
//                    val keyword = result.getString(result.getColumnIndex(COL_KEYWORD))
//                    val id = result.getString(result.getColumnIndex(COL_ID))
//                    val dates = result.getString(result.getColumnIndex(COL_DATES))
//                    val coinpage = result.getString(result.getColumnIndex(COL_COIN_HANDLE))
//                    val booked = result.getString(result.getColumnIndex(COL_BOOKMARK))
//                    if(booked.contains("0")){
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,false,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                    else{
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,true,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                }while (result.moveToNext())
//            }
//            result.close()
//            db.close()
//            System.out.println("inside:$tweets")
//
//        }
//        catch (e:SQLiteCantOpenDatabaseException){
//            e.printStackTrace()
//        }
//        System.out.println("outside:$tweets")
//        return tweets
//    }
//    fun readBookmarks():MutableList<Tweet>{
//        val tweets : MutableList<Tweet> = ArrayList()
//        try {
//            val db = this.readableDatabase
//            val query = "Select * from $TABLE_NAME where $COL_BOOKMARK=1 ORDER BY $COL_ID DESC"
//            val result = db.rawQuery(query,null)
//            if(result.moveToFirst()){
//                do{
//                    val mcoin = result.getString(result.getColumnIndex(COL_COIN))
//                    val coin_symbol = result.getString(result.getColumnIndex(COL_COIN_SYMBOL))
//                    val mtweet = result.getString(result.getColumnIndex(COL_TWEET))
//                    val url = result.getString(result.getColumnIndex(COL_URL))
//                    val keyword = result.getString(result.getColumnIndex(COL_KEYWORD))
//                    val id = result.getString(result.getColumnIndex(COL_ID))
//                    val dates = result.getString(result.getColumnIndex(COL_DATES))
//                    val coinpage = result.getString(result.getColumnIndex(COL_COIN_HANDLE))
//                    val booked = result.getString(result.getColumnIndex(COL_BOOKMARK))
//                    if(booked.contains("0")){
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,false,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                    else{
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,true,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                }while (result.moveToNext())
//            }
//            result.close()
//            db.close()
//            System.out.println("inside:$tweets")
//
//        }
//        catch (e:SQLiteCantOpenDatabaseException){
//            e.printStackTrace()
//        }
//        System.out.println("outside:$tweets")
//        return tweets
//    }
//
//    fun updatetweet(sqltweet: Sqltweet){
//        val db = this.writableDatabase
//        val cv = ContentValues()
//        cv.put(COL_BOOKMARK,sqltweet.booked)
//        db.update(TABLE_NAME,cv,"$COL_ID = ?", arrayOf(sqltweet.tweetid))
//        db.close()
//    }

}

