package com.greenrepack.greenrepackassos.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.greenrepack.greenrepackassos.ProjectsOverviewActivity;
import com.greenrepack.greenrepackassos.R;
import com.greenrepack.greenrepackassos.RegisterActivity;
import com.greenrepack.greenrepackassos.service.ApiBuilder;
import com.greenrepack.greenrepackassos.service.ApiResult;
import com.greenrepack.greenrepackassos.service.ResponseData;
import com.greenrepack.greenrepackassos.service.Status;
import com.greenrepack.greenrepackassos.service.accessibility.AccessApiCall;
import com.greenrepack.greenrepackassos.service.accessibility.AccessAssosRequest;
import com.greenrepack.greenrepackassos.service.assos.AssosGet;
import com.greenrepack.greenrepackassos.service.assos.ReponseAssos;
import com.greenrepack.greenrepackassos.ui.login.LoginViewModel;
import com.greenrepack.greenrepackassos.ui.login.LoginViewModelFactory;
import com.greenrepack.greenrepackassos.databinding.ActivityLoginBinding;
import com.greenrepack.greenrepackassos.utils.AppContextValue;

import java.time.Duration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private ApiResult<ReponseAssos> loginServiceResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginServiceResult = new ApiResult<>();

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final Button registerAccess = binding.registerLink;
        final TextView error_text = binding.errorTxt;

        usernameEditText.setText("W9C1000189");
        passwordEditText.setText("mdp1111");

        registerAccess.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                error_text.setVisibility(View.GONE);
                error_text.setText("");

                final String formResult = evaluateForm(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                error_text.setText(formResult);
                if(!formResult.isEmpty()) {
                    error_text.setVisibility(View.VISIBLE);
                }else {
                    error_text.setVisibility(View.GONE);
                   loginServiceCall(AccessAssosRequest.builder()
                            .rna(usernameEditText.getText().toString())
                            .password(passwordEditText.getText().toString())
                            .build());
                    if (!loginServiceResult.isHasError() && loginServiceResult.getResult() != null) {
                        if (loginServiceResult.getResult().getStatus().equals("ECHEC_RNA")) {
                            error_text.setText("Les informations sur l'identifiant sont invalides !");
                            error_text.setVisibility(View.VISIBLE);
                        } else if (loginServiceResult.getResult().getStatus().equals("ECHEC_RNA_PSWD")) {
                            error_text.setText("L'identifiant rna ou le mot de passe est erroné !");
                            error_text.setVisibility(View.VISIBLE);
                        } else if (loginServiceResult.getResult().getStatus().equals("SUCCES")) {
                            Toast.makeText(getApplicationContext(), "connected !! :)", Duration.ofSeconds(5).getNano()).show();
                            startActivity(new Intent(getApplicationContext(), ProjectsOverviewActivity.class));
                        }
                    } else {
                        error_text.setText(loginServiceResult.getMsgError());
                        error_text.setVisibility(View.VISIBLE);
                    }
                }
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private String evaluateForm(String rnaId, String pswd) {
        String error = "";
        boolean valid = true;
        if(rnaId.trim().isEmpty() || pswd.trim().isEmpty()) {
            if(rnaId.trim().isEmpty()){
                error += "Le champs de l'identifiant rna est requis !";
            }
            if(pswd.trim().isEmpty()){
                error += "Le champs du mot de passe est requis !";
            }
            valid = false;
        } else if(!(pswd.length() >= 5)) {
            error += "Le champs du mot de passe est petit !";
            valid = false;
        }
        return error;
    }
    private void loginServiceCall(AccessAssosRequest request) {
        try{
            AccessApiCall apiCall = ApiBuilder.builder(getApplicationContext()).create(AccessApiCall.class);
            apiCall.login(request).enqueue(new Callback<ReponseAssos>() {
                @Override
                public void onResponse(Call<ReponseAssos> call, Response<ReponseAssos> response) {
                    if(response.isSuccessful()) {
                        loginServiceResult.setResult(response.body());
                    }
                }
                @Override
                public void onFailure(Call<ReponseAssos> call, Throwable t) {
                    loginServiceResult.setHasError(true);
                    loginServiceResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                    t.printStackTrace();
                }
            });
        }catch (Exception e){
            loginServiceResult.setHasError(true);
            loginServiceResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
            e.printStackTrace();
        }
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}