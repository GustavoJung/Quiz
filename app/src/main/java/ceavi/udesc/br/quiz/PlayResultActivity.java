package ceavi.udesc.br.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayResultActivity extends AppCompatActivity implements View.OnClickListener {
    private String categoria;
    private int pontuacao_rodada;

    private TextView pontuacao;
    private Button playAgain;
    private Button changeCategory;
    private Button seeRankings;
    private Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_result);
        categoria = getIntent().getStringExtra("categoria");
        pontuacao_rodada = Integer.parseInt(getIntent().getStringExtra("pontuacao") );

        pontuacao = findViewById(R.id.pontuacao);
        pontuacao.setText("Você fez " + pontuacao_rodada + " pontos!");
        playAgain = findViewById(R.id.jogar_novamente);
        changeCategory = findViewById(R.id.mudar_categoria);
        seeRankings = findViewById(R.id.ver_rankinkgs);
        menu = findViewById(R.id.bt_inicio);

        pontuacao.setOnClickListener(this);
        playAgain.setOnClickListener(this);
        changeCategory.setOnClickListener(this);
        seeRankings.setOnClickListener(this);
        menu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jogar_novamente:
                Intent intent1 = new Intent(PlayResultActivity.this, PlayActivity.class);
                intent1.putExtra("categoria",categoria+"");
                startActivity(intent1);
                finish();
                break;
            case R.id.mudar_categoria:
                Intent intent2 = new Intent(PlayResultActivity.this, CategoriasActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.ver_rankinkgs:
                Intent intent3 = new Intent(PlayResultActivity.this,RankingActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.bt_inicio:
                Intent intent4 = new Intent(PlayResultActivity.this,MainActivity.class);
                startActivity(intent4);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlayResultActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
