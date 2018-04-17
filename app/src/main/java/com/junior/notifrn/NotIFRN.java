package com.junior.notifrn;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class NotIFRN extends AppCompatActivity {

    EditText editEmail, editSenha;
    Button btnLogar;

    String url = "";
    String parametro = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_ifrn);

        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        btnLogar = (Button) findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    String email = editEmail.getText().toString();
                    String senha = editSenha.getText().toString();

                    if (email.isEmpty() || senha.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
                    } else {

                        url = "https://portal.ifrn.edu.br";

                        parametro = "email=" + email + "&senha" + senha;

                        new SolicitaDados().execute(url);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class SolicitaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            //params comes from the execute() call: params[0] i the url.
            //try {
            return Conexao.postDados(urls[0], parametro);
            //} catch (IOException e){
            //    return "Unable to retrieve web page, URL may be invalid.";
            // }
        }

        //onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {

            if (resultado.equals("login_ok")) {
                Intent abreCaledario1 = new Intent(NotIFRN.this, Calendario.class);
                startActivity(abreCaledario1);
            } else {
                Toast.makeText(getApplicationContext(), "Usuário ou senha estão incorretos", Toast.LENGTH_LONG).show();
            }
        }
    }
}