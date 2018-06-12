package com.anmol.coinpanda.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.anmol.coinpanda.Model.*
import org.jetbrains.anko.db.update

val DNAME = "keydb"
val TNAME = "keytable"
val KEY = "key"
val KEYVALUE = "keyvalue"
class Dbtopicshelper (context: Context):SQLiteOpenHelper(context, DNAME,null,1){

    override fun onCreate(p0: SQLiteDatabase?) {
        val createtable = "CREATE TABLE " + TNAME + " (" +
                KEY + " TEXT PRIMARY KEY NOT NULL UNIQUE," +
                KEYVALUE + " INTEGER)"

        p0?.execSQL(createtable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TNAME")
        onCreate(p0)
    }

    fun insertData(value:Value){
        try{
        val db = this.writableDatabase
        val cv = ContentValues()
            cv.put(KEY,value.key)
            cv.put(KEYVALUE,value.keyvalue)
            db.insert(TNAME,null,cv)
//            if(result == (-1).toLong())
//                System.out.println("coinstatus is failed")
//            else
//                System.out.println("coinstatus is successs")
        }catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }

    }
    fun readData(): ArrayList<Value> {
        val values = ArrayList<Value>()
        try {
            val db = this.readableDatabase
            val query = "Select * from $TNAME"
            val result = db.rawQuery(query,null)

            if(result.moveToFirst()){
                do{
                    val key = result.getString(result.getColumnIndex(KEY))
                    val keyvalue = result.getInt(result.getColumnIndex(KEYVALUE))
                    val value = Value(key,keyvalue)
                    values.add(value)
                }while (result.moveToNext())
            }
            result.close()
            db.close()

        }
        catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }
        return values
    }

    fun updatedata(value: Value) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(KEYVALUE,value.keyvalue)
        db.update(TNAME, cv,"$KEY = ?", arrayOf(value.key))
        db.close()
    }

}

