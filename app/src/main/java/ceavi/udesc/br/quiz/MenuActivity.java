package ceavi.udesc.br.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button play;
    private Button opcoes;
    private Button ranking;
    private Button sair;
    private FirebaseAuth mAuth;
    private String spe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        play = (Button) this.findViewById(R.id.bt_play);
        opcoes = (Button) this.findViewById(R.id.bt_opcoes);
        ranking = (Button) this.findViewById(R.id.bt_ranking);
        sair = (Button) this.findViewById(R.id.bt_sair);

        this.findViewById(R.id.bt_play).setOnClickListener(this);
        this.findViewById(R.id.bt_opcoes).setOnClickListener(this);
        this.findViewById(R.id.bt_ranking).setOnClickListener(this);
        this.findViewById(R.id.bt_sair).setOnClickListener(this);

        spe = getIntent().getStringExtra("spe");

        mAuth = FirebaseAuth.getInstance();

    }

    public void onStart() {
        super.onStart();
    }

    public void sair(View v){
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void opcoes(){
        Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void jogar(){
        Intent intent = new Intent(MenuActivity.this, CategoriasActivity.class);
        System.out.println(spe);
        intent.putExtra("spe", spe);
        startActivity(intent);
        finish();
    }

    public void ranking(){
        Intent intent = new Intent(MenuActivity.this, RankingActivity.class);
        intent.putExtra("spe", spe);
        startActivity(intent);
        finish();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_sair:
                sair(v);
                break;
            case R.id.bt_opcoes:
               opcoes();
                break;
            case R.id.bt_play:
                jogar();
                break;
            case R.id.bt_ranking:
                ranking();
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed(){
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
