package functions;

import bean.Goods;
import bean.Mch;
import bean.Order;
import org.w3c.dom.Document;
import util.*;

import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.exit;

public class UnifyNative {
    public String createMessage(Mch mch, Goods goods, Order order ) throws Exception{
        if(goods == null || mch == null || order == null){
            return "Need necessary parameters";
        }
        Map<String,String> valueMap=new HashMap<String,String>();
        if(mch.getMch_id()==null || mch.getMch_id().equals("")
                || order.getOut_trade_no()==null || order.getOut_trade_no().equals("")
        ||goods.getBody()==null || goods.getBody().equals("")||
        mch.getMch_create_ip() == null || mch.getMch_create_ip().equals("")){
            return "Need necessary parameters";
        }
        valueMap.put("service", Constrants.UNIFYORDER_SERVICE);
        valueMap.put("mch_id", mch.getMch_id());
        valueMap.put("out_trade_no", order.getOut_trade_no());
        valueMap.put("body", goods.getBody());
        valueMap.put("total_fee",order.getTotal_fee()+"");
        valueMap.put("mch_create_ip",mch.getMch_create_ip());

        String notify_url = "www.baidu.com";
        valueMap.put("notify_url", notify_url);
        valueMap.put("sign_mode","RSA");

        XMLTools xmlTools=new XMLTools();
        String xmlString="";


        String priKey ="";
        if(mch.getPrivate_key() == null || mch.getPrivate_key().equals("")){
            Fileutil fileutil = new Fileutil();
            String path = "D:\\certificate";
            String target = "023200010000001";

            String pwd = fileutil.getContent(path, target, "pwd");

            priKey = Sign.getPriKey(pwd, path + "\\" + target + ".key");
            mch.setPrivate_key(priKey);
        }else{
            priKey = mch.getPrivate_key();
        }
        String sendMsg = XMLTools.sortByASCII(valueMap);
        String signature = Sign.signRSA(sendMsg, priKey);
        //sign
        valueMap.put("sign", signature);
        //这里暂时保管到mch中
        mch.setPrivate_key(signature);

        try {
            Document document = xmlTools.initXmlDocument();
            xmlString = xmlTools.mapToXmlString(document, valueMap);
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return xmlString;
    }
    public String nativePay(Goods goods,Order order,Mch mch) throws Exception{
        HTTPUtil httpUtil = new HTTPUtil();
        String xmlString = this.createMessage(mch,goods,order);
        String m = httpUtil.sendMsg(xmlString,Constrants.TEST_URL);
        XMLTools xmlTools = new XMLTools();
        if(!httpUtil.checkReturnMsg(m,mch.getPrivate_key(),null)){
            //验签失败
            exit(0);
        }
        Map<String,String> rmap= xmlTools.xmlStringToMap(m);
        //根据返回的url生成二维码
        String code_url= rmap.get("code_url");
        System.out.println(code_url);
        if(code_url == null || code_url.equals("")){
            return "";
        }
        String key = order.getOut_trade_no();
        QRCode qrCode=new QRCode();
        qrCode.generateQRCode(code_url,300,300,"D:\\qrcode\\"+key+".png");
        //等待统一订单通知
        return order.getStatus();
    }

}
