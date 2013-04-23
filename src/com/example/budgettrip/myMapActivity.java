/*
 * Code from 
 * http://stackoverflow.com/questions/11745314/why-retrieving-google-directions-for-android-using-kml-data-is-not-working-anymo/11745316#11745316
 * used to calculate distances and draw a route using google directions.
 */
package com.example.budgettrip;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class myMapActivity extends MapActivity {

	GeoPoint p1;
	GeoPoint p2;
	MapController mc;
	
	class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, 
        boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p1, screenPts);
 
            Bitmap bmp = BitmapFactory.decodeResource(
                getResources(), R.drawable.androidmarker);            
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-58, null);         
            return true;
        }
    } 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	MapView mapView = (MapView) findViewById(R.id.mapView);
    	mapView.setBuiltInZoomControls(true);
    	mc = mapView.getController();
    	List<Overlay> mapOverlays = mapView.getOverlays();
    	Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
    	IconsOverlay itemizedoverlay = new IconsOverlay(drawable, this);
    	GeoPoint point = new GeoPoint(19240000,-99120000);
    	mc.animateTo(point);
    	OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
    	itemizedoverlay.addOverlay(overlayitem);
    	mc.animateTo(new GeoPoint((int)(37.42291810*1E6), (int)(-122.08542120*1E6)));
    	GeoPoint[] testpoints = {new GeoPoint((int)(37.42291810*1E6), (int)(-122.08542120*1E6)), new GeoPoint((int)(40.7142298*1E6), (int)(-73.9614669*1E6))};
    	new RouteTask().execute(testpoints);
    	mapOverlays.add(itemizedoverlay);
    }

    
    @Override
    protected boolean isRouteDisplayed(){
    	return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.add_plan:
        	return true;
        case R.id.about:
        	return true;
        case R.id.help:
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
    
    private Route directions(final GeoPoint start, final GeoPoint dest) {
        Parser parser;
        String jsonURL = "http://maps.google.com/maps/api/directions/json?";
        final StringBuffer sBuf = new StringBuffer(jsonURL);
        sBuf.append("origin=");
        sBuf.append(start.getLatitudeE6()/1E6);
        sBuf.append(',');
        sBuf.append(start.getLongitudeE6()/1E6);
        sBuf.append("&destination=");
        sBuf.append(dest.getLatitudeE6()/1E6);
        sBuf.append(',');
        sBuf.append(dest.getLongitudeE6()/1E6);
        sBuf.append("&sensor=true&mode=driving");
        parser = new GoogleParser(sBuf.toString());
        Route r =  parser.parse();
        return r;
    }
    
    private class RouteTask extends AsyncTask <GeoPoint[], Void, Double> {

		@Override
		protected Double doInBackground(GeoPoint[]... params) {
			int count = params.length;
			double totalDistance = 0;
			for (int i = 0; i < count; i++) {
				if (params[i].length == 2) {
					Route r = directions(params[i][0], params[i][1]);
					if (r.getDistance() == 0) {
						break;
					}
					//RouteOverlay routeOverlay = new RouteOverlay(r, Color.BLUE);
					MapView mapView = (MapView) findViewById(R.id.mapView);
					//mapView.getOverlays().add(routeOverlay);
					totalDistance += r.getDistance();
				}
			}
			return totalDistance;
		}
		
		protected void onPostExecute(Double result) {
			Context context = getApplicationContext();
			CharSequence text = result.intValue() + " Hello toast!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
	     }
    	
    	
    }
}
