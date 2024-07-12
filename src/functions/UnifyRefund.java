package functions;

import bean.Goods;
import bean.Mch;
import bean.Order;
import bean.Refund;
import org.w3c.dom.Document;
import util.*;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;

public class UnifyRefund {
    public String createMessage(Mch mch,Order order, Refund refund) throws Exception{
        if(mch == null || order == null || refund == null){
            return "Need necessary parameters";
        }

        Map<String,String> valueMap=new HashMap<String,String>();
        if(mch.getMch_id()==null || mch.getMch_id().equals("")
                || order.getOut_trade_no()==null || order.getOut_trade_no().equals("")||
        refund.getOut_refund_no() == null || refund.getOut_refund_no().equals("")){
            return "Need necessary parameters";
        }
        valueMap.put("service", Constrants.UNIFYREFUND_SERVICE);
        valueMap.put("mch_id", mch.getMch_id());
        valueMap.put("out_trade_no", order.getOut_trade_no());
        valueMap.put("out_refund_no", refund.getOut_refund_no());
        valueMap.put("total_fee",order.getTotal_fee()+"");
        valueMap.put("refund_fee",refund.getRefund_fee()+"");

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
    public String refund(Order order,Mch mch,Refund refund) throws Exception{
        HTTPUtil httpUtil = new HTTPUtil();
        String xmlString = this.createMessage(mch,order,refund);
        String m = httpUtil.sendMsg(xmlString,Constrants.TEST_URL);

        if(!httpUtil.checkReturnMsg(m,mch.getPrivate_key(),null)){
            //验签失败
            exit(0);
        }
        UnifyRefundQuery unifyRefundQuery = new UnifyRefundQuery();
        unifyRefundQuery.refundQuery(mch,order,refund);
        return order.getStatus();
    }
}
