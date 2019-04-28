
package ceavi.udesc.br.quiz;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import ceavi.udesc.br.quiz.model.Academico;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nome, spe, sigla, email, spe_confirma;
    private FirebaseAuth mAuth;
    private LoginButton loginButton;
    private CallbackManager callBackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nome = findViewById(R.id.nome);
        spe = findViewById(R.id.spe);
        sigla = findViewById(R.id.sigla);
        email = findViewById(R.id.email);
        spe_confirma = findViewById(R.id.spe_confirma);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.bt_login).setOnClickListener(this);

        }

    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                registerUser(sigla.getText().toString(), spe.getText().toString(), nome.getText().toString(), email.getText().toString(), 0);
                break;
        }
    }

    private void registerUser(String siglaA, final String speF, String nomeA, String emailU, int pont) {
        final String name = nomeA;
        final String siglaF = siglaA;
        final String speA = speF;
        final String emailUs = emailU;
        final int pontuacao = pont;

        mAuth.createUserWithEmailAndPassword(emailUs, speA).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final Academico c = new Academico();
                    c.setNome(name);
                    c.setSpe(speA);
                    c.setFaculdade(siglaF);
                    c.setEmail(emailUs);
                    c.setPontuacao(0);

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference();

                    FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean existe = false;
                            if (dataSnapshot != null) {
                                for (DataSnapshot users : dataSnapshot.getChildren()) {
                                    if (users.child("email").getValue().toString().equalsIgnoreCase(emailUs)) {
                                        existe = true;
                                        if (existe) break;
                                    }
                                }
                                if (!existe) {
                                    ref.child("users").child(speA).setValue(c);
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Email já está em uso! Tente novamente!", Toast.LENGTH_SHORT).show();
                                    email.requestFocus();
                                    nome.setText("");
                                    spe.setText("");
                                    sigla.setText("");
                                    email.setText("");
                                    email.requestFocus();
                                    spe_confirma.setText("");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }else {
                    Toast.makeText(LoginActivity.this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
                  }
            }
        });
    }

}