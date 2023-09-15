package net.rlbot.internal.managers;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class AccountManager {

    public static boolean isAccountAvailable() {
        return username != null;
    }

    public static void setAccount(@NonNull String username, @NonNull String password) {
        AccountManager.username = username;
        AccountManager.password = password;
        AccountManager.accountStatus = AccountStatus.UNKNOWN;
    }

    public static void removeAccount() {
        AccountManager.username = null;
        AccountManager.password = null;
    }

    @Getter
    private static String username;

    @Getter
    private static String password;

    @Getter
    @Setter
    private static AccountStatus accountStatus;
}
