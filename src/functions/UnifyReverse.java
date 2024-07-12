package functions;

import bean.Mch;
import bean.Order;
import org.w3c.dom.Document;
import util.*;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;

public class UnifyReverse {
    public String createMessage(Mch mch, Order order) throws Exception{
        if(mch == null || order == null ){
            return "Need necessary parameters";
        }
        Map<String,String> valueMap=new HashMap<String,String>();
        if(mch.getMch_id()==null || mch.getMch_id().equals("")
                || order.getOut_trade_no()==null || order.getOut_trade_no().equals("")){
            return "Need necessary parameters";
        }
        valueMap.put("service", Constrants.UNIFYREVERSE_SERVICE);
        valueMap.put("mch_id", mch.getMch_id());
        valueMap.put("out_trade_no", order.getOut_trade_no());

        valueMap.put("sign_mode","RSA");
        XMLTools xmlTools=new XMLTools();
        String xmlString="";

        String priKey ="";

            Fileutil fileutil = new Fileutil();
            String path = "D:\\certificate";
            String target = "023200010000001";

            String pwd = fileutil.getContent(path, target, "pwd");

            priKey = Sign.getPriKey(pwd, path + "\\" + target + ".key");
            mch.setPrivate_key(priKey);


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
    public String reverseOrder(Mch mch,Order order) throws Exception{
        HTTPUtil httpUtil = new HTTPUtil();
        String xmlString = this.createMessage(mch,order);
        String m = httpUtil.sendMsg(xmlString,Constrants.TEST_URL);
        XMLTools xmlTools = new XMLTools();
        if(!httpUtil.checkReturnMsg(m,mch.getPrivate_key(),null)){
            //验签失败
            exit(0);
        }
        Map<String,String> rmap= xmlTools.xmlStringToMap(m);
        return rmap.get("result_code");
    }
}
