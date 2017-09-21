package me.drakeet.floo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author drakeet
 */
public interface IntentHandler {

    /**
     * Called immediately after intent has created on {@link Floo#start()}.
     *
     * @param context The context.
     * @param intent The intent.
     */
    void onIntentCreated(@NonNull Context context, @NonNull Intent intent);
}
