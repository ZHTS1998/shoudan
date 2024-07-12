import bean.*;
import functions.*;
import util.Constrants;
import util.HTTPUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
public class Main {
    public static void main(String[] args) throws Exception {
        //TIP 当文本光标位于高亮显示的文本处时按 <shortcut actionId="ShowIntentionActions"/>
        // 查看 IntelliJ IDEA 建议如何修正。

        Mch mch  = new Mch();
        mch.setMch_id("023200010000001");
        mch.setMch_create_ip("127.0.0.1");
        Goods goods = new Goods();
        goods.setBody("THIS IS A GOOD");
        Order order = new Order();
        order.setOut_trade_no("2024050901056134511");
        order.setTotal_fee(1);
        //支付

        UnifyPay unifyPay = new UnifyPay();
        unifyPay.pay(order,mch,goods);


        //下单
      //  mch.setPrivate_key("");
      //  UnifyNative unifyNative = new UnifyNative();
     ///   unifyNative.nativePay(goods,order,mch);




        /*

        //关闭订单
        Scanner sc = new Scanner(System.in);
        //按任意键发起关闭指令
        String s =sc.nextLine();
        if(s!=null && !s.isEmpty()) {
            mch.setPrivate_key("");
            UnifyClose unifyClose = new UnifyClose();
            unifyClose.closeOrder(mch,order);
        }


        //统一撤销
        mch.setPrivate_key("");
        UnifyReverse unifyReverse = new UnifyReverse();
        unifyReverse.reverseOrder(mch,order);

        //退款
        mch.setPrivate_key("");
        UnifyRefund unifyRefund = new UnifyRefund();
        Refund refund = new Refund();
        refund.setOut_refund_no("re202405091018");
        refund.setRefund_fee(1);
        unifyRefund.refund(order,mch,refund);


         */
        //日期
        /*
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024,Calendar.JUNE,06);
        Date date=calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dateString=sdf.format(date);
        Bills bills = new Bills();
        bills.setSettleDate(dateString);
        bills.setMCHNO("023200010000001");
        bills.setPayChannel("0001");
        //对账单申请

        BillApplication billApplication = new BillApplication();
       billApplication.applyBills(mch,bills);
        //对账单下载
        BillDownload billDownload = new BillDownload();
        billDownload.downloadBills(mch,bills);



         */




    }
}
