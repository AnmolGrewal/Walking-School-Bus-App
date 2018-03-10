package com.cmpt276.project.walkinggroupapp.datamanagers;

//import com.android.volley.VolleyError;
//Fixed Spelling
/**
 * Abstract base class for other data managers to factor out the code for being observable.
 * To use with an Android activity:
 * 1. Have the activity implement the DataManagerListener interface.
 * 2. Override the networkError() and dataChanged() methods
 * 3. Override the onResume() method, use it to register itself as a listener.
 * 4. Override the onPause() method, use it to unregister itself as a listener.
 * 
 * sample code for client usage:
  	// Listener support for data managers
	@Override
	protected void onPause() {
		super.onPause();
		App.getSurveyManager().removeListener(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		App.getSurveyManager().addListener(this);
	}

	@Override
	public void networkError(VolleyError volleyError) {
		VotoProxyBase.displayDefaultErrorBox(this, volleyError);			
	}

	@Override
	public void dataChanged() {
		// Do something! Get the list of items of interest by doing something like:
		for (Survey survey : App.getSurveyManager().surveys()) {
			... process that survey...
		}
	}
 */
abstract public class AbstractManager {
//	private List<DataManagerListener> listeners = new ArrayList<DataManagerListener>();
//
//
//	public void addListener(DataManagerListener listener) {
//		listeners.add(listener);
//	}
//	public void removeListener(DataManagerListener listener) {
//		listeners.remove(listener);
//	}
//
//	protected void notifyDataChangeListeners(){
//		for (DataManagerListener listener : listeners) {
//			listener.dataChanged();
//		}
//	}
//	protected void notifyErrorListeners(VolleyError volleyError){
//		for (DataManagerListener listener : listeners) {
//			listener.networkError(volleyError);
//		}
//	}
}
