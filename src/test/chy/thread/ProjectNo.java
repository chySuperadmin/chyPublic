package test.chy.thread;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成项目编码和 立项编码
 * @author chy_zpark
 *
 */
public class ProjectNo implements Callable<String> {
	
	// 当前编号
	private static StringBuffer numStr = null;
	
	// 初始化编号 Map
	private static Map<String,AtomicInteger> map = new ConcurrentHashMap<String,AtomicInteger>();
	
	// 系统重启时，需要 
	public static void setMap(String key, AtomicInteger num) {
		ProjectNo.map.put(key, num);
	}

	// 定义当天初始化toDay
	private static StringBuffer toDayStr = new StringBuffer(newDate());
	
	// 模块简称+yyyyMMdd   或者   模块简称+yyyyMMdd+模块简称 
	private static StringBuffer moduleLogogram;
	
	/**
	 * 模块简称入参
	 * @return
	 */
	public static StringBuffer getModuleLogogram() {
		return moduleLogogram;
	}

	public static void setModuleLogogram(String moduleLogogram) {
		ProjectNo.moduleLogogram = new StringBuffer(moduleLogogram);
	}
	
	/**
	 *  生成编号   模块简写+年月日+4位流水号
	 */
	public String call() throws Exception {
		synchronized (ProjectNo.class) {
			// 当前日期
	    	String newDayStr = newDate();
//	    	
			// 如果不是当天编码  重新生成日期
	    	if(!toDayStr.toString().equals(newDayStr.toString())) {
	    		toDayStr = new StringBuffer(newDayStr);
	    		map = new ConcurrentHashMap<String,AtomicInteger>();
	    	}
			
			// 字符拼接
	    	StringBuffer stringBuilder = new StringBuffer();
			
	    	// 先匹配map中是否有对应业务编号有+1，无生成之后插入   key为入参 模块简称,value为自增正整数，根据业务 if不同最大值
			// 定义mapKey
			StringBuffer mapKey = null;
			
			if(moduleLogogram==null) {
				mapKey = toDayStr;
			}else {
				mapKey = new StringBuffer(moduleLogogram.toString()).append(toDayStr);
			}
			
			AtomicInteger moduleLogogramInt = map.get(mapKey.toString());
	    	if(moduleLogogramInt == null) {
	    		// 没值直接放入新的key、value
	    		moduleLogogramInt = new AtomicInteger(1);
	    		// 模块简称为空时,不做拼接
    			numStr = stringBuilder.append(mapKey).append(String.format("%04d", moduleLogogramInt.get()));
    			map.put(mapKey.toString(), moduleLogogramInt);
	    	}else {
	    		// 有值   取出后判断是否等于9999
	    		// 小于9999  +1
	    		// 否则异常  不让生成
	    		if(moduleLogogramInt.get()<200) {
	    			numStr = stringBuilder.append(mapKey).append(String.format("%04d", moduleLogogramInt.get()));
	    			moduleLogogramInt.incrementAndGet();
	    			map.put(mapKey.toString(), moduleLogogramInt);
	    		}else {
	    			throw new IllegalArgumentException("超出当天可生成量");
	    		}
	    	}
	        return numStr.toString();
		}
    }
	
	private static String newDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		
		Instant instant = date.toInstant();

        LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
 
    public static void main(String[] args){
        while(true) {
        	FutureTask<String> futureTask = new FutureTask<String>((Callable<String>) new ProjectNo());
        	FutureTask<String> futureTask1 = new FutureTask<String>((Callable<String>) new ProjectNo());
//        	FutureTask<String> futureTask2 = new FutureTask<String>((Callable<String>) new ProjectNo());
        	FutureTask<String> futureTask3 = new FutureTask<String>((Callable<String>) new ProjectNo());
	        new Thread(futureTask).start();
	        new Thread(futureTask1).start();
//	        new Thread(futureTask2).start();
	        new Thread(futureTask3).start();
        	try {
        		setModuleLogogram("LX");
        		String result = futureTask.get();
        		System.out.println("result=="+result);
        		String result1 = futureTask1.get();
        		System.out.println("result1=="+result1);
//        		String result2 = futureTask2.get();
//        		System.out.println("result2=="+result2);
        		String result3 = futureTask3.get();
        		System.out.println("result3=="+result3);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	} catch (ExecutionException e) {
        		e.printStackTrace();
        		throw new IllegalArgumentException(e.fillInStackTrace());
        	}
        }
    }
}
