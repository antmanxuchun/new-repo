package com.ai.job.scan.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ai.job.scan.bean.JobInfo;
import com.ai.job.scan.dao.BrowserScanDao;
import com.ai.job.scan.exception.BusinessException;
import com.ai.job.scan.util.CollectionUtil;

public class BrowserScanService {
	private BrowserScanDao scanDao;
	
	public BrowserScanDao getScanDao() {
		return scanDao;
	}

	public void setScanDao(BrowserScanDao scanDao) {
		this.scanDao = scanDao;
	}
	
	public void scanUrl() throws BusinessException{
		scanDao.deleteJobInfo();
		List<Map<String,String>> urlList = scanDao.selectUrlList();
		if(null != urlList && urlList.size() > 0){
			String url = null;
			Connection conn = null;
			Document doc = null;
			List<Map<String,String>> jobList = new ArrayList<Map<String,String>>();
			for(int i=0;i<urlList.size();i++){
				url = urlList.get(i).get("urlAddr");
				String urlTemp = null;
				int paramNum = 1;
				do {
					urlTemp = url.replace("param1", paramNum + "");
					System.out.println(urlTemp);
					conn = Jsoup.connect(urlTemp);
					try {
						doc = conn.get();
					} catch (IOException e) {
						throw new BusinessException("网址无法访问："+e.getMessage());
					}
					Elements docs = doc.getElementsByTag("a");
					if (null != docs) {
						String urlStr = null;
						String text = null;
						List<String> jobs = new ArrayList<String>();
						List<Map<String, String>> gongsi = new ArrayList<Map<String, String>>();
						List<String> gongsiList = new ArrayList<String>();
						for (Element els : docs) {
							urlStr = els.attr("href");
							text = els.text();
							if (!urlStr.contains("http")
									&& (urlStr.contains("gongsi") || urlStr
											.contains("jobs"))
									&& urlStr.endsWith("html")) {
								if (urlStr.contains("jobs")) {
									jobs.add(urlStr);
								}
								if (urlStr.contains("gongsi")
										&& !gongsiList.contains(urlStr)) {
									gongsiList.add(urlStr);
									Map<String, String> gongsiMap = new HashMap<String, String>();
									gongsiMap.put("url", urlStr);
									gongsiMap.put("name", text);
									gongsi.add(gongsiMap);
								}
							}
						}
						for (int j = 0; j < jobs.size(); j++) {
							Map<String, String> jobMap = new HashMap<String, String>();
							jobMap.put("job", jobs.get(j));
							if (i <= gongsi.size() - 1) {
								jobMap.put("gongsiUrl", gongsi.get(j)
										.get("url"));
								jobMap.put("gongsiName",
										gongsi.get(j).get("name"));
							}
							jobList.add(jobMap);
						}
						getJobDetail(jobList);
					}
					paramNum++;
				}while(!doc.text().contains("链接不存在"));
			}
		}
	}

