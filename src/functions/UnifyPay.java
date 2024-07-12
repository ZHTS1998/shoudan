package functions;

import bean.*;
import org.w3c.dom.Document;
import util.*;

import java.util.HashMap;
import java.util.Map;


import static java.lang.System.exit;

public class UnifyPay {
    public String createMessage(Mch mch, Goods goods,Order order ) throws Exception{

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

        valueMap.put("service", Constrants.UNIFYPAY_SERVICE);
        valueMap.put("mch_id", mch.getMch_id());
        valueMap.put("out_trade_no", order.getOut_trade_no());
        valueMap.put("body", goods.getBody());
        valueMap.put("total_fee",order.getTotal_fee()+"");
        valueMap.put("mch_create_ip",mch.getMch_create_ip());

        String auth_code = "134187103979284094";
        //auth_code
        valueMap.put("auth_code",auth_code);
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
        try {
            Document document = xmlTools.initXmlDocument();
            xmlString = xmlTools.mapToXmlString(document, valueMap);
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return xmlString;
    }
    public String pay(Order order,Mch mch,Goods goods) throws Exception{
        HTTPUtil httpUtil = new HTTPUtil();
        String xmlString = this.createMessage(mch,goods,order);
        String m = httpUtil.sendMsg(xmlString,Constrants.TEST_URL);

        if(!httpUtil.checkReturnMsg(m,mch.getPrivate_key(),null)){
            //验签失败
            exit(0);
        }
        UnifyPayQuery unifyPayQuery = new UnifyPayQuery();
        unifyPayQuery.payQuery(mch,order);
        return order.getStatus();
    }
}
