package com.bld.utils;

import java.util.AbstractCollection;
import java.util.Iterator;

public class CollectionUtils {

	public interface Condition{
		boolean ifCondition(Object obj);
	}
	public interface Excute{
		void doExcute(Iterator<?> it,AbstractCollection ac,Object obj);
	}
	 public static void remove(AbstractCollection coll,Condition condition)
	 {
		 Iterator it= coll.iterator();
		 while(it.hasNext())
		 {
			 Object item=it.next();
			 if(condition==null || condition.ifCondition(item))
			 {
			 it.remove();
			 }
		 }
	 }
	 
	 public static void excute(AbstractCollection coll,Condition condition,Excute excute)
	 {
		 Iterator it= coll.iterator();
		 while(it.hasNext())
		 {
			
			 Object item=it.next();
			 if(condition==null || (condition!=null && condition.ifCondition(item)))
			 {
				 excute.doExcute(it,coll, item);
			 }
		 }
	 }

	
}
