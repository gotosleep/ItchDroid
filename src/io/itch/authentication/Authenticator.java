package io.itch.authentication;

import io.itch.activities.LoginActivity;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class Authenticator extends AbstractAccountAuthenticator {

    private final Context context;

    public Authenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType,
            String[] requiredFeatures, Bundle options)
            throws NetworkErrorException {
        final Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse arg0, Account arg1, Bundle arg2)
            throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse arg0, String arg1) {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType,
            Bundle options) throws NetworkErrorException {

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(getContext());

        String authToken = am.peekAuthToken(account, authTokenType);

        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        } else {
            // If we get here, then we couldn't access the user's password - so we
            // need to re-prompt them for their credentials. We do that by creating
            // an intent to display our AuthenticatorActivity.
            final Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, account.type);
            intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }
    }

    @Override
    public String getAuthTokenLabel(String arg0) {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse arg0, Account arg1, String[] arg2)
            throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse arg0, Account arg1, String arg2, Bundle arg3)
            throws NetworkErrorException {
        return null;
    }

    public Context getContext() {
        return context;
    }

}
