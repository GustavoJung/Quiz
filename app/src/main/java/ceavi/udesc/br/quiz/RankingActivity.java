package ceavi.udesc.br.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(RankingActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
