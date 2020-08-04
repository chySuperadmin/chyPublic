package test.chy.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
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
	private static int number = 1;
	
	// ���嵱���ʼ��toDay
	private static String toDay = new SimpleDateFormat("yyyyMMdd").format(new Date());
	
	// ģ����
	private static String moduleLogogram;
	
	/**
	 * ģ�������
	 * @return
	 */
	public static String getModuleLogogram() {
		return moduleLogogram;
	}

	public static void setModuleLogogram(String moduleLogogram) {
		IssueNo.moduleLogogram = moduleLogogram;
	}
	
	/**
	 *  ���ɱ��   ģ���д+������+4λ��ˮ��
	 */
	public String call() throws Exception {
		try {
			lock.lock();
	    	String newDay = new SimpleDateFormat("yyyyMMdd").format(new Date());
	    	
	    	// ������ǵ������  ������������;  ��� ���쳬�� 9999�� �׳��쳣
	    	if(!toDay.equals(newDay)) {
	    		toDay = newDay;
	    		number = 1;
	    	}else if(number>9999){
	    		throw new IllegalArgumentException("���������������");
	    	}
	    	
	    	String numStr = String.format("%04d", number);
	    	
	    	// ÿ�λ�ȡ֮��+1
	    	number++;
	    	
	    	// �ַ�ƴ��
	    	StringBuilder stringBuilder = new StringBuilder();
	    	
	    	if(moduleLogogram!=null) {
	    		stringBuilder.append(moduleLogogram);
	    	}
	    	
	        return stringBuilder.append(toDay).append(numStr).toString();
		}catch (Exception e) {
			throw new IllegalArgumentException("���������������");
		} finally {
			lock.unlock();
		}
    }
 
    public static void main(String[] args){
//    	setModuleLogogram("LX");
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
