package com.junior.notifrn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class NotIFRN extends AppCompatActivity {

    EditText editEmail, editSenha;
    Button btnLogar;

    String email = "", senha  = "";
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

                    email = editEmail.getText().toString();
                    senha = editSenha.getText().toString();

                    if (email.isEmpty() || senha.isEmpty()) {
                        if (!(editEmail.getText().toString().trim().length() > 0)){
                            editEmail.setError("Matricula");
                        }
                        if (!(editSenha.getText().toString().trim().length() > 0)){
                            editSenha.setError("Senha");
                        }
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
                    } else {
                        new Autenticacao().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class Autenticacao extends AsyncTask<Object, Object, String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(NotIFRN.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            try {

                JSONObject userValues = new JSONObject();
                userValues.put("username", email);
                userValues.put("password", senha);
                String jsonStr = userValues.toString();
                Log.i("::CODEJSON", "OK");

                HttpRequest json = HttpRequest
                        .post("https://suap.ifrn.edu.br/api/v2/autenticacao/token/")
                        .header("Content-Type", "application/json")
                        .send(jsonStr);

                String jsonObject = json.body();

                JSONObject token = new JSONObject(jsonObject);
                Log.i("::CODEJSON", "erro: "+json.code());
                Log.i("::CODEJSON", "erro: "+json.message());

                String accessToken = token.getString("token");

                Log.i("::CODEJSON", accessToken);

                return accessToken;

            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String token) {

            progressDialog.cancel();

            if (token != null){
//                Toast.makeText(NotIFRN.this, token, Toast.LENGTH_SHORT).show();
                Intent abreCaledario1 = new Intent(NotIFRN.this, Calendario.class);
                startActivity(abreCaledario1);

            }else {
                Toast.makeText(NotIFRN.this, "Erro", Toast.LENGTH_SHORT).show();
            }
        }
    }
}