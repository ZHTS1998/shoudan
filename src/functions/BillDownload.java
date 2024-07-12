package functions;

import bean.Bills;
import bean.Mch;
import org.w3c.dom.Document;
import util.*;

import java.util.*;

import static java.lang.System.exit;

public class BillDownload {
    public String createMessage(Mch mch, Bills bills) throws Exception {
        Map<String, String> valueMap = new HashMap<String, String>();
        if (bills == null) {
            return "Bills can't be null";
        }
        if (bills.getMCHNO() == null || bills.getMCHNO().equals("") || bills.getSettleDate() == null || bills.getSettleDate().equals("")) {
            return "Need Necessary information";
        }

        valueMap.put("MCHNO", bills.getMCHNO());
        valueMap.put("SETTLEDATE", bills.getSettleDate());
        valueMap.put("VERSION", "3.0.0");
        valueMap.put("SIGNMODE", "RSA");
        XMLTools xmlTools = new XMLTools();
        String xmlString = "";

        String priKey = "";

        Fileutil fileutil = new Fileutil();
        String path = "D:\\certificate";
        String target = "023200010000001";

        String pwd = fileutil.getContent(path, target, "pwd");

        priKey = Sign.getPriKey(pwd, path + "\\" + target + ".key");
        mch.setPrivate_key(priKey);

        String sendMsg = XMLTools.sortByASCII(valueMap);
        String signature = Sign.signRSA(sendMsg, priKey);
        String pubPath = "D:\\certificate\\publickey\\testpublickey.cer";

        //sign
        valueMap.put("SIGN", signature);
        //这里暂时保管到mch中

        try {
            Document document = xmlTools.initXmlDocument();
            xmlString = xmlTools.mapToXmlString(document, valueMap);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return xmlString;
    }

    public String downloadBills(Mch mch, Bills bills) throws Exception {

        String xmlStringQuery = this.createMessage(mch, bills);
        HTTPUtil httpUtil = new HTTPUtil();
        //Thread.sleep(10000);
        String msg = httpUtil.sendMsg(xmlStringQuery, Constrants.BILL_DOWNLOAD_TEST_URL);

        XMLTools xmlTools = new XMLTools();
        Map<String, String> returnMap = xmlTools.xmlStringToMap(msg);

        String filedata = returnMap.get("FILEDATA");
        returnMap.remove("FILEDATA");
        boolean b = httpUtil.checkReturnMsg(msg, mch.getPrivate_key(), returnMap);

        if (!b) {
            exit(0);
        }
        if (returnMap.containsKey("RESPCODE") && returnMap.get("RESPCODE").equals("AAAAAAA")) {
            String path = "D:\\bills\\" + bills.getSettleDate() + ".xls";
            Billutil.uncompress(filedata, path);
            if (Billutil.isSameMd5(filedata, returnMap.get("FILEMD5"))) {
                System.out.println("md5验证成功");
                return "SUCCESS";
            }

            return "FAIL";
        }
        return "FAIL";
    }

}
