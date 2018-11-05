package com.ai.job.scan.bean;

public class JobInfo {
	private String scanDate;
	private int jobId;
	private int salaryUp;
	private int salaryDown;
	private int jobAddrId;
	private int eduId;
	private int jobExpTime;
	private String jobDetail;
	private String jobRequest;
	private String pubDate;
	private String gongsiName;
	private String jobUrl;
	private String gongsiUrl;
	
	
	public String getScanDate() {
		return scanDate;
	}
	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getGongsiName() {
		return gongsiName;
	}
	public void setGongsiName(String gongsiName) {
		this.gongsiName = gongsiName;
	}
	public String getJobUrl() {
		return jobUrl;
	}
	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}
	public String getGongsiUrl() {
		return gongsiUrl;
	}
	public void setGongsiUrl(String gongsiUrl) {
		this.gongsiUrl = gongsiUrl;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public int getSalaryUp() {
		return salaryUp;
	}
	public void setSalaryUp(int salaryUp) {
		this.salaryUp = salaryUp;
	}
	public int getSalaryDown() {
		return salaryDown;
	}
	public void setSalaryDown(int salaryDown) {
		this.salaryDown = salaryDown;
	}
	public int getJobAddrId() {
		return jobAddrId;
	}
	public void setJobAddrId(int jobAddrId) {
		this.jobAddrId = jobAddrId;
	}
	public int getEduId() {
		return eduId;
	}
	public void setEduId(int eduId) {
		this.eduId = eduId;
	}
	public int getJobExpTime() {
		return jobExpTime;
	}
	public void setJobExpTime(int jobExpTime) {
		this.jobExpTime = jobExpTime;
	}
	public String getJobDetail() {
		return jobDetail;
	}
	public void setJobDetail(String jobDetail) {
		this.jobDetail = jobDetail;
	}
	public String getJobRequest() {
		return jobRequest;
	}
	public void setJobRequest(String jobRequest) {
		this.jobRequest = jobRequest;
	}
	@Override
	public String toString() {
		return "JobInfo [jobId=" + jobId + ", salaryUp=" + salaryUp
				+ ", salaryDown=" + salaryDown + ", jobAddrId=" + jobAddrId
				+ ", eduId=" + eduId + ", jobExpTime=" + jobExpTime
				+ ", jobDetail=" + jobDetail + ", jobRequest=" + jobRequest
				+ ", pubDate=" + pubDate + ", gongsiName=" + gongsiName
				+ ", jobUrl=" + jobUrl + ", gongsiUrl=" + gongsiUrl +", scanDate="+scanDate+ "]";
	}
}
