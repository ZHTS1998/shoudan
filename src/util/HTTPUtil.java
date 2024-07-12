package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class HTTPUtil {
    public String sendMsg(String xmlString,String targetUrl) throws Exception{
        String line = "";
        try {
            URL url = new URL(targetUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setRequestProperty("Accept", "application/xml");
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(xmlString);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            StringBuilder sb = new StringBuilder();

            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"GBK"));
                for(line = br.readLine();line!=null;line = br.readLine()){
                    sb.append(line);
                }
                inputStream.close();
            }
            System.out.println(sb.toString());
            return sb.toString();
        }catch(Exception e) {

        }
        return line;
    }

    public static boolean checkReturnMsg(String xmlString, String privateKey,Map<String, String> returnMap) throws Exception {

       if((xmlString== null||xmlString.equals("")) && returnMap == null){
           System.out.println("输入格式有误");
           return false;
       }

        XMLTools xmlTools = new XMLTools();
        if(returnMap == null){
            returnMap= xmlTools.xmlStringToMap(xmlString);
        }
        if(returnMap.containsKey("return_code")){
            if(!returnMap.get("return_code").equals("SUCCESS")) {
                System.out.println("通信失败");
                return false;
            }
        }
        String sign ="";
        if(returnMap.containsKey("sign")) {
            sign = returnMap.get("sign");
            returnMap.remove("sign");
        }else if(returnMap.containsKey("SIGN")){
            sign = returnMap.get("SIGN");
            returnMap.remove("SIGN");
        }else{
            System.out.println("No signature returned");
            return false;
        }
        String str = xmlTools.sortByASCII(returnMap);
        //System.out.println(str);
        String path = "D:\\certificate\\publickey\\testpublickey.cer";
        String publicKey = Sign.getPubCer(path);
        System.out.println("publickey:"+publicKey);
        if(!Sign.verifySignRSA(str,sign,publicKey)){
            //如果验证失败
            System.out.println("返回签名验证失败");
            return false;
        }
        System.out.println("返回签名验证成功");
        return true;
    }

}
