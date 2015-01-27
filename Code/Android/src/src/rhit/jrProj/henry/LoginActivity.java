package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

	/**
	 * A dialog that displays during the authentication process saying "Authenticating with Firebase..."
	 */
	private ProgressDialog mAuthProgressDialog;

	private Firebase ref;

	/* Data from the authenticated user */
	private AuthData authData;

	/* A tag that is used for logging statements */
	private static final String TAG = "LoginDemo";

	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;

	private GlobalVariables mGlobalVariables;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGlobalVariables =  ((GlobalVariables) getApplicationContext());
		ref = new Firebase(mGlobalVariables.getFirebaseUrl());
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(0x268bd2));
		
		Firebase.setAndroidContext(this);
		setContentView(R.layout.activity_login);
		this.mAuthProgressDialog = new ProgressDialog(this);
		this.mAuthProgressDialog.setTitle("Loading");
		this.mAuthProgressDialog.setMessage("Authenticating with Firebase...");
		this.mAuthProgressDialog.setCancelable(false);
		if(!((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE)){
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}


		// Set up the login form.
		this.mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		populateAutoComplete();

		this.mPasswordView = (EditText) findViewById(R.id.password);
		this.mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				attemptLogin();
			}
		});

		Button mEmailRegister = (Button) findViewById(R.id.register);
		mEmailRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				registerNewUser(view);
			}
		});

		this.mLoginFormView = findViewById(R.id.login_form);
		this.mProgressView = findViewById(R.id.login_progress);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		// Reset errors.
		this.mEmailView.setError(null);
		this.mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = this.mEmailView.getText().toString();
		String password = this.mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			this.mPasswordView
					.setError(getString(R.string.error_invalid_password));
			focusView = this.mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			this.mEmailView.setError(getString(R.string.error_field_required));
			focusView = this.mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			this.mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = this.mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			this.mAuthProgressDialog.show();
			loginWithPassword(email, password);
			showProgress(false);
		}
	}

	/**
	 * Opens up a list list of projects after logging in.
	 * 
	 * @param authdata
	 */
	public void openProjectListView(AuthData authdata) {

		this.mAuthProgressDialog.hide();
		Intent intent = new Intent(this, WelcomeActivity.class);
		intent.putExtra("user",
				mGlobalVariables.getFirebaseUrl() + "users/" + this.authData.getUid());
		this.startActivity(intent);
		this.finish();
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			this.mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			this.mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							LoginActivity.this.mLoginFormView
									.setVisibility(show ? View.GONE
											: View.VISIBLE);
						}
					});

			this.mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			this.mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							LoginActivity.this.mProgressView
									.setVisibility(show ? View.VISIBLE
											: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			this.mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			this.mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * Shows the user's primary email address stores on their Android, used for autocomplete
	 */
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	/**
	 * sets up autocomplete for email addresses
	 */
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}
	
	/**
	 * does nothing right now
	 */
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	/**
	 * Authenticates the user with Firebase, using the specified email address and password
	 * @param username
	 * @param password
	 */
	public void loginWithPassword(String username, String password) {
		this.ref.authWithPassword(username, password, new AuthResultHandler(
				"password"));
	}

	/**
	 * Populates the autocomplete feature
	 */
	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}
	
	/**
	 * Displays an alert dialog that contains the specified error message
	 * @param message
	 */
	private void showErrorDialog(String message) {
		new AlertDialog.Builder(this).setTitle("Error").setMessage(message)
				.setPositiveButton(android.R.string.ok, null)
				.setIcon(android.R.drawable.ic_dialog_alert).show();
	}
	
	/**
	 * Create adapter to tell the AutoCompleteTextView what to show in its
	 * dropdown list.
	 * @param emailAddressCollection
	 */
	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				LoginActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				emailAddressCollection);

		this.mEmailView.setAdapter(adapter);
	}
	
	/**
	 * Gets the primary email address associated with this phone in order to add it to the autocomplete list
	 *
	 */
	private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}

	/**
	 * Verifies the user provided a valid email.
	 * 
	 * @param email
	 * @return
	 */
	private boolean isEmailValid(String email) {
		return email.contains("@");
	}

	/**
	 * Verifies the password is longer than 4 characters.
	 * 
	 * @param password
	 * @return
	 */
	private boolean isPasswordValid(String password) {
		return password.length() > 4;
	}

	/**
	 * Authenticates a user to allow them to login.
	 * 
	 * @param authData
	 */
	private void setAuthenticatedUser(AuthData authData) {
		if (authData != null) {
			this.authData = authData;
			openProjectListView(this.authData);
		}
	}

	/**
	 * Opens a browser activity that loads the page that allows the user to create a new account
	 * @param view
	 */
	private void registerNewUser(View view) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://henry-staging.firebaseio.com"));
		startActivity(browserIntent);

	}
	
	/**
	 * A custom AuthResultHandler 
	 * @author daveyle
	 *
	 */
	private class AuthResultHandler implements Firebase.AuthResultHandler {

		private final String provider;
		
		/**
		 * Creates a new AuthResultHandler
		 * @param provider
		 */
		public AuthResultHandler(String provider) {
			this.provider = provider;
		}
		/**
		 * On successful authentication, calls the setAuthenticatedUser method
		 */
		public void onAuthenticated(AuthData authData) {
			setAuthenticatedUser(authData);
		}

		/**
		 * On failed login, displays an error message
		 */
		public void onAuthenticationError(FirebaseError firebaseError) {
			showErrorDialog(firebaseError.toString());
			mAuthProgressDialog.hide();
		}
	}

}