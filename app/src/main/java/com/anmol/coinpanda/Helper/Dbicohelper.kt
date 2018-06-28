package com.anmol.coinpanda.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.anmol.coinpanda.Fragments.ico
import com.anmol.coinpanda.Model.Icocoin
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Model.Tweet

val DATABASE_ICO = "icodb"
val TABLE_ICO = "ico_table"
val COL_ICO_NAME = "ico_name"
val COL_TELEGRAM = "telegram_url"
val COL_WEBSITE = "website"
val COL_MEDIUM_URL = "medium_url"
val COL_CROWDSALE_DATE = "crowdsale_date"
val COL_ICO_STATUS = "ico_status"
val COL_INDUSTRY = "industry"
val COL_DESCRIPTION = "description"
val COL_HARDCAP = "hardcap"
val COL_SOFTCAP = "softcap"
val COL_TWITTER_URL = "twitter_url"
val COL_RATING = "rating"


class Dbicohelper (context: Context):SQLiteOpenHelper(context, DATABASE_ICO,null,1){

    override fun onCreate(p0: SQLiteDatabase?) {
        val createtable = "CREATE TABLE " + TABLE_ICO + " (" +
                COL_ICO_NAME + " TEXT PRIMARY KEY NOT NULL UNIQUE," +
                COL_TELEGRAM + " VARCHAR(256)," +
                COL_WEBSITE + " VARCHAR(256)," +
                COL_MEDIUM_URL + " VARCHAR(256)," +
                COL_CROWDSALE_DATE + " TEXT," +
                COL_ICO_STATUS + " TEXT," +
                COL_INDUSTRY + " VARCHAR(256)," +
                COL_DESCRIPTION + " TEXT," +
                COL_HARDCAP + " TEXT," +
                COL_SOFTCAP + " TEXT," +
                COL_TWITTER_URL + " TEXT," +
                COL_RATING + " TEXT)"

        p0?.execSQL(createtable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_ICO")
        onCreate(p0)
    }

    fun insertData(icocoin: Icocoin){
        try{
        val db = this.writableDatabase
            db.enableWriteAheadLogging()
        val cv = ContentValues()
            cv.put(COL_ICO_NAME,icocoin.ico_name)
            cv.put(COL_TELEGRAM,icocoin.telegram_url)
            cv.put(COL_WEBSITE,icocoin.website)
            cv.put(COL_MEDIUM_URL,icocoin.medium_url)
            cv.put(COL_CROWDSALE_DATE,icocoin.crowdsale_date)
            cv.put(COL_ICO_STATUS,icocoin.ico_status)
            cv.put(COL_INDUSTRY,icocoin.industry)
            cv.put(COL_DESCRIPTION,icocoin.icodescription)
            cv.put(COL_HARDCAP,icocoin.hardcap)
            cv.put(COL_SOFTCAP,icocoin.softcap)
            cv.put(COL_TWITTER_URL,icocoin.twitter_url)
            cv.put(COL_RATING,icocoin.rating)

            val result = db.insert(TABLE_ICO,null,cv)
            if(result == (-1).toLong())
                System.out.println("sqlstatus is failed")
            else
                System.out.println("sqlstatus is successs")
        }catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }

    }
    fun readData(dataquery: String):MutableList<Icocoin>{
        val icocoins : MutableList<Icocoin> = ArrayList()
        try {
            val db = this.readableDatabase
            val query = dataquery
            val result = db.rawQuery(query,null)
            if(result.moveToFirst()){
                do{
                    val ico_name = result.getString(result.getColumnIndex(COL_ICO_NAME))
                    val telegram_url = result.getString(result.getColumnIndex(COL_TELEGRAM))
                    val website = result.getString(result.getColumnIndex(COL_WEBSITE))
                    val medium_url = result.getString(result.getColumnIndex(COL_MEDIUM_URL))
                    val crowdsale = result.getString(result.getColumnIndex(COL_CROWDSALE_DATE))
                    val icostatus = result.getString(result.getColumnIndex(COL_ICO_STATUS))
                    val industry = result.getString(result.getColumnIndex(COL_INDUSTRY))
                    val icodescription = result.getString(result.getColumnIndex(COL_DESCRIPTION))
                    val hardcap = result.getString(result.getColumnIndex(COL_HARDCAP))
                    val softcap = result.getString(result.getColumnIndex(COL_SOFTCAP))
                    val twitterurl = result.getString(result.getColumnIndex(COL_TWITTER_URL))
                    val rating = result.getString(result.getColumnIndex(COL_RATING))
                    val icocoin = Icocoin(ico_name,telegram_url,website,medium_url,crowdsale,icostatus,industry,icodescription,hardcap,softcap,twitterurl,rating)
                    icocoins.add(icocoin)
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
            System.out.println("inside:$icocoins")

        }
        catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }
        System.out.println("outside:$icocoins")
        return icocoins
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
    fun updatedata(icocoin: Icocoin){
        val db = this.writableDatabase
    db.enableWriteAheadLogging()
        val cv = ContentValues()
    cv.put(COL_TELEGRAM,icocoin.telegram_url)
    cv.put(COL_WEBSITE,icocoin.website)
    cv.put(COL_MEDIUM_URL,icocoin.medium_url)
    cv.put(COL_CROWDSALE_DATE,icocoin.crowdsale_date)
    cv.put(COL_ICO_STATUS,icocoin.ico_status)
    cv.put(COL_INDUSTRY,icocoin.industry)
    cv.put(COL_DESCRIPTION,icocoin.icodescription)
    cv.put(COL_HARDCAP,icocoin.hardcap)
    cv.put(COL_SOFTCAP,icocoin.softcap)
    cv.put(COL_TWITTER_URL,icocoin.twitter_url)
    cv.put(COL_RATING,icocoin.rating)

    db.update(TABLE_ICO,cv,"$COL_ICO_NAME = ?", arrayOf(icocoin.ico_name))
        db.close()
    }

}

