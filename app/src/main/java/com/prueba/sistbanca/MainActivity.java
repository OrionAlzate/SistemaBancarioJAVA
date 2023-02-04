package com.prueba.sistbanca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txt_id, txt_nombre, txt_email, txt_contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_id = (EditText) findViewById(R.id.txt_id);
        txt_nombre = (EditText) findViewById(R.id.txt_nombre);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_contrasena = (EditText) findViewById(R.id.txt_contrasena);

    }

    // Metodo para registrar un cliente
    public void Registrar(View v){
        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getWritableDatabase();

        String id = txt_id.getText().toString();
        String nombre = txt_nombre.getText().toString();
        String email = txt_email.getText().toString();
        String contrasena = txt_contrasena.getText().toString();

        if (adminDB != null){
            if(!id.isEmpty() && !nombre.isEmpty() && !email.isEmpty() && !contrasena.isEmpty()){
                SQLiteDatabase comparar = conectorSQLite.getReadableDatabase();
                String compararExistente = "select id from cliente where id = '"+id+"'";
                Cursor encontrarExistente = comparar.rawQuery(compararExistente,null);

                if (!encontrarExistente.moveToFirst()) {

                    ContentValues contenedor = new ContentValues();
                    contenedor.put("id", id);
                    contenedor.put("nombre", nombre);
                    contenedor.put("correo", email);
                    contenedor.put("contrasena", contrasena);
                    adminDB.insert("cliente", null, contenedor);
                    adminDB.close();

                    txt_id.setText("");
                    txt_nombre.setText("");
                    txt_email.setText("");
                    txt_contrasena.setText("");

                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    adminDB.close();
                    Toast.makeText(this, "Cliente YA EXISTE, debe ingresar otro ID", Toast.LENGTH_SHORT).show();
                }

            }else {
                adminDB.close();
                Toast.makeText(this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
            }
        } else {
            adminDB.close();
            Toast.makeText(this, "La base de datos no existe", Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo Buscar un cliente
    public void Buscar(View v){
        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getReadableDatabase();
        String id = txt_id.getText().toString();
        if(!id.isEmpty()) {
            Cursor lectorSQLite = adminDB.rawQuery("SELECT nombre, correo FROM cliente WHERE id = '" + id + "'", null);

            if(lectorSQLite.moveToFirst()){
                txt_nombre.setText(lectorSQLite.getString(0));
                txt_email.setText(lectorSQLite.getString(1));
                adminDB.close();
            } else {
                Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
                txt_nombre.setText("");
                txt_email.setText("");
                txt_contrasena.setText("");
                adminDB.close();

            }
        } else {
            Toast.makeText(this, "Debe ingresar el ID para buscar al cliente", Toast.LENGTH_SHORT).show();
            txt_nombre.setText("");
            txt_email.setText("");
            txt_contrasena.setText("");
            adminDB.close();
        }
    }


    // Otra opcion para el Metodo Eliminar() cliente
    public void Eliminar(View v){
        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getWritableDatabase();

        String id = txt_id.getText().toString();
        String nombre = txt_nombre.getText().toString();
        String email = txt_email.getText().toString();
        String contrasena = txt_contrasena.getText().toString();

        if(!id.isEmpty() && !nombre.isEmpty() && !email.isEmpty() && !contrasena.isEmpty()){

            SQLiteDatabase comparar = conectorSQLite.getReadableDatabase(); // verificar que sea la misma contrasena del id que estamos eliminando
            Cursor passwordTest = comparar.rawQuery("SELECT contrasena FROM cliente WHERE id ='" + id + "'", null);

            if (passwordTest.moveToFirst()) {
                if (passwordTest.getString(0).equals(contrasena)) {

                    int cant = adminDB.delete("cliente", "id ='" + id + "'", null);

                    if (cant == 1) {
                        Toast.makeText(this, "Cliente '" + id + "' eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        txt_id.setText("");
                        txt_nombre.setText("");
                        txt_email.setText("");
                        txt_contrasena.setText("");
                        adminDB.close();

                    } else {
                        Toast.makeText(this, "Cliente '" + id + "' no encontrado", Toast.LENGTH_SHORT).show();
                        txt_id.setText("");
                        txt_nombre.setText("");
                        txt_email.setText("");
                        txt_contrasena.setText("");
                        adminDB.close();

                    }

                } else {
                    adminDB.close();

                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }  else {
                adminDB.close();
                Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
            }

        } else {
            adminDB.close();
            Toast.makeText(this, "Debe ingresar todos los datos del cliente para eliminarlo", Toast.LENGTH_SHORT).show();
        }
    }

    // metodo para modificar un cliente
    public void Modificar(View v){

        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getWritableDatabase();

        String id = txt_id.getText().toString();
        String nombre = txt_nombre.getText().toString();
        String email = txt_email.getText().toString();
        String contrasena = txt_contrasena.getText().toString();

        if(!id.isEmpty() && !nombre.isEmpty() && !email.isEmpty() && !contrasena.isEmpty()) {

            SQLiteDatabase comparar = conectorSQLite.getReadableDatabase(); // verificar que sea la misma contrasena del id que estamos eliminando
            Cursor passwordTest = comparar.rawQuery("SELECT contrasena FROM cliente WHERE id ='" + id + "'", null);

            if (passwordTest.moveToFirst()) {
                if (passwordTest.getString(0).equals(contrasena)) {

                    ContentValues contenedor = new ContentValues();
                    contenedor.put("nombre", nombre);
                    contenedor.put("correo", email);
                    contenedor.put("contrasena", contrasena);

                    int cant = adminDB.update("cliente", contenedor, "id="+id, null);

                    if(cant == 1){
                        Toast.makeText(this, "Cliente '"+id+"' modificado con éxito", Toast.LENGTH_SHORT).show();
                        txt_contrasena.setText("");
                        adminDB.close();

                    } else {
                        adminDB.close();
                        Toast.makeText(this, "Cliente '"+id+"' no ha sido modificado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    adminDB.close();
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }  else {
                adminDB.close();
                Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
            }
        } else {
            adminDB.close();
            Toast.makeText(this, "Debe llenar todos los campos para modificar el cliente", Toast.LENGTH_SHORT).show();
        }
    }

    public void bkLogin (View v){

        Intent Cuent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(Cuent);
    }
}