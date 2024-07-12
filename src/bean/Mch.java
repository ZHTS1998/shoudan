package bean;
import java.util.*;

public class Mch {
    private String mch_id;
    private String mch_create_ip;
    private String merchant_name;
    private String attach;
    private String device_info;
    private List<String> goods_id_list;
    private String private_key;
    private String gm_private_key;
    private String MCHNO;

    public String getMCHNO() {
        return MCHNO;
    }

    public void setMCHNO(String MCHNO) {
        this.MCHNO = MCHNO;
    }

    public String getGm_private_key() {
        return gm_private_key;
    }

    public void setGm_private_key(String gm_private_key) {
        this.gm_private_key = gm_private_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getMch_create_ip() {
        return mch_create_ip;
    }

    public void setMch_create_ip(String mch_create_ip) {
        this.mch_create_ip = mch_create_ip;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public List<String> getGoods_id_list() {
        return goods_id_list;
    }

    public void setGoods_id_list(List<String> goods_id_list) {
        this.goods_id_list = goods_id_list;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }
}
