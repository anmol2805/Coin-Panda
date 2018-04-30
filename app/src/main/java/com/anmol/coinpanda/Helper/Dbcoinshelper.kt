package com.anmol.coinpanda.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Sqlcoin
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Model.Tweet

val DB_NAME = "coinsdb"
val TB_NAME = "coins_table"
val COIN = "coin"
val COIN_SYMBOL = "coin_symbol"
val COIN_HANDLE = "coin_handle"
val COIN_ID = "id"
class Dbcoinshelper (context: Context):SQLiteOpenHelper(context, DB_NAME,null,1){

    override fun onCreate(p0: SQLiteDatabase?) {
        val createtable = "CREATE TABLE " + TB_NAME + " (" +
                COIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COIN + " VARCHAR(256)," +
                COIN_SYMBOL + " VARCHAR(256) NOT NULL UNIQUE," +
                COIN_HANDLE + " VARCHAR(256))"

        p0?.execSQL(createtable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TB_NAME")
        onCreate(p0)
    }

    fun insertData(sqlcoin: Sqlcoin){
        try{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COIN,sqlcoin.coin)
        cv.put(COIN_SYMBOL,sqlcoin.coin_symbol)
        cv.put(COIN_HANDLE,sqlcoin.coinpage)

            val result = db.insert(TB_NAME,null,cv)
            if(result == (-1).toLong())
                System.out.println("coinstatus is failed")
            else
                System.out.println("coinstatus is successs")
        }catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }

    }
    fun readData():MutableList<Allcoin>{
        val allcoins : MutableList<Allcoin> = ArrayList()
        try {
            val db = this.readableDatabase
            val query = "Select * from $TB_NAME"
            val result = db.rawQuery(query,null)
            if(result.moveToFirst()){
                do{
                    val mcoin = result.getString(result.getColumnIndex(COIN))
                    val coin_symbol = result.getString(result.getColumnIndex(COIN_SYMBOL))
                    val coinpage = result.getString(result.getColumnIndex(COIN_HANDLE))
                    val allcoin = Allcoin(coin_symbol,mcoin,coinpage)
                    allcoins.add(allcoin)
                }while (result.moveToNext())
            }
            result.close()
            db.close()

        }
        catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }
        return allcoins
    }

}

