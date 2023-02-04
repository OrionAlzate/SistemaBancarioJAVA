package com.prueba.sistbanca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TransaccionActivity extends AppCompatActivity {

    EditText cuentatra, fechatra, horatra, valortra;
    Spinner tipotrans;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaccion);

        cuentatra = findViewById(R.id.etcuentatra);
        fechatra = findViewById(R.id.etdatetra);
        horatra = findViewById(R.id.ethoratra);
        valortra = findViewById(R.id.etvalortra);
        tipotrans = (Spinner) findViewById(R.id.sptipoTra);

        cuentatra.setText(getIntent().getStringExtra("nrocuenta"));
        id = getIntent().getStringExtra("ident");

        String [] opcionesTra = {"Consignación", "Retiro"};
        ArrayAdapter <String> adaptadorTra = new ArrayAdapter<String>(this, R.layout.spinner_style,opcionesTra);
        tipotrans.setAdapter(adaptadorTra);

    }

    public void ReturnCuenta (View v) {

        Intent Cuenta = new Intent(getApplicationContext(), CuentaActivity.class);
        Cuenta.putExtra("identTrans", id);
        startActivity(Cuenta);
    }


    public void Retirar (View v){
        String spSeleccion = tipotrans.getSelectedItem().toString();

        if(spSeleccion.equals("Retiro")){
            

        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getReadableDatabase();

        String nrocuenta = cuentatra.getText().toString();
        String ident = id;
        String fecha = fechatra.getText().toString();
        String hora = horatra.getText().toString();
        String valor = valortra.getText().toString();

        if (!fecha.isEmpty() && !hora.isEmpty()  && !valor.isEmpty() && !spSeleccion.isEmpty()){

            String consulta = "select saldo from cuenta where nrocuenta ='"+nrocuenta+ "' and ident = '"+ ident +"'";
            Cursor buscarSaldo = adminDB.rawQuery(consulta,null);

            if (buscarSaldo.moveToFirst()) {

                //String saldoDisp = buscarSaldo.getString(0);

                int saldo = (Integer.parseInt(buscarSaldo.getString(0)));
                int valorTra = (Integer.parseInt(valor));
                int saldoDisponible = saldo - 10000;

                if (saldoDisponible >= valorTra) {

                    // saldo = (saldo - valorTra) - 10000; // asi es cobrando 10mil por comision de retiro
                    saldo = saldo - valorTra; // asi es sin comision de retiro

                    ContentValues contenedor = new ContentValues();
                    contenedor.put("saldo", saldo);
                    int cant = adminDB.update("cuenta", contenedor, "nrocuenta = '" + nrocuenta + "' and ident = '"+ident+"'", null);

                    if (cant == 1) {

                        Toast.makeText(this, "Retiro realizado con éxito", Toast.LENGTH_SHORT).show();

                        SQLiteDatabase transaccionDB = conectorSQLite.getWritableDatabase();
                        ContentValues contentTra = new ContentValues();
                        contentTra.put("nrocuenta", nrocuenta);

                        contentTra.put("fecha", fecha);
                        contentTra.put("hora", hora);
                        contentTra.put("tipotrans",spSeleccion);
                        contentTra.put("valor",valor);
                        transaccionDB.insert("transaccion",null,contentTra);
                        transaccionDB.close();


                        fechatra.setText("");
                        horatra.setText("");
                        valortra.setText("");




                    } else {

                        Toast.makeText(this, "Error, no se pudo realizar el retiro", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(this, "El valor del retiro debe ser inferior al saldo disponible", Toast.LENGTH_SHORT).show();

                }


            //  Toast.makeText(this, "Saldo " + saldoDisponible, Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(this, "Cuenta no encontrada", Toast.LENGTH_SHORT).show();

            }

        }else{

            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();

        }
    } else {
            Toast.makeText(this, "Eligió Retirar, debe seleccionar Tipo Transacción: Retiro", Toast.LENGTH_SHORT).show();
        }


    }

    public void Consignar (View v){

        ConectionSQLite conectorSQLite = new ConectionSQLite(this,"BancaDB",null,1);
        SQLiteDatabase adminDB = conectorSQLite.getReadableDatabase();

        String nrocuenta = cuentatra.getText().toString();
        String ident = id;
        String fecha = fechatra.getText().toString();
        String hora = horatra.getText().toString();
        String valor = valortra.getText().toString();
        String spSeleccion = tipotrans.getSelectedItem().toString();

        if(spSeleccion.equals("Consignación")){


            if (!fecha.isEmpty() && !hora.isEmpty()  && !valor.isEmpty() && !spSeleccion.isEmpty()){
            String consulta = "select saldo from cuenta where nrocuenta ='"+nrocuenta+ "' and ident = '"+ ident +"'";
            Cursor buscarSaldo = adminDB.rawQuery(consulta,null);

            if (buscarSaldo.moveToFirst()) {

                int saldo = (Integer.parseInt(buscarSaldo.getString(0)));
                int valorTra = (Integer.parseInt(valor));

                    saldo = saldo + valorTra;

                    ContentValues contenedor = new ContentValues();
                    contenedor.put("saldo", saldo);

                    int cant = adminDB.update("cuenta", contenedor, "nrocuenta = '" + nrocuenta + "' and ident = '"+ident+"'", null);

                    if (cant == 1) {

                        Toast.makeText(this, "Consignación realizada con éxito", Toast.LENGTH_SHORT).show();

                        SQLiteDatabase transaccionDB = conectorSQLite.getWritableDatabase();
                        ContentValues contentTra = new ContentValues();
                        contentTra.put("nrocuenta", nrocuenta);
                        contentTra.put("fecha", fecha);
                        contentTra.put("hora", hora);
                        contentTra.put("tipotrans",spSeleccion);
                        contentTra.put("valor",valor);
                        transaccionDB.insert("transaccion",null,contentTra);
                        transaccionDB.close();

                        fechatra.setText("");
                        horatra.setText("");
                        valortra.setText("");

                    } else {

                        Toast.makeText(this, "Error, no se pudo realizar la consignación", Toast.LENGTH_SHORT).show();

                    }



            }else{

                Toast.makeText(this, "Cuenta no encontrada", Toast.LENGTH_SHORT).show();

            }

            }else{

            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(this, "Eligió Consignar, debe seleccionar Tipo Transacción: Conisgnación", Toast.LENGTH_SHORT).show();
        }

    }


}