package test.chy.thread;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ������Ŀ����� �������
 * @author chy_zpark
 *
 */
public class IssueNo implements Callable<String> {
	
	private Lock lock=new ReentrantLock();
	
	// ���嵱���ʼ��int
	private static AtomicInteger number = new AtomicInteger(1);
	
	// ���嵱���ʼ��toDay
	private static StringBuffer toDay = new StringBuffer(newDate());
	
	// ģ����
	private static StringBuffer moduleLogogram;
	
	/**
	 * ģ�������
	 * @return
	 */
	public static StringBuffer getModuleLogogram() {
		return moduleLogogram;
	}

	public static void setModuleLogogram(StringBuffer moduleLogogram) {
		IssueNo.moduleLogogram = moduleLogogram;
	}
	
	/**
	 *  ���ɱ��   ģ���д+������+4λ��ˮ��
	 */
	public String call() throws Exception {
		try {
			lock.lock();
	    	
			StringBuffer newDay = newDate();
	    	
	    	// ������ǵ������  ������������;  ��� ���쳬�� 9999�� �׳��쳣
	    	if(!toDay.toString().equals(newDay.toString())) {
	    		toDay = newDay;
	    		number.set(1);
	    	}else if(number.get()>19){
	    		throw new IllegalArgumentException("���������������");
	    	}
	    	StringBuffer buffer = new StringBuffer(String.format("%04d", number.get()));
	    	// ÿ�λ�ȡ֮��+1
	    	number.incrementAndGet();
	    	
	    	// �ַ�ƴ��
	    	StringBuffer stringBuilder = new StringBuffer();
	    	
	    	if(moduleLogogram!=null) {
	    		stringBuilder.append(moduleLogogram);
	    	}
	    	
	        return stringBuilder.append(toDay).append(buffer).toString();
		}catch (Exception e) {
			throw new IllegalArgumentException("���������������");
		} finally {
			lock.unlock();
		}
    }
	
	private static StringBuffer newDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		
		Instant instant = date.toInstant();

        LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return new StringBuffer(localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
	}
 
    public static void main(String[] args){
    	setModuleLogogram(new StringBuffer("LX"));
        while(true) {
        	FutureTask<String> futureTask = new FutureTask<String>((Callable<String>) new IssueNo());
        	FutureTask<String> futureTask1 = new FutureTask<String>((Callable<String>) new IssueNo());
//        	FutureTask<String> futureTask2 = new FutureTask<String>((Callable<String>) new IssueNo());
        	FutureTask<String> futureTask3 = new FutureTask<String>((Callable<String>) new IssueNo());
	        new Thread(futureTask).start();
	        new Thread(futureTask1).start();
//	        new Thread(futureTask2).start();
	        new Thread(futureTask3).start();
        	try {
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
