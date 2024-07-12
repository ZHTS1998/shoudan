package functions;

import bean.Mch;
import bean.Order;
import bean.Refund;
import org.w3c.dom.Document;
import util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.exit;

public class UnifyRefundQuery {
    public String createMessage(Mch mch, Order order, Refund refund) throws Exception{
        if(mch == null || order == null || refund == null){
            return "Need necessary parameters";
        }
        Map<String,String> valueMap=new HashMap<String,String>();
        if(mch.getMch_id()==null || mch.getMch_id().equals("")
                || order.getOut_trade_no()==null || order.getOut_trade_no().equals("")||
                refund.getOut_refund_no() == null || refund.getOut_refund_no().equals("")){
            return "Need necessary parameters";
        }
        valueMap.put("service", Constrants.UNIFYREFUNDQUERY_SERVICE);
        valueMap.put("mch_id", mch.getMch_id());
        valueMap.put("out_trade_no", order.getOut_trade_no());
        valueMap.put("out_refund_no", refund.getOut_refund_no());

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
    public String refundQuery(Mch mch, Order order,Refund refund) throws Exception{
        String xmlStringQuery = this.createMessage(mch, order,refund);
        HTTPUtil httpUtil = new HTTPUtil();
        ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();
        map.put("times",0);

        while(true) {
            String msg = httpUtil.sendMsg(xmlStringQuery,Constrants.TEST_URL);

            XMLTools xmlTools = new XMLTools();
            Map<String,String> returnMap = xmlTools.xmlStringToMap(msg);
            boolean b = httpUtil.checkReturnMsg(msg,mch.getPrivate_key(),returnMap);
            if(!b){

                exit(0);
            }
            if(returnMap.containsKey("err_code")){

                return "refund_query_error"+":"+"err_code:"+returnMap.get("err_code");
            }
            if(returnMap.isEmpty() || (!returnMap.containsKey("refund_status"))){
                Thread.sleep(1000);
                map.put("times",map.get("times")+1);
                if(map.get("times") >= 1000) {
                    break;
                }
                continue;
            }
            String state = returnMap.get("refund_status");
            order.setStatus(state);
            System.out.println("state:"+state);
            if(!state.equals("PROCESSING")) {

                break;
            }else{
                Thread.sleep(1000);
                map.put("times",map.get("times")+1);
                if(map.get("times") >= 1000) {
                    break;
                }
            }
        }
        return order.getOut_trade_no();
    }
}
