package me.drakeet.floo;

import android.support.annotation.NonNull;

/**
 * @author drakeet
 */
public final class Preconditions {

    @NonNull
    @SuppressWarnings("ConstantConditions")
    public static <T> T checkNotNull(@NonNull final T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }


    private Preconditions() {}
}
