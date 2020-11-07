package com.hxw.wanandroid;

import com.hxw.core.utils.HexUtils;

import java.text.DecimalFormat;

/**
 * @author hxw
 * @date 2019/12/21
 */
public class Mortgage {

    public static void main(String[] args) {

        DecimalFormat format = new DecimalFormat("#.00");
        //贷款
        double loan = 2000000;
        //银行利率
        double rate = 0.0325 / 12;
        //理财利率
        double lrate=0.08/12;
        //期限
        int time = 12 * 30;
        double earn1=0;
        double earn2=0;
        //等额本息计算
        double pow = Math.pow(1 + rate, time);
        double refund1 = Double.parseDouble(format.format(loan * rate * pow / (pow - 1)));
        //等额本金计算
        double bMonth = loan / time;
        double bReturn ;
        for (int i = 0; i < time; i++) {
            bReturn = bMonth * i;
            double refund2 = Double.parseDouble(format.format(bMonth + (loan - bReturn) * rate));

            if (refund1<refund2){
                earn1=(refund2-refund1+earn1)*(1+lrate);
                earn2=earn2*(1+lrate);
            }else {
                earn1=earn1*(1+lrate);
                earn2=(refund1-refund2+earn2)*(1+lrate);
            }
            System.out.println("本息月还->" + refund1+"  本金"+(i+1)+"月还->" + refund2+"  本息收益->"+earn1+"  本金收益->"+earn2);
        }
    }
}
