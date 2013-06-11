package com.laz.arya;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static final String TAG = "AryaMain";
	
	private EditText text;


	
	
	  // This method is called at button click because we assigned the name to the
	  // "OnClick property" of the button
	  public void onClick(View view) {
		  Log.d(TAG, "Button clicked!");
	    switch (view.getId()) {
	    case R.id.button1:
	      RadioButton celsiusButton = (RadioButton) findViewById(R.id.radio0);
	      RadioButton fahrenheitButton = (RadioButton) findViewById(R.id.radio1);
	      if (text.getText().length() == 0) {
	        Toast.makeText(this, "Please enter a valid number",
	            Toast.LENGTH_LONG).show();
	        return;
	      }

	      float inputValue = Float.parseFloat(text.getText().toString());
	      if (celsiusButton.isChecked()) {
	        text.setText(String
	            .valueOf(convertFahrenheitToCelsius(inputValue)));
	        celsiusButton.setChecked(false);
	        fahrenheitButton.setChecked(true);
	      } else {
	        text.setText(String
	            .valueOf(convertCelsiusToFahrenheit(inputValue)));
	        fahrenheitButton.setChecked(false);
	        celsiusButton.setChecked(true);
	      }
	      break;
	    }
	  }

	  // Converts to celsius
	  private float convertFahrenheitToCelsius(float fahrenheit) {
	    return ((fahrenheit - 32) * 5 / 9);
	  }

	  // Converts to fahrenheit
	  private float convertCelsiusToFahrenheit(float celsius) {
	    return ((celsius * 9) / 5) + 32;
	  }
	
	public void onGraphicsButtonClick(View view)
	{
		Log.d(TAG, "Switching to OpenGL activity...");
		Intent graphicsActivityIntent = new Intent(getApplicationContext(), GraphicsActivity.class);
		startActivity(graphicsActivityIntent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "MainActivity created!");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    text = (EditText) findViewById(R.id.editText1);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(TAG, "MainActivity restored!");
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		Log.d(TAG, "onNavigationItemSelected");
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
