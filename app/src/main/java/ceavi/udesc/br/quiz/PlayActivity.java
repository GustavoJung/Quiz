package ceavi.udesc.br.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import ceavi.udesc.br.quiz.model.Academico;
import ceavi.udesc.br.quiz.model.Questao;


//Essa classe é o controlador da parte jogável (selecionar resposta, etc..).
//Cada turno se dá por 4 questões, obtidas do banco de dados, aleatoriamente selecionadas.
public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaração de variáveis
    FirebaseDatabase fire; //declaração do banco
    String categoria;
    private static final long START_TIME_IN_MILLIS = 16000;
    private TextView mTimer;
    private CountDownTimer mCountDown;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private String correctAnswer;
    private Button mAnswerA, mAnswerB, mAnswerC, mAnswerD, mAnswerE;
    private int pontuacao;
    private List<Questao> questoes_banco = new ArrayList<>();
    private Random r = new Random();
    private String spe;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        fire = FirebaseDatabase.getInstance();//instanciação do banco

        spe = getIntent().getStringExtra("spe");

        mAnswerA = findViewById(R.id.id_alternativa1);
        mAnswerB = findViewById(R.id.id_alternativa2);
        mAnswerC = findViewById(R.id.id_alternativa3);
        mAnswerD = findViewById(R.id.id_alternativa4);
        mAnswerE = findViewById(R.id.id_alternativa5);

        mAnswerA.setOnClickListener(this);
        mAnswerB.setOnClickListener(this);
        mAnswerC.setOnClickListener(this);
        mAnswerD.setOnClickListener(this);
        mAnswerE.setOnClickListener(this);

        mTimer = findViewById(R.id.timer);

        categoria = getIntent().getStringExtra("categoria");
        fillingInformation(categoria.trim());
    }

    private void startTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        mCountDown = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_alternativa1:
                confereResposta(mAnswerA.getText().toString(),mAnswerA);
                break;
            case R.id.id_alternativa2:
                confereResposta(mAnswerB.getText().toString(), mAnswerB);
                break;
            case R.id.id_alternativa3:
                confereResposta(mAnswerC.getText().toString(), mAnswerC);
                break;
            case R.id.id_alternativa4:
                confereResposta(mAnswerD.getText().toString(), mAnswerD);
                break;
            case R.id.id_alternativa5:
                confereResposta(mAnswerE.getText().toString(), mAnswerE);
                break;
        }
    }

    private void confereResposta(String text, Button bt) {
        mAnswerA.setEnabled(false);
        mAnswerB.setEnabled(false);
        mAnswerC.setEnabled(false);
        mAnswerD.setEnabled(false);
        mAnswerE.setEnabled(false);

        if (text.equalsIgnoreCase(correctAnswer)) {
            pontuacao += 10;
            bt.setBackgroundColor(Color.GREEN);
        } else {
            bt.setBackgroundColor(Color.RED);
        }

        if (questoes_banco.size() > 0) {
            new Handler().postDelayed(new Runnable(){
                public void run(){
                    mCountDown.cancel();
                    fillButtons();
                }
            },800);
        } else {
            final Intent intent = new Intent(PlayActivity.this, PlayResultActivity.class);
                intent.putExtra("categoria", categoria + "");
                intent.putExtra("pontuacao", pontuacao + "");
                intent.putExtra("spe", spe);

                //instancia atual do banco de dados
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                //referencia ao banco em determinado 'pai'. Nesse caso, a referencia é direto ao pai 'users'.
            final DatabaseReference ref = database.getReference("users");
                //adicionando um listener a essa referencia
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Academico u = new Academico();
                    if (dataSnapshot.exists()) {//essa verificação é "necessária" pois os listeners do firebase são assíncronos
                        //ou seja, eles não esperam o resultado da busca ao banco chegar para continuar o código, eles continuam direto, com essa veri-
                        //ficação, o código acontece se o dado do banco existe, evitando null pointers e erros.

                        for (DataSnapshot d : dataSnapshot.getChildren()) {//esse for percorre todos os filhos da referencia 'users'
                            if (d.getKey().equalsIgnoreCase(spe)) {
                                u.setEmail(d.child("email").getValue().toString());
                                u.setFaculdade(d.child("faculdade").getValue().toString());
                                u.setNome(d.child("nome").getValue().toString());
                                u.setPontuacao((Integer.parseInt(d.child("pontuacao").getValue().toString())) + pontuacao);
                                u.setSpe(spe);
                                System.out.println("if antes do firebase ");
                                ref.child(spe).setValue(u);
                                System.out.println("if depois");
                                break;
                            }
                        }

                    }else{
                        System.out.println("DataSnapshot não existe");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            System.out.println("countdown");
            mCountDown.cancel();
            startActivity(intent);
            finish();
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        if (seconds > 0) {
            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            mTimer.setText(timeLeftFormatted);
        }
        if(seconds==0){
            System.out.println("zero");
            mTimeLeftInMillis= START_TIME_IN_MILLIS;
            fillButtons();
        }
    }

    private void fillingInformation(final String categoria) {
        fire.getReference().child("questoes").child(categoria).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Questao q;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                q = new Questao();
                                q.setQuestao(ds.child("questao").getValue().toString());
                                q.setRespostaA(ds.child("respostaA").getValue().toString());
                                q.setRespostaB(ds.child("respostaB").getValue().toString());
                                q.setRespostaC(ds.child("respostaC").getValue().toString());
                                q.setRespostaD(ds.child("respostaD").getValue().toString());
                                q.setRespostaE(ds.child("respostaE").getValue().toString());
                                q.setRespostaCorreta(ds.child("respostaCorreta").getValue().toString());
                                questoes_banco.add(q);
                            }
                            fillButtons();
                        } else {
                            System.out.println("ERROR");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("ERROR");
                    }
                });
       }

        private void fillButtons () {

            mAnswerA.setBackgroundColor(Color.WHITE);
            mAnswerB.setBackgroundColor(Color.WHITE);
            mAnswerC.setBackgroundColor(Color.WHITE);
            mAnswerD.setBackgroundColor(Color.WHITE);
            mAnswerE.setBackgroundColor(Color.WHITE);

            mAnswerA.setEnabled(true);
            mAnswerB.setEnabled(true);
            mAnswerC.setEnabled(true);
            mAnswerD.setEnabled(true);
            mAnswerE.setEnabled(true);

            Questao a = new Questao();
            a = questoes_banco.get(r.nextInt(questoes_banco.size()));
            questoes_banco.remove(a);

            TextView questao = (TextView) this.findViewById(R.id.id_questao);
            questao.setText(a.getQuestao());

            mAnswerA.setText(a.getRespostaA());
            mAnswerB.setText(a.getRespostaB());
            mAnswerC.setText(a.getRespostaC());
            mAnswerD.setText(a.getRespostaD());
            mAnswerE.setText(a.getRespostaE());
            correctAnswer = a.getRespostaCorreta();

            startTimer();
    }

        @Override
        public void onBackPressed () {
            Intent intent = new Intent(PlayActivity.this, CategoriasActivity.class);
            mCountDown.cancel();
            startActivity(intent);
            finish();
        }
    }
