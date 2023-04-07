package JavaBeans;

/**
 * Created by Friday on 2018/7/7.
 */

public class GoodsBean {

    
    private int goodsId;              //商品ID
    private String goodsImgId;            //商品图片ID
    private String goodsDescribe;    //商品描述

    private double goodsPrice;      //商品价格
    private String goodsPostage;    //商品邮费
    private String goodsPlace;      //商品地点

    private int goodsSeller;        //商家ID

    public int getGoodsSeller() {
        return goodsSeller;
    }

    public void setGoodsSeller(int goodsSeller) {
        this.goodsSeller = goodsSeller;
    }

    public String getGoodsPlace() {return goodsPlace;}

    public void setGoodsPlace(String goodsPlace) {
        this.goodsPlace = goodsPlace;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public String getGoodsPostage() {
        return goodsPostage;
    }

    public void setGoodsPostage(String goodsPostage) {
        this.goodsPostage = goodsPostage;
    }

    public String getGoodsDescribe() {return goodsDescribe;}

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsprice) {
        this.goodsPrice = goodsprice;
    }

    public void setGoodsDescribe(String goodsDescribe) {this.goodsDescribe = goodsDescribe;}

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsImgId() {return goodsImgId;}

    public void setGoodsImgId(String goodsImgId) {this.goodsImgId = goodsImgId;}
}
