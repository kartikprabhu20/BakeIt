package com.bakingapp.kprabhu.bakeit.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by kprabhu on 9/17/17.
 */

public class Ingredient implements Parcelable{

    public String quantity;
    public String measure;
    public String ingredient;

    protected Ingredient(Parcel in) {
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readString();
        }
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(quantity).append(measure).append(ingredient).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Ingredient)) {
            return false;
        }
        Ingredient rhs = ((Ingredient) other);
        return new EqualsBuilder().append(quantity, rhs.quantity).append(measure, rhs.measure).append(ingredient, rhs.ingredient).isEquals();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(quantity);
        }
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
}
