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

package me.drakeet.floo.sample.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author drakeet
 */
public class Mail implements Parcelable {

  public String content;
  public String from;
  public String to;

  public Mail() {}

  private Mail(Parcel in) {
    this.content = in.readString();
    this.from = in.readString();
    this.to = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.content);
    dest.writeString(this.from);
    dest.writeString(this.to);
  }

  @Override
  public int describeContents() { return 0; }

  public static final Parcelable.Creator<Mail> CREATOR = new Parcelable.Creator<Mail>() {
    @Override
    public Mail createFromParcel(Parcel source) {
      return new Mail(source);
    }

    @Override
    public Mail[] newArray(int size) {
      return new Mail[size];
    }
  };

  @Override
  public String toString() {
    return "Mail { " +
        "name='" + content + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        " }";
  }
}
