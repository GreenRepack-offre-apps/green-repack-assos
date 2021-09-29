package com.greenrepack.greenrepackassos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greenrepack.greenrepackassos.databinding.ActivityRegisterBinding;
import com.greenrepack.greenrepackassos.service.ApiBuilder;
import com.greenrepack.greenrepackassos.service.ApiResult;
import com.greenrepack.greenrepackassos.service.Status;
import com.greenrepack.greenrepackassos.service.accessibility.AccessApiCall;
import com.greenrepack.greenrepackassos.service.accessibility.AccessAssosRequest;
import com.greenrepack.greenrepackassos.ui.login.LoginActivity;
import com.greenrepack.greenrepackassos.utils.AppContextValue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ApiResult<Status> registerResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerResult = new ApiResult<>();
        //setContentView(R.layout.activity_register);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final EditText password2EditText = binding.password2;
        final Button registerButton = binding.register;
        final Button loginAccess = binding.loginLink;
        final TextView error_text = binding.errorTxt;

        loginAccess.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        registerButton.setOnClickListener(v -> {

            error_text.setVisibility(View.GONE);
            error_text.setText("");

            final String formResult = evaluateForm(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(), password2EditText.getText().toString());
            error_text.setText(formResult);
            if(!formResult.isEmpty()) {
                error_text.setVisibility(View.VISIBLE);
            }else{
                error_text.setVisibility(View.GONE);
                registerServiceCall(AccessAssosRequest.builder()
                        .rna(usernameEditText.getText().toString())
                        .password(passwordEditText.getText().toString())
                        .build());
                if(!registerResult.isHasError() && registerResult.getResult() != null) {
                    if(registerResult.getResult().getStatus().equals("ECHEC")){
                        error_text.setText("Les informations saisis sont invalides !");
                        error_text.setVisibility(View.VISIBLE);
                    }
                    else if(registerResult.getResult().getStatus().equals("ECHEC_RNA")) {
                        error_text.setText("Cet identifiant RNA n'existe pas !");
                        error_text.setVisibility(View.VISIBLE);
                    }else if(registerResult.getResult().getStatus().equals("EXIST")) {
                        error_text.setText("Le compte pour cet identifiant rna existe déja !");
                        error_text.setVisibility(View.VISIBLE);
                    }else if(registerResult.getResult().getStatus().equals("SUCCES")) {
                        //
                    }
                }else{
                    error_text.setText(registerResult.getMsgError());
                    error_text.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String evaluateForm(String rnaId, String pswd, String pswd2) {
        String error = "";
        boolean valid = true;
        if(rnaId.trim().isEmpty() || pswd.trim().isEmpty() || pswd2.trim().isEmpty()) {
            if(rnaId.trim().isEmpty()){
                error += "Le champs de l'identifiant rna est requis !";
            }
            if(pswd.trim().isEmpty() || pswd2.trim().isEmpty()){
                error += "Le ou les champs du mot de passe est requis !";
            }
            valid = false;
        } else if(!(pswd.length() >= 5) || !(pswd2.length() >= 5) ) {
            error += "Le champs du mot de passe est petit !";
            valid = false;
        } else if(!pswd.equals(pswd2)){
            error += "Les champs du mot de passe ne correspondent pas !";
            valid = false;
        }
        return error;
    }

    private void registerServiceCall(AccessAssosRequest request){
        try{
            AccessApiCall apiCall = ApiBuilder.builder(getApplicationContext()).create(AccessApiCall.class);

            apiCall.register(request).enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if(response.isSuccessful()) {
                        registerResult.setResult(response.body());
                        //Toast.makeText(this, "Vous pouvez vous connecter !", Toast.LENGTH_LONG);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    registerResult.setHasError(true);
                    registerResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                    t.printStackTrace();
                }
            });
        }catch (Exception e){
            registerResult.setHasError(true);
            registerResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
            e.printStackTrace();
        }
    }
}