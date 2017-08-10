package com.webquiver.lelu.classes;

/**
 * Created by WebQuiver 04 on 7/21/2017.
 */

public class ODRItem
{

    private String ODR_ID;
    private String STATUS;
    private String FEEDBACK;
    private float RATING;
    private String DATE;
    private String NAME;

    private int NumberOfProducts;



    public ODRItem(String ODR_ID, String NAME, String STATUS, String DATE) {

    this.ODR_ID = ODR_ID;
    this.STATUS = STATUS;
    this.DATE = DATE;
    this.NAME = NAME;

   }
    public ODRItem() {

    }

    public String getODR_ID() {
        return ODR_ID;
    }
    public void setODR_ID(String ODR_ID) {
        this.ODR_ID = ODR_ID;
    }

    public String getSTATUS() {
        return STATUS;
    }
    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getFEEDBACK() {
        return FEEDBACK;
    }
    public void setFEEDBACK(String FEEDBACK) {
        this.FEEDBACK = FEEDBACK;
    }

    public float getRATING() {

        return RATING;

    }
    public void setRATING(float RATING) {

        this.RATING= RATING;

    }


    public String getDATE() {
        return DATE;
    }
    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getNAME() {
        return NAME;
    }
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }


    public int getNumberOfProducts() {
        return NumberOfProducts;
    }
    public void setNumberOfProducts(int NumberOfProducts) {
        this.NumberOfProducts = NumberOfProducts;
    }


}