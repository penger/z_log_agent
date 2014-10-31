package go.home;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TestHashSet {

	private static HashSet<String> current=new HashSet<String>();
	private static HashSet<String> previous=new HashSet<String>();
	
	public static void main(String[] args) {
		insertnitems(current, 100);
		previous=current;
		int size = current.size();
		System.out.println(size);
		
		previous=(HashSet<String>) current.clone();
		System.out.println(current.size()+"     "+previous.size());
		current.clear();
		
//		while(iterator.hasNext()){
//			String next = iterator.next();
//			System.out.println(next);
////			current.remove(next);
//		}
//		while(size>0){	
//			String next = iterator.next();
//			System.out.println(next);
//			current.remove(next);
//			size--;
//		}
		System.out.println(current.size()+"     "+previous.size());
	}
	
	
	private static void  insertnitems(Set<String> set,int size){
		for(int i=0;i<size;i++){
			set.add(i+"");
			System.out.println(i);
		}
	}

}
