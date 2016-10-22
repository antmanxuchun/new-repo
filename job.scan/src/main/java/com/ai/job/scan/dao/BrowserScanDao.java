package com.ai.job.scan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.ai.job.scan.bean.JobInfo;

public class BrowserScanDao {
	private static String NAMESPACE = "JobScanDao.";
	private SqlSession session;
	
	public SqlSession getSession() {
		return session;
	}
	
	public void setSession(SqlSession session) {
		this.session = session;
	}
	
	/**
	 * 查询url网页信息
	 * @return
	 */
	public List<Map<String,String>> selectUrlList(){
		List<Map<String,String>> urlList = session.selectList(NAMESPACE+"queryUrlInfos", null);
		return urlList;
	}
	
	/**
	 * 查询城市信息
	 * @return
	 */
	public List<Map<String,String>> selectCityList(){
		List<Map<String,String>> urlList = session.selectList(NAMESPACE+"queryCityInfos", null);
		return urlList;
	}
	
	/**
	 * 查询学历信息
	 * @return
	 */
	public List<Map<String,String>> selectEduList(){
		List<Map<String,String>> urlList = session.selectList(NAMESPACE+"queryEduInfos", null);
		return urlList;
	}
	
	/**
	 * 插入职位信息表
	 * @param jobInfoList
	 * @return
	 */
	public int insertJobInfo(List<JobInfo> jobInfoList){
		if(null != jobInfoList && jobInfoList.size() > 0){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("list", jobInfoList);
			session.insert(NAMESPACE+"insertJobInfo", param);
//			for(int i=0;i<jobInfoList.size();i++){
//				session.insert(NAMESPACE+"insertJobInfo", jobInfoList.get(i));				
//			}
		}
		return 0;
	}
	
	/**
	 * 删除职位信息表
	 * @return
	 */
	public int deleteJobInfo(){
		int result = session.delete(NAMESPACE+"deleteJobInfo");
		return result;
	}
	
}
