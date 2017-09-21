package me.drakeet.floo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author drakeet
 */
public class DefaultIntentHandler implements IntentHandler {

    @Override
    public void onIntentCreated(@NonNull Context context, @NonNull Intent intent) {
        context.startActivity(intent);
    }
}
