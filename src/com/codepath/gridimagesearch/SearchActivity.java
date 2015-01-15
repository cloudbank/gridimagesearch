package com.codepath.gridimagesearch;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	List<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter adapter;
	String currentQuery;
	GoogleImageSearchFilter filter = new GoogleImageSearchFilter();
	private final int REQUEST_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setUpViews();
		adapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(adapter);
		gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long arg3) {
			  Intent intent	= new Intent(getApplicationContext(),ImageDisplayActivity.class);
			  ImageResult result = imageResults.get(position);
			  intent.putExtra("result"	,result);
			  startActivity(intent);
			  }
			
		});
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				callGoogleApi(page);
			}
			
		});
		
	}

	public void setUpViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);

	}

	public void onImageSearch(View v) {
	    imageResults.clear();
		String query = etQuery.getText().toString();
		Toast.makeText(this, "searching for " + query, Toast.LENGTH_SHORT)
				.show();
		currentQuery = Uri.encode(query);
		callGoogleApi(0);

	}

	private void callGoogleApi(int start) {
		AsyncHttpClient client = new AsyncHttpClient();
		String urlString =	applyFilterToUrl("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&"+"start=" +start+"&v=1.0&q="+ currentQuery);
		client.get(urlString, 
		new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode,
		             org.apache.http.Header[] headers,JSONObject response) {
				JSONArray JSONResults = null;
				try {
					JSONResults = response.getJSONObject("responseData").getJSONArray("results");
					//imageResults.clear();
					adapter.addAll(ImageResult.fromJSONArray(JSONResults));
					
				    Log.d("DEBUG",imageResults.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			public void onFailure(int statusCode,
		             org.apache.http.Header[] headers,java.lang.Throwable throwable,JSONObject response) {
				Log.d("DEBUG","onfail");
			}
		});
	}

	
	private String applyFilterToUrl(String url) {
		//check the filter for values and add to url if there are any
		String add="";
		add+= (filter.getColor().equals("none") ? ""  : "&imgcolor="+filter.getColor());
		add+= (filter.getType().equals("none") ? ""  : "&imgtype="+filter.getType());
		add+= (filter.getSize().equals("none") ? "" : "&imgsz="+filter.getSize());
		add+= (filter.getSite().equals("none") ? "" : "&as_sitesearch="+filter.getSite());
		if(add.length() > 0){
			url += add;
		}
		Log.d("DEBUG",url);
		return url;
		
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_filter:
			goToFilterActivty();
			break;
		default:
			break;
		}
		return true;
	}
	
	
	private void goToFilterActivty() {
		  Intent intent	= new Intent(getApplicationContext(),FilterActivity.class);
		  intent.putExtra("filter",filter);
		  startActivityForResult(intent,REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	     // Extract name value from result extras
	     filter = (GoogleImageSearchFilter) data.getSerializableExtra("filter");
	     // Toast the name to display temporarily on screen
	  }
	} 
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
