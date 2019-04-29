package ceavi.udesc.br.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppEventsLogger.activateApp(getApplication());
        mAuth = FirebaseAuth.getInstance();

        choose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void choose(){


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.popup_inicial,null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button bt_cadastrar = (Button) mView.findViewById(R.id.bt_cadastrar);
        Button bt_login = (Button) mView.findViewById(R.id.bt_logar);
        ImageView im = (ImageView) mView.findViewById(R.id.image);
        im.setImageResource(R.drawable.spe_image);

        final EditText spe = (EditText) mView.findViewById(R.id.spe_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfUsernameExists(spe.getText().toString(), spe);
            }
        });

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.putExtra("spe", spe.getText().toString());
                startActivity(intent);
                    dialog.dismiss();
                finish();

            }
        });
    }

    private void checkIfUsernameExists(final String username, final EditText et) {
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference reference = dataBase.getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean controle = false;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equalsIgnoreCase(username)) {
                            controle = true;
                            break;
                        } else {
                            controle = false;
                        }
                    }
                    if (controle == true) {
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        intent.putExtra("spe", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "SPE INVÁLIDO OU INEXISTENTE!", Toast.LENGTH_LONG).show();
                        et.setText("");
                        et.requestFocus();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    }

