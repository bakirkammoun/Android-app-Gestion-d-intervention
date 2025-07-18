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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;

public class SecondFragment extends Fragment {

    private String username, password;
    private EditText usernameLogin, passwordLogin;
    private Button loginButton;
    private ProgressDialog prgDialog;

    private View v;
    private TextView tv;

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(com.example.gestionintervention.R.layout.fragment_second, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameLogin = v.findViewById(com.example.gestionintervention.R.id.usernameLogin);
        passwordLogin = v.findViewById(com.example.gestionintervention.R.id.passwordLogin);
        loginButton = v.findViewById(com.example.gestionintervention.R.id.login);
        tv = v.findViewById(com.example.gestionintervention.R.id.errorMessage);

        prgDialog = new ProgressDialog(this.getContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        loginButton.setOnClickListener(view1 -> loginUser());
    }

    public void loginUser() {
        username = usernameLogin.getText().toString().trim();
        password = passwordLogin.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            invokeWS(username, password);
        } else {
            Toast.makeText(this.getContext(), "Veuillez remplir les deux champs.", Toast.LENGTH_LONG).show();
        }
    }

    private void invokeWS(String username, String password) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();

        // Encoder l'URL pour éviter les erreurs liées aux caractères spéciaux
        String url;
        try {
            url = "http://192.168.96.169:8086/user/login/" +
                    URLEncoder.encode(username, "UTF-8") + "&" +
                    URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(), "Erreur dans l'encodage de l'URL.", Toast.LENGTH_LONG).show();
            prgDialog.hide();
            return;
        }

        client.get(url, null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                try {
                    String response = new String(responseBody, "UTF-8");

                    if (statusCode == 200 && Boolean.parseBoolean(response)) {
                        Toast.makeText(SecondFragment.this.getContext(), "Connexion réussie !", Toast.LENGTH_LONG).show();

                        // Navigation vers le fragment suivant
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        NavHostFragment.findNavController(SecondFragment.this)
                                .navigate(com.example.gestionintervention.R.id.action_LoginFragment_to_InterventionsFragment, bundle);
                    } else {
                        Toast.makeText(SecondFragment.this.getContext(), "Nom d'utilisateur ou mot de passe incorrect.", Toast.LENGTH_LONG).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(SecondFragment.this.getContext(), "Erreur dans la réponse du serveur.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(SecondFragment.this.getContext(), "Ressource demandée introuvable.", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(SecondFragment.this.getContext(), "Erreur interne du serveur.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SecondFragment.this.getContext(), "Erreur inattendue ! Vérifiez votre connexion internet.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
