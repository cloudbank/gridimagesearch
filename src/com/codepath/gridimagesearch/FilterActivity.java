package com.codepath.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class FilterActivity extends Activity {

	GoogleImageSearchFilter filter;
	
	Spinner color,type,size;
	EditText site;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		setUpViews();
		filter = (GoogleImageSearchFilter) getIntent().getSerializableExtra("filter");
		fillFilterPage(filter);
	}

	private void setUpViews() {
		color = (Spinner) findViewById(R.id.colorSpin);
		type = (Spinner) findViewById(R.id.typeSpin);
		size = (Spinner) findViewById(R.id.sizeSpin);
		site = (EditText) findViewById(R.id.etSite);
		
	}
	public void setSpinnerToValue(Spinner spinner, String value) {
	    int index = 0;
	    SpinnerAdapter adapter = spinner.getAdapter();
	    for (int i = 0; i < adapter.getCount(); i++) {
	        if (adapter.getItem(i).equals(value)) {
	            index = i;
	            break; // terminate loop
	        }
	    }
	    spinner.setSelection(index);
	}
	private void fillFilterPage(GoogleImageSearchFilter filter2) {
		setSpinnerToValue(color, filter2.getColor());
		setSpinnerToValue(type, filter2.getType());
		setSpinnerToValue(size, filter2.getSize());
		site.setText(filter2.getSite());
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}
	
	// onclick for setting and serializing the filter object and passing it back to main activity
	public void saveFilter(View v) {
		filter = setFilterFromPage();
		Intent data = new Intent();
		data.putExtra("filter", filter);
		setResult(RESULT_OK, data);
		finish();
	}

	private GoogleImageSearchFilter setFilterFromPage() {
		String colorString =  color.getSelectedItem().toString();
		filter.setColor(colorString);
		String typeString =  type.getSelectedItem().toString();
		filter.setType(typeString);
		String sizeString =  size.getSelectedItem().toString();
		filter.setSize(sizeString);
		String siteString =  site.getText().toString();
		filter.setSite(siteString);
		return filter;
	}

}
