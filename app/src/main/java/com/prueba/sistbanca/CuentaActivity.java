package com.prueba.sistbanca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CuentaActivity extends AppCompatActivity {

    EditText nrocuenta, ident2, date, saldo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        nrocuenta = (EditText) findViewById(R.id.etnrocuenta);
        ident2 = (EditText) findViewById(R.id.etidentcuenta);
        date = (EditText) findViewById(R.id.etdatecuenta);
        saldo = (EditText) findViewById(R.id.etsaldocuenta);

        String getId = getIntent().getStringExtra("idCliente");
        String getContrasena = getIntent().getStringExtra("contrasena");
        String idTrans = getIntent().getStringExtra("identTrans");

        if(idTrans==null){
            ident2.setText(getId);
        } else {
            ident2.setText(idTrans);
        }





    }

    public void Transaccion (View v) {

        String idCuenta = nrocuenta.getText().toString();
        String ident = ident2.getText().toString();


        if (!idCuenta.isEmpty()){
            ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
            SQLiteDatabase comparar = conectorSQLite.getReadableDatabase();
            String compararExistente = "select nrocuenta from cuenta where nrocuenta = '" +idCuenta+ "' and ident = '"+ident+"'";
            Cursor encontrarExistente = comparar.rawQuery(compararExistente, null);

            if (encontrarExistente.moveToFirst()){
                Intent Trans = new Intent(getApplicationContext(), TransaccionActivity.class);
                Trans.putExtra("nrocuenta", nrocuenta.getText().toString());
                Trans.putExtra("ident", ident);
                startActivity(Trans);
            } else {
                Toast.makeText(this, "Nro de Cuenta no existente", Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(this, "Debe seleccionar una cuenta", Toast.LENGTH_SHORT).show();
        }


    }

    public void ReturnCliente (View v) {
        
        

        Intent Cliente = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(Cliente);

    }

    public void Registrar (View v) {

        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getWritableDatabase();

        String idCuenta = nrocuenta.getText().toString();
        String dateCuenta = date.getText().toString();
        String saldoCuenta = saldo.getText().toString();
        String ident =  ident2.getText().toString();
        // deberia de poner aca el id del cliente

        if (!idCuenta.isEmpty() && !dateCuenta.isEmpty() && !saldoCuenta.isEmpty()){

            SQLiteDatabase comparar = conectorSQLite.getReadableDatabase();
            String compararExistente = "select nrocuenta, ident from cuenta where nrocuenta  = '" +idCuenta+ "' AND ident = '"+ident+"'";
            Cursor encontrarExistente = comparar.rawQuery(compararExistente, null);

            if (!encontrarExistente.moveToFirst()){

                //validar tambien el  ident de la cuenta, no solo el nrocuenta
                ContentValues contenedor = new ContentValues();
                contenedor.put("nrocuenta", idCuenta);
                contenedor.put("ident", ident);
                contenedor.put("fecha", dateCuenta);
                contenedor.put("saldo", saldoCuenta);
                adminDB.insert("cuenta", null, contenedor);
                adminDB.close();

                Toast.makeText(this, "Cuenta registrada Exitosamente", Toast.LENGTH_SHORT).show();

            }else{
                adminDB.close();
                Toast.makeText(this, "Cliente ya existente", Toast.LENGTH_SHORT).show();

            }

        }else{
            adminDB.close();
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();

        }


    }

    public void Buscar (View v){

        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getReadableDatabase();

        String nroCuenta = nrocuenta.getText().toString();
        String ident =  ident2.getText().toString();


        if (!nroCuenta.isEmpty()){

            String consulta = "select fecha, saldo from cuenta where nrocuenta = '" +nroCuenta+ "' and ident = '"+ident+"'";
            Cursor buscarCuenta = adminDB.rawQuery(consulta, null);

            if (buscarCuenta.moveToFirst()){

                date.setText(buscarCuenta.getString(0));
                saldo.setText(buscarCuenta.getString(1));
                adminDB.close();



            }else{
                adminDB.close();
                Toast.makeText(this, "Cuenta no encontrada", Toast.LENGTH_SHORT).show();

                date.setText("");
                saldo.setText("");

            }

        }else{
            adminDB.close();
            Toast.makeText(this, "Debe ingresar el numero de cuenta para buscar", Toast.LENGTH_SHORT).show();

        }

    }

    public void Modificar (View v){

        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getWritableDatabase();

        String idCuenta = nrocuenta.getText().toString();
        String ident =  ident2.getText().toString();
        String dateCuenta = date.getText().toString();
        String saldoCuenta = saldo.getText().toString();



        if (!idCuenta.isEmpty() && !dateCuenta.isEmpty() && !saldoCuenta.isEmpty()){

            SQLiteDatabase comparar = conectorSQLite.getReadableDatabase();
            String compararExistente = "select nrocuenta from cuenta where nrocuenta = '" +idCuenta+ "' and ident = '"+ident+"'";
            Cursor encontrarExistente = comparar.rawQuery(compararExistente, null);

            if (encontrarExistente.moveToFirst()){

                ContentValues contenedor = new ContentValues();
                contenedor.put("fecha", dateCuenta);
                contenedor.put("saldo", saldoCuenta);

                int cantidad = adminDB.update("cuenta", contenedor, "nrocuenta ='"+ idCuenta+ "' and ident = '"+ident+"'", null);

                if (cantidad==1){
                    adminDB.close();
                    Toast.makeText(this, "La cuenta ha sido modificada con éxito", Toast.LENGTH_SHORT).show();

                }else{
                    adminDB.close();

                    Toast.makeText(this, "La cuenta no ha sido modificada", Toast.LENGTH_SHORT).show();

                }

                adminDB.close();


            }else{
                adminDB.close();
                Toast.makeText(this, "Cuenta no existente", Toast.LENGTH_SHORT).show();

            }

        }else{
            adminDB.close();
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();

        }

    }

    public void Eliminar (View v){

        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getWritableDatabase();

        String idCuenta = nrocuenta.getText().toString();
        String dateCuenta = date.getText().toString();
        String saldoCuenta = saldo.getText().toString();
        String ident =  ident2.getText().toString();


        if (!idCuenta.isEmpty()){

            SQLiteDatabase comparar = conectorSQLite.getReadableDatabase();
            String compararExistente = "select nrocuenta from cuenta where nrocuenta = '" +idCuenta+ "' and ident + '"+ident+"'";
            Cursor encontrarExistente = comparar.rawQuery(compararExistente, null);

            if (encontrarExistente.moveToFirst()){

                int cantidad = adminDB.delete("cuenta", "nrocuenta = '"+ idCuenta+ "'",null);

                if (cantidad==1){
                    adminDB.close();

                    Toast.makeText(this, "Cuenta eliminada exitosamente", Toast.LENGTH_SHORT).show();
                    nrocuenta.setText("");
                    date.setText("");
                    saldo.setText("");

                }else{
                    adminDB.close();

                    Toast.makeText(this, "No se ha podido eliminar la cuenta", Toast.LENGTH_SHORT).show();

                    date.setText("");
                    saldo.setText("");

                }

            }else{

                Toast.makeText(this, "Cuenta no encontrada", Toast.LENGTH_SHORT).show();
                adminDB.close();

                date.setText("");
                saldo.setText("");

            }

        }else{
            adminDB.close();
            Toast.makeText(this, "Debe ingresar el número de cuenta", Toast.LENGTH_SHORT).show();

        }

    }

}