package com.prueba.sistbanca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConectionSQLite extends SQLiteOpenHelper {

    String tblcliente = "CREATE TABLE cliente(id text, nombre text, correo text, contrasena text)";
    String tblcuenta = "CREATE TABLE cuenta (nrocuenta text, ident text, fecha text, saldo integer)";
    String tbltransaccion = "CREATE TABLE transaccion (nrocuenta text, fecha text, hora text, tipotrans text, valor integer)";


    public ConectionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BancaDB) {
        BancaDB.execSQL(tblcliente);
        BancaDB.execSQL(tblcuenta);
        BancaDB.execSQL(tbltransaccion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
