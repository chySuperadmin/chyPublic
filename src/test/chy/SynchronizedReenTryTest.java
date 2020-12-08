package test.chy;

public class SynchronizedReenTryTest extends SuperXttblog {
	    public static void main(String[] args) {
	    	SynchronizedReenTryTest child = new SynchronizedReenTryTest();
	        child.doSomething();
	    }

	    public synchronized void doSomething() {
	        System.out.println("child.doSomething()" + Thread.currentThread().getName());
<<<<<<< HEAD
	        doAnotherThing(); // 调用自己类中其他的synchronized方法33
=======
	        doAnotherThing(); // 调用自己类中其他的synchronized方法
>>>>>>> branch 'master' of https://github.com/chySuperadmin/chyPublic.git
	    }

	    private synchronized void doAnotherThing() {
	        super.doSomething(); // 调用父类的synchronized方法
	        System.out.println("child.doAnotherThing()" + Thread.currentThread().getName());
	    }
	}

	class SuperXttblog {
	    public synchronized void doSomething() {
	        System.out.println("father.doSomething()" + Thread.currentThread().getName());
	    }
	}