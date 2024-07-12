package bean;

public class Goods {
    private String goods_id;
    private String body;

    private String mch_id;
    private String goods_detail;
    private String goods_tag;
    private String goods_name;
    private int quantity;
    private int price;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }

    public String getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(String goods_detail) {
        this.goods_detail = goods_detail;
    }
}
