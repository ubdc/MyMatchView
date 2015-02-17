package com.example.mymatchview;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.mymatchview.MatchView.Line;

public class MainActivity extends Activity implements OnSeekBarChangeListener {
	MatchView matchView;
	EditText txt;
	SeekBar seekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		matchView = (MatchView) findViewById(R.id.mv);
		matchView.setLines(Alphabet.match("WWW.GOOGLE.COM"));
		
		txt = (EditText) findViewById(R.id.txt);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(this);
	}
	
	public void setText(View v) {
		List<Line> match = Alphabet.match(txt.getText().toString());
		matchView.setLines(match);
	}
	
	public void annimateIn(View v) {
		matchView.animIn();
	}
	
	public void annimateOut(View v) {
		matchView.animOut();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		matchView.setAnimProgress(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
	
	
}
