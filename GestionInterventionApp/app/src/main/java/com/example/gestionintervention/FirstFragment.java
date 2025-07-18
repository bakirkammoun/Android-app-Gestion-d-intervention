package com.example.gestionintervention;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import classes.User;
import classes.Utility;
import cz.msebera.android.httpclient.Header;

public class FirstFragment extends Fragment {
    ProgressDialog prgDialog;
    private String userName, email, password;
    EditText userNameInput, passwordInput, emailInput;
    Button registerUserButton;
    TextView linkToLogin;
    private View v;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        v = inflater.inflate(com.example.gestionintervention.R.layout.fragment_first, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        userNameInput = (EditText) v.findViewById(com.example.gestionintervention.R.id.userName);
        passwordInput = (EditText) v.findViewById(com.example.gestionintervention.R.id.password);
        emailInput = (EditText) v.findViewById(com.example.gestionintervention.R.id.email);
        prgDialog = new ProgressDialog(this.getContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        registerUserButton = (Button) v.findViewById(com.example.gestionintervention.R.id.regesterUser);

        // Register user button click listener
        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = userNameInput.getText().toString();
                password = passwordInput.getText().toString();
                email = emailInput.getText().toString();
                registerUser(view);
            }
        });

        // Link to login screen
        linkToLogin = (TextView) v.findViewById(com.example.gestionintervention.R.id.linkToLogin);
        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("The user already have an account ");
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(com.example.gestionintervention.R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    // Register user method
    public void registerUser(View view) {
        userName = userNameInput.getText().toString();
        password = passwordInput.getText().toString();
        email = emailInput.getText().toString();
        User user = new User(userName, password, email);
        RequestParams params = new RequestParams();

        // Validation checks
        if (Utility.isNotNull(userName) && Utility.isNotNull(password) && Utility.isNotNull(email)) {
            if (Utility.validate(email)) {
                params.put("username", userName);
                params.put("password", password);
                params.put("email", email);
                System.out.println(params.toString());
                invokeWS(params); // Call web service
            } else {
                Toast.makeText(this.getContext(), "Veuillez entrer un email valide", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.getContext(), "Veuillez remplir les deux champs.", Toast.LENGTH_LONG).show();
        }
    }

    // Web service call
    private void invokeWS(RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();

        // Add Content-Type header if necessary
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");

        // Make the POST request
        client.post("http://192.168.96.169:8086/user/register", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                try {
                    // Display raw server response for debugging
                    String response = new String(responseBody, "UTF-8");
                    System.out.println("Server response: " + response);

                    JSONArray obj = new JSONArray(response);
                    if (statusCode == 200) {  // success
                        Toast.makeText(FirstFragment.this.getContext(), "Your account was created successfully ", Toast.LENGTH_LONG).show();
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(com.example.gestionintervention.R.id.action_FirstFragment_to_SecondFragment);
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(FirstFragment.this.getContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(FirstFragment.this.getContext(), "Username already exists. Try another one", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(FirstFragment.this.getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FirstFragment.this.getContext(), "Unexpected Error occured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
