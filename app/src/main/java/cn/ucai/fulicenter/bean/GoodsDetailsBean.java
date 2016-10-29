package cn.ucai.fulicenter.bean;

import java.util.Arrays;
import java.util.List;

/**GoodsDetailsBean类
 * Created by Administrator on 2016/10/13 0013.
 */
public class GoodsDetailsBean {

    /**
     * id : 280
     * goodsId : 7677
     * catId : 291
     * goodsName : 双层分格饭盒 绿色
     * goodsEnglishName : Monbento
     * goodsBrief : PP食品级材质，轻巧、易清洗、蠕变性小，不易变形，可置于微波炉加热，可方巾洗碗机清洗。双层色彩可以随意组合，轻巧方便。
     * shopPrice : ￥253
     * currencyPrice : ￥293
     * promotePrice : ￥0
     * rankPrice : ￥293
     * isPromote : false
     * goodsThumb : 201509/thumb_img/7677_thumb_G_1442391216339.png
     * goodsImg : 201509/thumb_img/7677_thumb_G_1442391216339.png
     * addTime : 1442419200000
     * shareUrl : http://m.fulishe.com/item/7677
     * properties : [{"id":9529,"goodsId":0,"colorId":7,"colorName":"白色","colorCode":"#ffffff","colorImg":"","colorUrl":"https://detail.tmall.com/item.htm?spm=a1z10.5-b.w4011-3609973698.66.6PtkVY&id=520971761592&rn=5ddf7aff64dbe1a24da0eaf7409e3389&abbucket=15&skuId=3104519239252","albums":[{"pid":7677,"imgId":28296,"imgUrl":"201509/goods_img/7677_P_1442391216432.png","thumbUrl":"no_picture.gif"},{"pid":7677,"imgId":28297,"imgUrl":"201509/goods_img/7677_P_1442391216215.png","thumbUrl":"no_picture.gif"},{"pid":7677,"imgId":28298,"imgUrl":"201509/goods_img/7677_P_1442391216692.png","thumbUrl":"no_picture.gif"},{"pid":7677,"imgId":28299,"imgUrl":"201509/goods_img/7677_P_1442391216316.png","thumbUrl":"no_picture.gif"}]}]
     * promote : false
     */

    private int id;

    private int goodsId;

    private int catId;

    private String goodsName;

    private String goodsEnglishName;

    private String goodsBrief;

    private String shopPrice;

    private String currencyPrice;

    private String promotePrice;

    private String rankPrice;

    private boolean isPromote;

    private String goodsThumb;

    private String goodsImg;

    private long addTime;

    private String shareUrl;

    private PropertiesBean[] properties;


    public GoodsDetailsBean() {
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setGoodsId(int goodsId){
        this.goodsId = goodsId;
    }
    public int getGoodsId(){
        return this.goodsId;
    }
    public void setCatId(int catId){
        this.catId = catId;
    }
    public int getCatId(){
        return this.catId;
    }
    public void setGoodsName(String goodsName){
        this.goodsName = goodsName;
    }
    public String getGoodsName(){
        return this.goodsName;
    }
    public void setGoodsEnglishName(String goodsEnglishName){
        this.goodsEnglishName = goodsEnglishName;
    }
    public String getGoodsEnglishName(){
        return this.goodsEnglishName;
    }
    public void setGoodsBrief(String goodsBrief){
        this.goodsBrief = goodsBrief;
    }
    public String getGoodsBrief(){
        return this.goodsBrief;
    }
    public void setShopPrice(String shopPrice){
        this.shopPrice = shopPrice;
    }
    public String getShopPrice(){
        return this.shopPrice;
    }
    public void setCurrencyPrice(String currencyPrice){
        this.currencyPrice = currencyPrice;
    }
    public String getCurrencyPrice(){
        return this.currencyPrice;
    }
    public void setPromotePrice(String promotePrice){
        this.promotePrice = promotePrice;
    }
    public String getPromotePrice(){
        return this.promotePrice;
    }
    public void setRankPrice(String rankPrice){
        this.rankPrice = rankPrice;
    }
    public String getRankPrice(){
        return this.rankPrice;
    }
    public void setIsPromote(boolean isPromote){
        this.isPromote = isPromote;
    }
    public boolean getIsPromote(){
        return this.isPromote;
    }
    public void setGoodsThumb(String goodsThumb){
        this.goodsThumb = goodsThumb;
    }
    public String getGoodsThumb(){
        return this.goodsThumb;
    }
    public void setGoodsImg(String goodsImg){
        this.goodsImg = goodsImg;
    }
    public String getGoodsImg(){
        return this.goodsImg;
    }
    public void setAddTime(long addTime){
        this.addTime = addTime;
    }
    public long getAddTime(){
        return this.addTime;
    }
    public void setShareUrl(String shareUrl){
        this.shareUrl = shareUrl;
    }
    public String getShareUrl(){
        return this.shareUrl;
    }
    public void setProperties(PropertiesBean[] properties){
        this.properties = properties;
    }
    public PropertiesBean[] getProperties(){
        return this.properties;
    }

    @Override
    public String toString() {
        return "GoodsDetailsBean{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", catId=" + catId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsEnglishName='" + goodsEnglishName + '\'' +
                ", goodsBrief='" + goodsBrief + '\'' +
                ", shopPrice='" + shopPrice + '\'' +
                ", currencyPrice='" + currencyPrice + '\'' +
                ", promotePrice='" + promotePrice + '\'' +
                ", rankPrice='" + rankPrice + '\'' +
                ", isPromote=" + isPromote +
                ", goodsThumb='" + goodsThumb + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", addTime=" + addTime +
                ", shareUrl='" + shareUrl + '\'' +
                ", properties=" + Arrays.toString(properties) +
                '}';
    }
}
