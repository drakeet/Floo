/*
 * Copyright 2017 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.drakeet.floo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author drakeet
 */
public class TargetMapParser {

    private static final String TARGET_URL = "url";


    @NonNull
    public Map<String, Target> fromJson(@Nullable String json) throws JSONException {
        return toTargetMap(toJsonObject(json));
    }


    /**
     * Convert a target map to JSON string.
     *
     * @param targetMap The target map.
     * @return A JSON string of the map.
     */
    @NonNull
    public String toJson(@NonNull Map<String, Target> targetMap) {
        final Map<String, JSONObject> flatMap = new HashMap<>();
        for (Map.Entry<String, Target> entry : targetMap.entrySet()) {
            try {
                flatMap.put(entry.getKey(), toJsonObject(entry.getValue()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            // 2 indent spaces for pretty printing
            return new JSONObject(flatMap).toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return "{}";
        }
    }


    @NonNull
    private JSONObject toJsonObject(@NonNull Target target) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt(TARGET_URL, target.getUrl());
        return jsonObject;
    }


    @NonNull
    private Map<String, Target> toTargetMap(@NonNull final JSONObject json) throws JSONException {
        final Map<String, Target> result = new HashMap<>();

        final Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Target target = getTarget(json.getJSONObject(key));
            result.put(key, target);
        }
        return result;
    }


    @NonNull
    private Target getTarget(@NonNull JSONObject targetJson) {
        String targetUrl = targetJson.optString(TARGET_URL, null);
        if (targetUrl == null) {
            throw new InvalidJsonException(targetJson.toString(),
                new NullPointerException(TARGET_URL + " == null"));
        }
        return new Target(targetUrl);
    }


    @NonNull
    private JSONObject toJsonObject(@Nullable String json) throws InvalidJsonException {
        if (json == null) {
            return new JSONObject();
        }
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            throw new InvalidJsonException(json, e);
        }
    }
}
