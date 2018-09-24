package com.example.user.myfinalprojectssc_ver1.MifareDesfire;

public class CardData {
    private String cardID, cardExDate;
    private boolean isActive, isPurseEnable, isTopupEnable, isAutoLoadEnable, isBookingEnable;
    private String cardStatus;

    public CardData() {
        cardID = "00";
        cardExDate = "0000";
        isTopupEnable = false;
        isPurseEnable = false;
        isActive = false;
        isAutoLoadEnable = false;
        isBookingEnable = false;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getCardExDate() {
        return cardExDate;
    }

    public void setCardExDate(String cardExDate) {
        this.cardExDate = cardExDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPurseEnable() {
        return isPurseEnable;
    }

    public void setPurseEnable(boolean purseEnable) {
        isPurseEnable = purseEnable;
    }

    public boolean isTopupEnable() {
        return isTopupEnable;
    }

    public void setTopupEnable(boolean topupEnable) {
        isTopupEnable = topupEnable;
    }

    public boolean isAutoLoadEnable() {
        return isAutoLoadEnable;
    }

    public void setAutoLoadEnable(boolean autoLoadEnable) {
        isAutoLoadEnable = autoLoadEnable;
    }

    public boolean isBookingEnable() {
        return isBookingEnable;
    }

    public void setBookingEnable(boolean bookingEnable) {
        isBookingEnable = bookingEnable;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }
}
