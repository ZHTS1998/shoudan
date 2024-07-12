package functions;
import bean.Bills;
import bean.Mch;

import org.w3c.dom.Document;
import util.*;

import java.util.HashMap;
import java.util.Map;


public class BillApplication {
    public String createMessage(Mch mch,Bills bills) throws Exception{
        Map<String,String> valueMap=new HashMap<String,String>();
        if(bills==null){
            return "Bills can't be null";
        }
        if(bills.getMCHNO() == null || bills.getMCHNO().equals("")
                || bills.getSettleDate() == null|| bills.getSettleDate().equals("") ){
            return "Need Necessary information";
        }
        valueMap.put("MCHNO", bills.getMCHNO());
        valueMap.put("SETTLEDATE", bills.getSettleDate());
        if( bills.getPayChannel()!= null &&!bills.getPayChannel().equals("")){
            valueMap.put("PAYCHANNEL", bills.getPayChannel());
        }
        valueMap.put("VERSION","3.0.0");
        valueMap.put("SIGNMODE","RSA");
        XMLTools xmlTools=new XMLTools();
        String xmlString="";

        String priKey ="";

        Fileutil fileutil = new Fileutil();
        String path = "D:\\certificate";
        String target = "023200010000001";

        String pwd = fileutil.getContent(path, target, "pwd");

        priKey = Sign.getPriKey(pwd, path + "\\" + target+".key");
        mch.setPrivate_key(priKey);

        String sendMsg = XMLTools.sortByASCII(valueMap);
        String signature = Sign.signRSA(sendMsg, priKey);
        //sign
        valueMap.put("SIGN", signature);
        //这里暂时保管到mch中


        try {
            Document document = xmlTools.initXmlDocument();
            xmlString = xmlTools.mapToXmlString(document, valueMap);
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return xmlString;
    }
    public String applyBills(Mch mch, Bills bills) throws Exception{

        String xmlStringQuery = this.createMessage(mch,bills);
        HTTPUtil httpUtil = new HTTPUtil();

        String msg = httpUtil.sendMsg(xmlStringQuery,Constrants.BILL_APPLICATION_TEST_URL);

        XMLTools xmlTools = new XMLTools();
        Map<String,String> returnMap = xmlTools.xmlStringToMap(msg);
        boolean b = httpUtil.checkReturnMsg(msg,mch.getPrivate_key(),returnMap);
        if(!b){

            return "FAIL";
        }
        if(returnMap.containsKey("RESPCODE")){
            return returnMap.get("RESPCODE");
        }
        return "FAIL";

    }
}
