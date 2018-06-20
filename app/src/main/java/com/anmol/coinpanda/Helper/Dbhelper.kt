package com.anmol.coinpanda.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteReadOnlyDatabaseException
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Model.Tweet

val DATABASE_NAME = "tweetsdb"
val TABLE_NAME = "tweets_table"
val COL_COIN = "coin"
val COL_COIN_SYMBOL = "coin_symbol"
val COL_COIN_HANDLE = "coin_handle"
val COL_TWEET = "sqltweet"
val COL_URL = "url"
val COL_KEYWORD = "keyword"
val COL_ID = "tweet_id"
val COL_DATES = "dates"


class Dbhelper (context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,1){

    override fun onCreate(p0: SQLiteDatabase?) {
        val createtable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE," +
                COL_COIN + " VARCHAR(256)," +
                COL_COIN_SYMBOL + " VARCHAR(256)," +
                COL_COIN_HANDLE + " VARCHAR(256)," +
                COL_TWEET + " TEXT," +
                COL_URL + " TEXT," +
                COL_KEYWORD + " VARCHAR(256)," +
                COL_DATES + " TEXT)"

        p0?.execSQL(createtable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    fun insertData(sqltweet: Sqltweet){
        try{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ID,sqltweet.tweetid)
        cv.put(COL_COIN,sqltweet.coin)
        cv.put(COL_COIN_SYMBOL,sqltweet.coin_symbol)
        cv.put(COL_COIN_HANDLE,sqltweet.coinpage)
        cv.put(COL_TWEET,sqltweet.tweet)
        cv.put(COL_URL,sqltweet.url)
        cv.put(COL_KEYWORD,sqltweet.keyword)
        cv.put(COL_DATES,sqltweet.dates)
            val result = db.insert(TABLE_NAME,null,cv)
            if(result == (-1).toLong())
                System.out.println("sqlstatus is failed")
            else
                System.out.println("sqlstatus is successs")
        }catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }

    }
    fun readData(dataquery: String):MutableList<Tweet>{
        val tweets : MutableList<Tweet> = ArrayList()
        try {
            val db = this.readableDatabase
            val query = dataquery
            val result = db.rawQuery(query,null)
            if(result.moveToFirst()){
                do{
                    val mcoin = result.getString(result.getColumnIndex(COL_COIN))
                    val coin_symbol = result.getString(result.getColumnIndex(COL_COIN_SYMBOL))
                    val mtweet = result.getString(result.getColumnIndex(COL_TWEET))
                    val url = result.getString(result.getColumnIndex(COL_URL))
                    val keyword = result.getString(result.getColumnIndex(COL_KEYWORD))
                    val id = result.getString(result.getColumnIndex(COL_ID))
                    val dates = result.getString(result.getColumnIndex(COL_DATES))
                    val coinpage = result.getString(result.getColumnIndex(COL_COIN_HANDLE))
                    val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,false,dates,"mc",coinpage)
                    tweets.add(tweet)
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
            System.out.println("inside:$tweets")

        }
        catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }
        catch (e:SQLiteReadOnlyDatabaseException){
            e.printStackTrace()
        }
        System.out.println("outside:$tweets")
        return tweets
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

