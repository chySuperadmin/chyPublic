package test.chy;

import java.util.HashMap;
import java.util.Map;

import test.chy.entity.OrderBO;

public class test {
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("object", new Object());
		map.put("str", new String("str"));
		map.put("int", 123);
		map.put("order", new OrderBO());
		Object object = map.get("order");
		
		System.out.println(map.get("object"));
		System.out.println(map.get("str"));
		System.out.println(map.get("int"));
		System.out.println(map.get("order"));
		
		
		String str = "1223334444";
		HashMap<String, Integer> hmap = new HashMap<String, Integer>(32);
		for(int i=0;i<str.length();i++) {
			char curChar = str.charAt(i);
			String s = curChar+"";
			hmap.compute(s, (k, v) -> {
                if (v == null) {
                    v = 1;
                } else {
                    v += 1;
                }
                return v;
            });
		}
		System.out.println(hmap.toString());
		
	    long value1 = System.currentTimeMillis();
	    for(long i=0;i<1000000000;i++)
	    {
	        "abcdaaaaaaavrveaaaaaaaae".indexOf("abcd");
	    }
	    long value2 = System.currentTimeMillis();
	    System.out.println(value2-value1);
	    System.out.println("abcdaaaaaaavrveaaaaaaaae".indexOf("abcd"));

	    value1 = System.currentTimeMillis();
	    for(long i=0;i<1000000000;i++)
	    {
	        "abcdaaaaaaavrveaaaaaaaae".startsWith("abcd");
	    }
	    value2 = System.currentTimeMillis();
	    System.out.println(value2-value1);
	    System.out.println("abcdaaaaaaavrveaaaaaaaae".startsWith("abcd"));
	}
	
	public void throwsTest() {
		System.out.println(111111);
<<<<<<< HEAD
		System.out.println(22222);
=======
>>>>>>> branch 'master' of https://github.com/chySuperadmin/chyPublic.git
	}
	
    public static void binaryTools() {
        // 1.从String中读取二进制
        String s  = "1101";
        Integer integer = Integer.valueOf(s, 2);
        // 13
        System.out.println(integer);
        // 2.从 10 进制数中读取二进制
        int i1 = 520;
        // 1000001000
        System.out.println(Integer.toBinaryString(i1));
        // 3.直接声明二进制
        int i2 = 0b1101;
        // 13
        System.out.println(i2);
    }
}
