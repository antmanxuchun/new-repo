package com.ai.job.scan.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtil {
	/**
	 * 获取List<Map>中指定key的元素，并放入List集合中返回
	 * @param arg1
	 * @param key
	 * @return
	 */
	public static <T> List<T> mapListToLitAccordToKey(
			List<Map<String, T>> arg1, String key) {
		List<T> result = null;
		if(null != arg1 && arg1.size() > 0){
			result = new ArrayList<T>();
			Map<String,T> map = null;
			for(int i=0;i<arg1.size();i++){
				map = arg1.get(i);
				if(null != map && null != map.get(key)){					
					result.add(map.get(key));
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取List<Map>中指定两个key的值，分别作为key和value放入一个Map对象中，并返回
	 * @param arg1
	 * @param key
	 * @param val
	 * @return
	 */
	public static <T> Map<T,T> mapListToMapAccordToKeyVal(List<Map<String, T>> arg1, String key,String val){
		Map<T,T> result = null;
		if(null != arg1 && arg1.size() > 0){
			result = new HashMap<T,T>();
			Map<String,T> map = null;
			for(int i=0;i<arg1.size();i++){
				map = arg1.get(i);
				if(null != map && null != map.get(key) && null != map.get(val)){
					result.put(map.get(key), map.get(val));
				}
			}
		}
		return result;
	}
}
