
package ceavi.udesc.br.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import ceavi.udesc.br.quiz.model.Academico;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nome, spe, sigla, email, spe_confirma;
    private FirebaseAuth mAuth;

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
        findViewById(R.id.bt_cadastrar).setOnClickListener(this);
        }

    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cadastrar:
                existe(spe.getText().toString());
                break;
        }
    }

    public void existe(final String spe) {
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = dataBase.getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("if1");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        System.out.println("for");
                        if (ds.getKey().equalsIgnoreCase(spe) || ds.child("email").getValue().toString().equalsIgnoreCase(email.getText().toString())) {
                            System.out.println(ds.getKey() + " uashd " + spe);
                            System.out.println(ds.child("email").getValue().toString() + " uashd " + email.getText().toString());
                            Toast.makeText(LoginActivity.this, "EMAIL OU SPE JÁ CADASTRADO!", Toast.LENGTH_LONG).show();
                            break;
                        } else {
                            registerUser(sigla.getText().toString(), spe, nome.getText().toString(), email.getText().toString(), 0);
                            break;
                        }
                    }
                }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    private void registerUser(final String siglaA, final String speF, String nomeA, String emailU, int pont) {
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

                    //instancia atual do banco
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //referencia a um determinado pai, passado abaixo, nesse caso 'users'.
                    final DatabaseReference ref = database.getReference();
                        //adicionando um listener para inserção de dados
                    FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {//verifica se a busca ao banco já retornou algo, e somente a partir dai continua o codigo
                                ref.child("users").child(speA).setValue(c);//na referencia 'users', no filho 'speA' é
                                // setado o valor C (um objeto nesse caso).
                                mAuth.signInWithEmailAndPassword(emailUs, speA);
                                appendFacul(speA, siglaF);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }
        });
    }

    private void appendFacul(final String speA, final String sigla) {
        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference referer = database1.getReference("faculdades");

        referer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    referer.child(sigla.toUpperCase()).child(speA).setValue(speA);
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    intent.putExtra("spe", speA);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}