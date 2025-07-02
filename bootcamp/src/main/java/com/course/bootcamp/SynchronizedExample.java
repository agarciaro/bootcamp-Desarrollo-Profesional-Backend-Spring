package com.course.bootcamp;

import java.util.ArrayList;
import java.util.List;

public class SynchronizedExample {

	private static List<String> items = new ArrayList<>();
	//private static List<String> items = Collections.synchronizedList(new ArrayList<>());

	public synchronized static void addItem(String item) {
		items.add(item);
		System.out.println("Added item: " + item + ", Cart size: " + items.size());
	}

	public static void main(String[] args) throws InterruptedException {
		
		String cart = new String();
		Runnable addItemTask = () -> {
			final String cartThread = cart.concat("Item-" + Thread.currentThread().getName());
			SynchronizedExample.addItem(cartThread);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		new Thread(addItemTask, "User1").start();
		new Thread(addItemTask, "User2").start();
		new Thread(addItemTask, "User3").start();
		new Thread(addItemTask, "User4").start();
		
		Thread.sleep(2000);
		System.out.println("Final size=" + items.size());
	}

}
