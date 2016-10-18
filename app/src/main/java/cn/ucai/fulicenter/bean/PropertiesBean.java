package cn.ucai.fulicenter.bean;

import java.util.List;
import cn.ucai.fulicenter.bean.AlbumsBean;
/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class PropertiesBean {

    /**
     * id : 8514
     * goodsId : 0
     * colorId : 4
     * colorName : 绿色
     * colorCode : #59d85c
     * colorImg : 201309/1380064997570506166.jpg
     * colorUrl : https://cn.shopbop.com/alexa-chung-loretta-romper-ag/vp/v=1/1573999972.htm?fm=search-shopbysize&os=false
     * albums : [{"pid":6936,"imgId":26104,"imgUrl":"201508/goods_img/6936_P_1439535131675.png","thumbUrl":"no_picture.gif"}]
     */

    private int id;

    private int goodsId;

    private int colorId;

    private String colorName;

    private String colorCode;

    private String colorImg;

    private String colorUrl;

    private AlbumsBean[] albums ;

    public PropertiesBean() {
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
    public void setColorId(int colorId){
        this.colorId = colorId;
    }
    public int getColorId(){
        return this.colorId;
    }
    public void setColorName(String colorName){
        this.colorName = colorName;
    }
    public String getColorName(){
        return this.colorName;
    }
    public void setColorCode(String colorCode){
        this.colorCode = colorCode;
    }
    public String getColorCode(){
        return this.colorCode;
    }
    public void setColorImg(String colorImg){
        this.colorImg = colorImg;
    }
    public String getColorImg(){
        return this.colorImg;
    }
    public void setColorUrl(String colorUrl){
        this.colorUrl = colorUrl;
    }
    public String getColorUrl(){
        return this.colorUrl;
    }
    public void setAlbums(AlbumsBean[] albums){
        this.albums = albums;
    }
    public AlbumsBean[] getAlbums(){
        return this.albums;
    }

    @Override
    public String toString() {
        return "PropertiesBean{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", colorId=" + colorId +
                ", colorName='" + colorName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorImg='" + colorImg + '\'' +
                ", colorUrl='" + colorUrl + '\'' +
                ", albumsBean=" + albums +
                '}';
    }
}
