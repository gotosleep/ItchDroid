package io.itch.authentication;

import io.itch.api.ItchApiClient;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
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
    }

    public Boolean isLoggedIn() {
        synchronized (LOCK) {
            return loggedIn;
        }
    }

    private void setLoggedIn(Boolean loggedIn) {
        synchronized (LOCK) {
            this.loggedIn = loggedIn;
        }
    }

    public String getToken() {
        synchronized (LOCK) {
            return token;
        }
    }

    private void setToken(String token) {
        synchronized (LOCK) {
            this.token = token;
        }
        ItchApiClient.setToken(token);
    }

    public void login(final Activity context, final SessionCallback callback) {
        AccountManager am = AccountManager.get(context);
        am.addAccount(ACCOUNT_TYPE, AUTH_TOKEN_TYPE_FULL, null, null, context, new AccountManagerCallback<Bundle>() {

            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                boolean success = false;
                try {
                    Object t = future.getResult().get(AccountManager.KEY_ACCOUNT_NAME);
                    if (t instanceof String) {
                        String name = (String) t;
                        if (!TextUtils.isEmpty(name)) {
                            success = true;
                        }
                    }
                } catch (Exception e) {
                    Log.e("Itch", "Login failure", e);
                } finally {
                    if (success) {
                        restoreSession(context, callback);
                    } else if (callback != null) {
                        callback.onFailed();
                    }
                }
            }
        }, null);
    }

    public void logout(final Activity context, final SessionCallback callback) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
        if (accounts != null && accounts.length > 0) {
            for (Account account : accounts) {
                am.removeAccount(account, new AccountManagerCallback<Boolean>() {

                    @Override
                    public void run(AccountManagerFuture<Boolean> future) {
                        if (callback != null) {
                            boolean success = false;
                            try {
                                success = future.getResult();
                            } catch (Exception e) {
                                success = false;
                                Log.e("Itch", "Failed to log out", e);
                            } finally {
                                if (success) {
                                    setLoggedIn(false);
                                    setToken(null);
                                    callback.onSuccess();
                                } else {
                                    callback.onFailed();
                                }
                            }
                        }
                    }
                }, null);
            }
        }
    }

    public void restoreSession(final Activity context, final SessionCallback callback) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
        if (accounts != null && accounts.length > 0) {
            Account first = accounts[0];
            am.getAuthToken(first, AUTH_TOKEN_TYPE_FULL, null, context, new AccountManagerCallback<Bundle>() {

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
                    } catch (Exception e) {
                        Log.e("Itch", "Session failure", e);
                    } finally {
                        setLoggedIn(success);
                        if (callback != null) {
                            if (success) {
                                callback.onSuccess();
                            } else {
                                callback.onFailed();
                            }
                        }
                    }
                }
            }, null);
        }
    }

    public static abstract class SessionCallback {
        public void onSuccess() {

        }

        public void onFailed() {

        }
    }
}
