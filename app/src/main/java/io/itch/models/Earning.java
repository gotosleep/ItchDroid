package io.itch.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Earning implements Parcelable {

    private String currency;
    private String amountFormatted;
    private Long amount;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmountFormatted() {
        return amountFormatted;
    }

    public void setAmountFormatted(String amountFormatted) {
        this.amountFormatted = amountFormatted;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getCurrency());
        dest.writeString(getAmountFormatted());
        dest.writeLong(getAmount());
    }

    public static final Parcelable.Creator<Earning> CREATOR = new Parcelable.Creator<Earning>() {

        @Override
        public Earning createFromParcel(Parcel source) {
            Earning result = new Earning();
            result.setCurrency(source.readString());
            result.setAmountFormatted(source.readString());
            result.setAmount(source.readLong());
            return result;
        }

        @Override
        public Earning[] newArray(int size) {
            return new Earning[size];
        }
    };

}
