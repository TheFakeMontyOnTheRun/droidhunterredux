package br.odb.robothunter.dalvik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AngstronDroidHunter extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_angstron_droid_hunter);
		
		
		findViewById( R.id.btnPlayGame ).setOnClickListener( this );
	}

	@Override
	public void onClick(View v) {
		
		Intent intent = new Intent( this, PlayLevelActivity.class );
		Bundle bundle = new Bundle();
		bundle.putString( PlayLevelActivity.LEVEL_NAME_KEY, "testeless.level" );
		intent.putExtras( bundle );
		this.startActivity( intent );		
	}
}
