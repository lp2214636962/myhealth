package com.itheima.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Bcrypt {

    @Test
    public void testBcrypt(){

        //
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        //加密
       //String s = encoder.encode("abc"); //  $2a$10$5hsnyq6yRV/5rmm5nFfddOyod3hzrEiRG8HIVWGQCsKKM9PFsjPGm
       //System.out.println(s);
       //System.out.println("-------------------------------------");
       //String s1 = encoder.encode("abc"); //  $2a$10$UPXlWPQV1vPCVkCQaCjs8ewLVlqelFGs/g11t8cWb0/yYF2SOByMK
       //System.out.println(s1);

        boolean flag = encoder.matches("abc", "$2a$10$5hsnyq6yRV/5rmm5nFfddOyod3hzrEiRG8HIVWGQCsKKM9PFsjPGm");
        boolean flag1 = encoder.matches("abc", "$2a$10$UPXlWPQV1vPCVkCQaCjs8ewLVlqelFGs/g11t8cWb0/yYF2SOByMK");
        System.out.println(flag+"--------"+flag1);
    }
}