	/**
	 * 获取job详细信息
	 * @param jobList
	 * @throws BusinessException 
	 */
	private void getJobDetail(List<Map<String,String>> jobList) throws BusinessException {
		if(null != jobList && jobList.size() >0){
			String jobUrl = null;
			String gongsiUrl = null;
			String gongsiName = null;
			Pattern exp=Pattern.compile("^//[\\w\\.\\-]+(?:/|(?:/[\\w\\.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);
			List<JobInfo> jobBeanList = new ArrayList<JobInfo>();
			for(int i=0;i<jobList.size();i++){
				jobUrl = jobList.get(i).get("job");
				gongsiUrl = jobList.get(i).get("gongsiUrl");
				gongsiName = jobList.get(i).get("gongsiName");
				if(null != jobUrl && jobUrl.length() > 0 && exp.matcher(jobUrl).matches()
						&& null != gongsiUrl && gongsiUrl.length() > 0 && exp.matcher(gongsiUrl).matches()){
					jobBeanList.add(scanJobUrl(jobUrl,gongsiUrl,gongsiName));
				}
			}
			System.out.println(jobBeanList);
			scanDao.insertJobInfo(jobBeanList);
		}
	}

	/**
	 * 扫面jobUrl
	 * @param jobUrl
	 * @return 
	 * @throws BusinessException 
	 */
	private JobInfo scanJobUrl(String jobUrl,String gongsiUrl,String gongsiName) throws BusinessException {
		Connection  jobCon = Jsoup.connect("http:"+jobUrl);
		Document jobDoc = null;
		try {
			jobDoc = jobCon.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements jobRequest = jobDoc.getElementsByClass("job_request");
		Elements jobBt = jobDoc.getElementsByClass("job_bt");
		JobInfo job = getJobRequest(jobRequest);
		job.setGongsiName(gongsiName);
		job.setGongsiUrl(gongsiUrl);
		job.setJobUrl(jobUrl);
		getJobBt(jobBt,job);
		return job;
	}

	/**
	 * 获取职位描述基本信息
	 * @param jobRequest
	 * @return 
	 * @throws BusinessException 
	 */
	private JobInfo getJobRequest(Elements jobRequest) throws BusinessException {
		List<String> jobReqList = new ArrayList<String>();
		if(null != jobRequest){
			for(Element elt : jobRequest){
				Elements pEles = elt.getElementsByTag("p");
				//地点、薪资、经验、学历、发布时间、职位属性
				for(Element elt1:pEles){
					Elements spanEles = elt1.getElementsByTag("span");
					if(null != spanEles && spanEles.size() > 0){
						for(Element elt2:spanEles){
							jobReqList.add(elt2.text());
						}
					}else{
						jobReqList.add(elt1.text());
					}
				}
			}
		}
		JobInfo job = analyJobRequest(jobReqList);
		return job;
	}
	/**
	 * 解析字段
	 * @param jobReqList
	 * @return 
	 * @throws BusinessException 
	 */
	private JobInfo analyJobRequest(List<String> jobReqList) throws BusinessException {
		JobInfo job = new JobInfo();
		if(null != jobReqList && jobReqList.size() > 0){
			List<Map<String, String>> cityMapList = scanDao.selectCityList();
			List<String> cityNameList = CollectionUtil.mapListToLitAccordToKey(cityMapList, "cityName");
			Map<String,String> cityIdNameMap = CollectionUtil.mapListToMapAccordToKeyVal(cityMapList, "cityName", "cityId");
			List<Map<String, String>> eduMapList = scanDao.selectEduList();
			List<String> eduNameList = CollectionUtil.mapListToLitAccordToKey(eduMapList, "eduName");
			Map<String,String> eduIdNameMap = CollectionUtil.mapListToMapAccordToKeyVal(eduMapList, "eduName", "eduId");
			String jobStr = null;
			boolean salary = false;
			boolean addr = false;
			boolean expTime = false;
			boolean edu = false;
			boolean pubDate = false;
			for(int i=0;i<jobReqList.size();i++){
				jobStr = jobReqList.get(i);
				if(!salary){
					salary = getSalary(job,jobStr);
				}
				if(!addr){					
					addr = getJobAddr(job,jobStr,cityNameList,cityIdNameMap);
				}
				if(!expTime){					
					expTime = getExpTime(job,jobStr);
				}
				if(!edu){
					edu = getEdu(job,jobStr,eduNameList,eduIdNameMap);
				}
				if(!pubDate){
					pubDate = getPubDate(job,jobStr);
				}
			}
		}
		return job;
	}

	/**
	 * 获取该职位的发布日期
	 * @param job
	 * @param jobStr
	 * @return
	 */
	private boolean getPubDate(JobInfo job, String jobStr) {
		boolean result = false;
		Pattern pattern = Pattern.compile(".*[0-9]{4}[^0-9][0-9]{1,2}[^0-9][0-9]{1,2}.*");//
		if(null != jobStr && pattern.matcher(jobStr).matches()){
			job.setPubDate(jobStr.replaceAll("\\D", ""));
			result = true;
		}
		return result;
	}

	/**
	 * 获取学历要求
	 * @param job
	 * @param jobStr
	 * @param eduNameList
	 * @param eduIdNameMap
	 * @return
	 */
	public boolean getEdu(JobInfo job, String jobStr, List<String> eduNameList,
			Map<String, String> eduIdNameMap) {
		boolean result = false;
		String patterStr = ".*["+eduNameList.toString().replace(" ", "").replace(",", "|")+"].*";
		Pattern pattern = Pattern.compile(patterStr);
		if(null != jobStr && jobStr.length() > 0 && pattern.matcher(jobStr).matches()){
			for(int i=0;i<eduNameList.size();i++){
				if(jobStr.contains(eduNameList.get(i))){
					String jobId = String.valueOf(eduIdNameMap.get(eduNameList.get(i)));
					job.setEduId(Integer.parseInt(jobId));
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * 获取工作经验
	 * @param job
	 * @param jobStr
	 * @return
	 * @throws BusinessException
	 */
	private boolean getExpTime(JobInfo job, String jobStr) throws BusinessException {
		boolean result = false;
		Pattern pattern = Pattern.compile(".*[年|year].*",Pattern.CASE_INSENSITIVE);
		if(null != jobStr && pattern.matcher(jobStr).matches()){
			pattern = Pattern.compile("\\D+");
			String[] jobStrSplit = pattern.split(jobStr);
			if(jobStrSplit.length > 0 && StringUtil.isNumeric(jobStrSplit[jobStrSplit.length-1])){
				job.setJobExpTime(Integer.parseInt(jobStrSplit[jobStrSplit.length-1]));
				result = true;
			}
		}
		return result;
	}

	/**
	 * 获取工作地点
	 * @param job
	 * @param jobStr
	 * @param cityNameList
	 * @param cityIdNameMap
	 * @return
	 */
	private boolean getJobAddr(JobInfo job, String jobStr, List<String> cityNameList, Map<String, String> cityIdNameMap) {
		boolean result = false;
		if(cityNameList.contains(jobStr.trim())){
			String cityId = String.valueOf(cityIdNameMap.get(jobStr.trim()));
			if(null != cityId && cityId.length() > 0){
				job.setJobAddrId(Integer.parseInt(cityId));
				result = true;
			}
		}
		return result;
	}

	/**
	 * 获取薪资水平
	 * @param job
	 * @param jobStr
	 * @return 
	 * @throws BusinessException
	 */
	public boolean getSalary(JobInfo job, String jobStr) throws BusinessException {
		boolean result = false;
		Pattern pattern = Pattern.compile("\\d*[ kK]*[\\-~]\\d*[ kK]*");
		if(null != jobStr && pattern.matcher(jobStr).matches()){
			pattern = Pattern.compile("\\D+");
			String[] jobStrSplit = pattern.split(jobStr);
			if(jobStrSplit.length == 2){
				try {
					job.setSalaryDown(Integer.parseInt(jobStrSplit[0]));
					job.setSalaryUp(Integer.parseInt(jobStrSplit[1]));
					result = true;
				}catch(Exception e){
					e.printStackTrace();
					throw new BusinessException(jobStr+"=工资格式非法!!");
				}
			}
		}
		return result;
	}

	private void getJobBt(Elements jobRequest, JobInfo job) {
		StringBuilder jobDetail = new StringBuilder();
		StringBuilder jobReq = new StringBuilder();
		if(null != jobRequest && jobRequest.size() > 0){
			for(Element ele : jobRequest){
				Elements eles = ele.getElementsByTag("p");
				if(null != eles && eles.size() > 0){
					String text = null;
					boolean isReq = false;
					for(Element ele1:eles){
						text = ele1.text();
						if(null != text && text.contains("职位要求")){
							isReq = true;
						}
						if(isReq){
							jobReq.append(text).append("~!~");
						}else{
							jobDetail.append(text).append("~!~");
						}
					}
				}
			}
		}
		if(jobDetail.toString().contains("~!~")){
			jobDetail.delete(jobDetail.lastIndexOf("~!~"), jobDetail.length());
		}
		if(jobReq.toString().contains("~!~")){
			jobReq.delete(jobReq.lastIndexOf("~!~"), jobReq.length());
		}
		job.setJobDetail(jobDetail.toString());
		job.setJobRequest(jobReq.toString());
	}
}
