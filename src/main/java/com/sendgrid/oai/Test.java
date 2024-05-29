package com.sendgrid.oai;

import com.sendgrid.oai.utils.Utility;

public class Test {
    public static void main(String[] args) {
        String path = "/v3/mail/batch/{batch_id}/value/";
        boolean res = Utility.isInstancePath(path);
        System.out.println(res);
    }
}
