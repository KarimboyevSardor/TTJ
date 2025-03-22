package com.example.talabalarniroyxatgaolish.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.talabalarniroyxatgaolish.data.Auth

class MyDatabase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), MyDbService {
    companion object {
        private const val DB_NAME = "auth"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "auth_table"
        private const val ID = "id"
        private const val NAME = "name"
        private const val ROLE = "role"
        private const val AUTH_ID = "auth_id"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val query = "create table $TABLE_NAME($ID integer primary key autoincrement not null, $NAME text not null, $ROLE text not null, $AUTH_ID integer not null)"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.let { db ->
            db.execSQL("CREATE TABLE auth_table_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "auth_id INTEGER NOT NULL)"
            )

            db.execSQL("INSERT INTO auth_table_new (id, name, role, auth_id)" +
                    "SELECT id, name, role, auth_id FROM auth_table")

            db.execSQL("DROP TABLE auth_table")

            db.execSQL("ALTER TABLE auth_table_new RENAME TO auth_table")
        }
    }

    override fun setAuth(auth: Auth) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, auth.name)
        contentValues.put(ROLE, auth.role)
        contentValues.put(AUTH_ID, auth.id)
        db.insert(TABLE_NAME, null, contentValues)
    }

    override fun getAuth(): MutableList<Auth> {
        val auths: MutableList<Auth> = mutableListOf()
        val db = this.readableDatabase
        val cursor = db.rawQuery("select * from $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(1)
                val role = cursor.getString(2)
                val auth_id = cursor.getLong(3)
                val auth = Auth(id = auth_id, name = name, role = role)
                auths.add(auth)
            } while (cursor.moveToNext())
        }
        return auths
    }

    override fun deleteAuth() {
        this.writableDatabase.delete(TABLE_NAME, "$ID > ?", arrayOf("0"))
    }
}