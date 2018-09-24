package com.example.user.myfinalprojectssc_ver1.ModelSQLite;

/**
 * Created by Ho Dong Trieu on 09/10/2018
 */
public class RecordTopUp {

    private int idtopup;
    private String cardid;
    private String amountbefore;
    private String amountafter;
    private String datetimetopup;
    private int statustopup;

    public RecordTopUp(){

    }

    public RecordTopUp(int idtopup, String cardid, String amountbefore, String amountafter, String datetimetopup, int statustopup) {
        this.idtopup = idtopup;
        this.cardid = cardid;
        this.amountbefore = amountbefore;
        this.amountafter = amountafter;
        this.datetimetopup = datetimetopup;
        this.statustopup = statustopup;
    }

    public int getStatustopup() {
        return statustopup;
    }

    public void setStatustopup(int statustopup) {
        this.statustopup = statustopup;
    }

    public int getIdtopup() {
        return idtopup;
    }

    public void setIdtopup(int idtopup) {
        this.idtopup = idtopup;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getAmountbefore() {
        return amountbefore;
    }

    public void setAmountbefore(String amountbefore) {
        this.amountbefore = amountbefore;
    }

    public String getAmountafter() {
        return amountafter;
    }

    public void setAmountafter(String amountafter) {
        this.amountafter = amountafter;
    }

    public String getDatetimetopup() {
        return datetimetopup;
    }

    public void setDatetimetopup(String datetimetopup) {
        this.datetimetopup = datetimetopup;
    }
}
