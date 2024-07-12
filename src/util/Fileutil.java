package util;

import java.io.*;


public class Fileutil {
    public String getContent(String path,String target,String extend){
        File file = new File(path+"/"+target+"."+extend);
        if(!file.exists()){
            System.out.println("File "+path+" is not exist");
            return "";
        }
        String res = "";
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){

        String line ="";
        while((line= reader.readLine())!=null){
            res+=line;
        }

        }catch (IOException e){
            System.out.println("Reading file "+path+" error");
        }
        return res;
    }

}
