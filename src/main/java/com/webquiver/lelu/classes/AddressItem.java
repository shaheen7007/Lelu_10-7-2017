package com.webquiver.lelu.classes;

/**
 * Created by WebQuiver 04 on 7/21/2017.
 */

public class AddressItem
{

    private String NAME;
    private String ADDRESS1;
    private String PLACE;
    private String DISTRICT;
    private String PINCODE;
    private String STATE;
    private String PHONE;
    private int ID;


    public AddressItem(String NAME, String ADDRESS1, String PLACE, String DISTRICT, String PINCODE, String STATE,String PHONE,Integer ID) {
    this.ADDRESS1 = ADDRESS1;
    this.NAME = NAME;
    this.PLACE = PLACE;
    this.DISTRICT = DISTRICT;
    this.PINCODE = PINCODE;
    this.STATE = STATE;
        this.PHONE=PHONE;
        this.ID=ID;
   }
    public AddressItem() {

    }



    public String getADDRESS1() {
        return ADDRESS1;
    }
    public void setADDRESS1(String ADDRESS1) {

        this.ADDRESS1 =ADDRESS1;

    }
    public String getNAME() {
        return NAME;
    }
    public void setNAME(String NAME) {
        this.NAME = NAME;

    }

    public String getDISTRICT() {
        return DISTRICT;
    }
    public void setDISTRICT(String DISTRICT) {
        this.DISTRICT = DISTRICT;
    }

    public String getPINCODE() {
        return PINCODE;
    }
    public void setPINCODE(String PINCODE) {
        this.PINCODE = PINCODE;
    }

    public String getPHONE() {
        return PHONE;
    }
    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }


    public String getSTATE() {
        return STATE;
    }
    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }


    public String getPLACE() {
        return PLACE;
    }
    public void setPLACE(String PLACE) {

        this.PLACE = PLACE;

    }

    public int getID() {

        return ID;

    }
    public void setID(Integer ID) {
        this.ID = ID;
    }

}