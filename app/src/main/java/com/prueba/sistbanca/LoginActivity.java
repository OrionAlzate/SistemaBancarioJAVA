package com.prueba.sistbanca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText txt_id, txt_contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_id = findViewById(R.id.txt_id);
        txt_contrasena = findViewById(R.id.txt_contrasena);

    }

    public void Ingresar (View v){

        String id = txt_id.getText().toString();
        String contrasena = txt_contrasena.getText().toString();

        if (!id.isEmpty() && !contrasena.isEmpty()) {

            ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
            SQLiteDatabase adminDB = conectorSQLite.getReadableDatabase();

            String compararExistente = "select id from cliente where id = '"+id+"'";
            Cursor encontrarExistente = adminDB.rawQuery(compararExistente,null);

            if (encontrarExistente.moveToFirst()) {

                String compararContra = "select contrasena from cliente where id = '"+ id +"' and contrasena = '"+contrasena+"'";
                Cursor encontrarContra = adminDB.rawQuery(compararContra,null);

                if (encontrarContra.moveToFirst()){

                    Intent Cuent = new Intent(getApplicationContext(), CuentaActivity.class);
                    Cuent.putExtra("idCliente",id);
                    Cuent.putExtra("contrasena",contrasena);
                    startActivity(Cuent);
                    adminDB.close();

                }else{
                    adminDB.close();
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();

                }



            }else {
                adminDB.close();
                Toast.makeText(this, "Usuario no Registrado", Toast.LENGTH_SHORT).show();

            }


        }else{

            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();

        }


    }

    public void CrearCuenta (View v){

        Intent Crear = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(Crear);

    }

}