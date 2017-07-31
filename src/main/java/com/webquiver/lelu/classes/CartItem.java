package com.webquiver.lelu.classes;

/**
 * Created by WebQuiver 04 on 7/21/2017.
 */

public class CartItem
{

    private int QUANTITY;
    private String NAME;
    private String PRODUCT_ID;
    private String IMAGE_URL;
    private String COLOR;
    private String PRICE;                     //offer price(amount to be paid)
    private String REALPRICE;                 //original price (price without offer)


    public CartItem(int QUANTITY, String NAME, String PRODUCT_ID,String IMAGE_URL,String COLOR,String PRICE,String REALPRICE) {
    this.QUANTITY = QUANTITY;
    this.NAME = NAME;
    this.PRODUCT_ID = PRODUCT_ID;
    this.IMAGE_URL = IMAGE_URL;
    this.COLOR = COLOR;
    this.PRICE = PRICE;
        this.REALPRICE=REALPRICE;
   }
    public CartItem() {

    }



    public int getQUANTITY() {
        return QUANTITY;
    }
    public void setQUANTITY(int QUANTITY) {

        this.QUANTITY = QUANTITY;

    }
    public String getNAME() {
        return NAME;
    }
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getREALPRICE() {
        return REALPRICE;
    }
    public void setREALPRICE(String REALPRICE) {
        this.REALPRICE = REALPRICE;
    }

    public String getCOLOR() {
        return COLOR;
    }
    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }
    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }
    public void setPRODUCT_ID(String PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public String getPRICE() {
        return PRICE;
    }
    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }


}