package br.odb.nodehunter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

public class ShowMainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_main_menu);

        findViewById( R.id.btnStartGame ).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_main_menu, menu);
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

    @Override
    public void onClick(View view) {

        boolean enableVRMode = ((CheckBox) findViewById( R.id.chkVRMode ) ).isChecked();
        boolean runInMultiplayer = ((CheckBox)findViewById( R.id.chkMultiplayer )).isChecked();
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra( "VRMODE", enableVRMode );
        intent.putExtra( "MULTIPLAYER", runInMultiplayer );
        startActivity( intent );
    }
}
