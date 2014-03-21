package io.itch.authentication;

import io.itch.api.ItchApiClient;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class SessionHelper {

    public static final String ACCOUNT_TYPE = "io.itch.account_type.user";
    public static final String AUTH_TOKEN_TYPE_FULL = "io.itch.session.token_type.full";

    private static SessionHelper SHARED_INSTANCE;
    private static final Object LOCK = new Object();

    private Boolean loggedIn;
    private String token;
    private AccountManagerCallback<Bundle> callback;

    public static SessionHelper getInstance() {
        if (SHARED_INSTANCE == null) {
            synchronized (LOCK) {
                if (SHARED_INSTANCE == null) {
                    SHARED_INSTANCE = new SessionHelper();
                }
            }
        }
        return SHARED_INSTANCE;
    }

    private SessionHelper() {
        this.loggedIn = false;
        this.callback = new AccountManagerCallback<Bundle>() {

            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                boolean success = false;
                try {
                    Object t = future.getResult().get(AccountManager.KEY_AUTHTOKEN);
                    if (t instanceof String) {
                        String token = (String) t;
                        if (!TextUtils.isEmpty(token)) {
                            setToken(token);
                            success = true;
                        }
                    }
                } catch (OperationCanceledException e) {
                    Log.e("Itch", "Session failure", e);
                } catch (AuthenticatorException e) {
                    Log.e("Itch", "Session failure", e);
                } catch (IOException e) {
                    Log.e("Itch", "Session failure", e);
                } finally {
                    setLoggedIn(success);
                }
            }
        };
    }

    public Boolean isLoggedIn() {
        return loggedIn;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    private void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
        ItchApiClient.setToken(token);
    }

    public void login(final Activity context) {
        AccountManager am = AccountManager.get(context);
        am.addAccount(ACCOUNT_TYPE, AUTH_TOKEN_TYPE_FULL, null, null, context, this.callback, null);
    }

    public Boolean restoreSession(final Activity context) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
        if (accounts != null && accounts.length > 0) {
            Account first = accounts[0];
            am.getAuthToken(first, AUTH_TOKEN_TYPE_FULL, null, context, this.callback, null);
        }
        return false;
    }
}
