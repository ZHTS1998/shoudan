package bean;
import java.util.*;
public class Order {
    private String out_trade_no;
    private String mch_id;
    private String time_start;
    private String time_expire;
    private int total_fee;
    private List<String> goods_id_list;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public List<String> getGoods_id_list() {
        return goods_id_list;
    }

    public void setGoods_id_list(List<String> goods_id_list) {
        this.goods_id_list = goods_id_list;
    }
}
