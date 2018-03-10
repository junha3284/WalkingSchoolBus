package com.jade.walkinggroupbus.walkingschoolbus.proxy;

//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonRequest;

/**
 * Abstract base class for the VOTO Mobile proxy classes. 
 */
abstract public class WGServerProxyBase {
//
//	protected static final String TAG = "WGProxy";
//	protected static final String SERVER_SCHEME = "https";
//	protected static final String SERVER_ADDRESS = "cmpt276-1177-bf.cmpt.sfu.ca:8443";
//
//	protected Response.ErrorListener errorListener;
//	private Context context;
//
//
//	/*
//	 * Constructors
//	 */
//	public WGServerProxyBase(Context context) {
//		this.context = context;
//		this.errorListener = getErrorListener(context);
//	}
//	public WGServerProxyBase(Context context, Response.ErrorListener errorListener) {
//		this.context = context;
//		this.errorListener = errorListener;
//	}
//
//
//	/*
//	 * URL Building Routines
//	 */
//	protected String buildUrl(String path) {
//		return buildUrl(path, new HashMap<String, String>());
//	}
//
//	protected String buildUrl(String path, Map<String, String> parameters) {
//		Uri.Builder builder = new Uri.Builder();
//		builder.scheme(SERVER_SCHEME);
//		builder.authority(SERVER_ADDRESS);
//		builder.appendEncodedPath(path);
//
//		// Add all parameters:
//		for (Map.Entry<String, String> entry : parameters.entrySet()) {
//			builder.appendQueryParameter(entry.getKey(), entry.getValue());
//		}
//
//
//		String url = builder.build().toString();
//		logMessage("Built URL: " + url);
//		return url;
//	}
//
//
//	/*
//	 * Send to queue
//	 */
//	protected <T> void sendRequest(JsonRequest<T> req) {
//		logMessage("Sending Request " + req.toString());
//		VotoRequestQueue.addToRequestQueue(context.getApplicationContext(), req);
//	}
//
//	/*
//	 * Default error handler: put up a message box.
//	 */
//	public static Response.ErrorListener getErrorListener(final Context context) {
//		return new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				// If this cast fails, it's because the context is likely an application
//				// context, not an Activity context. If using this code, must have
//				// passed in an Activity to the constructor!
//				Activity activity = (Activity) context;
//
//				displayDefaultErrorBox(activity, error);
//			}
//       };
//	}
//
//	public static void displayDefaultErrorBox(Activity context, VolleyError error) {
//		String errorMessage = getErrorMessage(error);
//
//		AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(context);
//		dlgBuilder.setTitle("Network Error");
//		dlgBuilder.setMessage(errorMessage);
//		dlgBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//		    @Override
//		    public void onClick(DialogInterface dialog, int which) {
//		        dialog.dismiss();
//		    }
//		});
//		dlgBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//		dlgBuilder.setCancelable(true); // This allows the 'BACK' button
//		dlgBuilder.create().show();
//	}
//
//	private static String getErrorMessage(VolleyError error) {
//
//		Log.e(TAG, "Got error: " , error);
//
//		String errorMessage = "";
//		if (error == null) {
//			errorMessage = "Something went wrong accessing the server, but no details available.";
//		} else if (error.getMessage() != null) {
//			errorMessage = "Error message: " + error.getMessage();
//		} else {
//			// Try finding message in response data (JSON from VOTO server)
//			String msg = "";
//			try {
//				String contents = new String(error.networkResponse.data, "UTF-8");
//				JSONObject response = new JSONObject(contents);
//				msg = response.getString("message");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			errorMessage = "Server says: \n" + msg + "\n\nError class: " + error;
//		}
//		return errorMessage;
//	}
//
//
//
//	/*
//	 * Logging
//	 */
//	public static void logMessage(String message) {
//		Log.i(TAG, message);
//	}
}