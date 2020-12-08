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
 * ������Ŀ����� �������
 * @author chy_zpark
 *
 */
public class ProjectNo implements Callable<String> {
	
	// ��ǰ���
	private static StringBuffer numStr = null;
	
	// ��ʼ����� Map
	private static Map<String,AtomicInteger> map = new ConcurrentHashMap<String,AtomicInteger>();
	
	// ϵͳ����ʱ����Ҫ 
	public static void setMap(String key, AtomicInteger num) {
		ProjectNo.map.put(key, num);
	}

	// ���嵱���ʼ��toDay
	private static StringBuffer toDayStr = new StringBuffer(newDate());
	
	// ģ����+yyyyMMdd   ����   ģ����+yyyyMMdd+ģ���� 
	private static StringBuffer moduleLogogram;
	
	/**
	 * ģ�������
	 * @return
	 */
	public static StringBuffer getModuleLogogram() {
		return moduleLogogram;
	}

	public static void setModuleLogogram(String moduleLogogram) {
		ProjectNo.moduleLogogram = new StringBuffer(moduleLogogram);
	}
	
	/**
	 *  ���ɱ��   ģ���д+������+4λ��ˮ��
	 */
	public String call() throws Exception {
		synchronized (ProjectNo.class) {
			// ��ǰ����
	    	String newDayStr = newDate();
//	    	
			// ������ǵ������  ������������
	    	if(!toDayStr.toString().equals(newDayStr.toString())) {
	    		toDayStr = new StringBuffer(newDayStr);
	    		map = new ConcurrentHashMap<String,AtomicInteger>();
	    	}
			
			// �ַ�ƴ��
	    	StringBuffer stringBuilder = new StringBuffer();
			
	    	// ��ƥ��map���Ƿ��ж�Ӧҵ������+1��������֮�����   keyΪ��� ģ����,valueΪ����������������ҵ�� if��ͬ���ֵ
			// ����mapKey
			StringBuffer mapKey = null;
			
			if(moduleLogogram==null) {
				mapKey = toDayStr;
			}else {
				mapKey = new StringBuffer(moduleLogogram.toString()).append(toDayStr);
			}
			
			AtomicInteger moduleLogogramInt = map.get(mapKey.toString());
	    	if(moduleLogogramInt == null) {
	    		// ûֱֵ�ӷ����µ�key��value
	    		moduleLogogramInt = new AtomicInteger(1);
	    		// ģ����Ϊ��ʱ,����ƴ��
    			numStr = stringBuilder.append(mapKey).append(String.format("%04d", moduleLogogramInt.get()));
    			map.put(mapKey.toString(), moduleLogogramInt);
	    	}else {
	    		// ��ֵ   ȡ�����ж��Ƿ����9999
	    		// С��9999  +1
	    		// �����쳣  ��������
	    		if(moduleLogogramInt.get()<200) {
	    			numStr = stringBuilder.append(mapKey).append(String.format("%04d", moduleLogogramInt.get()));
	    			moduleLogogramInt.incrementAndGet();
	    			map.put(mapKey.toString(), moduleLogogramInt);
	    		}else {
	    			throw new IllegalArgumentException("���������������");
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
