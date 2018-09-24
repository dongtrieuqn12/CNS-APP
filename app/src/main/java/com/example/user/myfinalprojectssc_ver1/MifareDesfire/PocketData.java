package com.example.user.myfinalprojectssc_ver1.MifareDesfire;

public class PocketData {
    private String pocketID, pocketStatus;
    private long pocketBalance;
    private String transactionCode, transactionCounter, transactionDateTime;
    private long beforeBalance, usedAmount, afterBalance;
    private String samID;

    public PocketData() {
    }

    public String getPocketID() {
        return pocketID;
    }

    public void setPocketID(String pocketID) {
        this.pocketID = pocketID;
    }

    public String getPocketStatus() {
        return pocketStatus;
    }

    public void setPocketStatus(String pocketStatus) {
        this.pocketStatus = pocketStatus;
    }

    public long getPocketBalance() {
        return pocketBalance;
    }

    public void setPocketBalance(long pocketBalance) {
        this.pocketBalance = pocketBalance;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionCounter() {
        return transactionCounter;
    }

    public void setTransactionCounter(String transactionCounter) {
        this.transactionCounter = transactionCounter;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public long getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(long beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public long getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(long usedAmount) {
        this.usedAmount = usedAmount;
    }

    public long getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(long afterBalance) {
        this.afterBalance = afterBalance;
    }

    public String getSamID() {
        return samID;
    }

    public void setSamID(String samID) {
        this.samID = samID;
    }
}
