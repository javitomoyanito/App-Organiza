package com.example.proyectobase.DB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AlumnoDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_ALUMNOS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT NOT NULL,
                $COL_APELLIDO TEXT NOT NULL,
                $COL_GRUPO TEXT NOT NULL,
                $COL_SECCION TEXT NOT NULL
            );
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALUMNOS")
        onCreate(db)
    }

    fun insert(nombre: String, apellido: String, grupo: String, seccion: String): Long {
        val cv = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_APELLIDO, apellido)
            put(COL_GRUPO, grupo)
            put(COL_SECCION, seccion)
        }
        return writableDatabase.insert(TABLE_ALUMNOS, null, cv)
    }

    fun getAll(): List<AlumnoLocal> {
        val out = mutableListOf<AlumnoLocal>()
        val sql = "SELECT $COL_ID, $COL_NOMBRE, $COL_APELLIDO, $COL_GRUPO, $COL_SECCION FROM $TABLE_ALUMNOS ORDER BY $COL_ID DESC"
        val c: Cursor = readableDatabase.rawQuery(sql, null)
        c.use {
            while (it.moveToNext()) {
                out += AlumnoLocal(
                    id = it.getInt(0),
                    nombre = it.getString(1),
                    apellido = it.getString(2),
                    grupo = it.getString(3),
                    seccion = it.getString(4)
                )
            }
        }
        return out
    }

    fun clear() {
        writableDatabase.delete(TABLE_ALUMNOS, null, null)
    }

    companion object {
        private const val DB_NAME = "alumnos.db"
        private const val DB_VERSION = 1

        const val TABLE_ALUMNOS = "alumnos_local"
        const val COL_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_APELLIDO = "apellido"
        const val COL_GRUPO = "grupo"
        const val COL_SECCION = "seccion"
    }
}

data class AlumnoLocal(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val grupo: String,
    val seccion: String
)
