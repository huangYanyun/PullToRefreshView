package com.test.notify;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observer;

/**
 * This class used as a watcher to send notification
 * @author huangyanyun
 *
 */
public class NotificationCenter {
	
	private HashMap<String,DummyObervable> mObservables;
	public static NotificationCenter instance = null;
	
	public static NotificationCenter getInstance(){
		if(instance == null){
			instance = new NotificationCenter();
		}
		return instance;
	}
	
	public void addObservable(String notification,Observer observer){
		DummyObervable mObservable = mObservables.get(notification);
		if(mObservable == null){
			mObservable = new DummyObervable(notification);
			mObservables.put(notification, mObservable);
		}
		mObservable.addObeserver(observer);
	}
	
	public void removeObservable(String notification,Observer observer){
		DummyObervable mObservable = mObservables.get(notification);
		if(mObservable == null){
			return;
		}else{
			mObservable.removeObeserver(observer);
		}
	}
	
	public void postNotification(String notification,Observer observer){
		DummyObervable mObservable = mObservables.get(notification);
		if(mObservable != null){
			if(observer != null){
				mObservable.postNotification(observer);
			}else{
				mObservable.postNotification(null);
			}
		}
	}
	
	
	class DummyObervable{
		
		String name;
		List<WeakReference<Observer>> _list = new ArrayList<WeakReference<Observer>>();
		
		private DummyObervable(String notification){
			name = notification;
		}
		
		private void addObeserver(Observer obsever){
			List<WeakReference<Observer>> list = 
					new ArrayList<WeakReference<Observer>>(_list);
			for(WeakReference<Observer> item : list){
				if(item.get() == obsever){
					return;
				}
				_list.add(new WeakReference<Observer>(obsever));
			}
		}
		
		private void removeObeserver(Observer observer){
			List<WeakReference<Observer>> list = 
					new ArrayList<WeakReference<Observer>>(_list);
			for(WeakReference<Observer> item : list){
				if(item.get() == observer){
					_list.remove(item);
				}
			}
		}
		
		private void postNotification(Observer observer){
			List<WeakReference<Observer>> list = 
					new ArrayList<WeakReference<Observer>>(_list);
			for(WeakReference<Observer> item : list){
				if(item.get() == null){
					//delete by GC
					_list.remove(item);
				}else{
//					item.get().update();
				}
			}
		}
		
	}

}
