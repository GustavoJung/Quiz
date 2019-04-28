package ceavi.udesc.br.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction().add(R.id.fragment_container, new PreferenceFragment()).commit();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
