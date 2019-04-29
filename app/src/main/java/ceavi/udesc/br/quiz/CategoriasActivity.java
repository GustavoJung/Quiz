package ceavi.udesc.br.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class CategoriasActivity extends AppCompatActivity implements View.OnClickListener{

    private Button cat1;
    private Button cat2;
    private Button cat3;
    private Button cat4;
    private FirebaseAuth mAuth;

    private String spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        spe = getIntent().getStringExtra("spe");

        cat1 = (Button) this.findViewById(R.id.bt_cat1);
        cat2 = (Button) this.findViewById(R.id.bt_cat2);
        cat3 = (Button) this.findViewById(R.id.bt_cat3);
        cat4 = (Button) this.findViewById(R.id.bt_cat4);

        this.findViewById(R.id.bt_cat1).setOnClickListener(this);
        this.findViewById(R.id.bt_cat2).setOnClickListener(this);
        this.findViewById(R.id.bt_cat3).setOnClickListener(this);
        this.findViewById(R.id.bt_cat4).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_cat1:
                 categoriaSelected("geografia");
                break;
            case R.id.bt_cat2:
                categoriaSelected("química");
                break;
            case R.id.bt_cat3:
                categoriaSelected("física");
                break;
            case R.id.bt_cat4:
                categoriaSelected("gerais");
                break;

        }
    }

    private void categoriaSelected(String categoria) {
        Intent intent = new Intent(CategoriasActivity.this, PlayActivity.class);
        intent.putExtra("categoria",categoria);
        intent.putExtra("spe", spe);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(CategoriasActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }


}
