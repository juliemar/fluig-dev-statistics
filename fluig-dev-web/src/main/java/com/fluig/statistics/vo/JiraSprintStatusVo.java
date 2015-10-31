package com.fluig.statistics.vo;

import java.io.Serializable;

public class JiraSprintStatusVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int totalIssuesOpen = 0;
	
	private int totalIssuesClose =0;
	
	private int totalIssuesProgress =0;

	private int totalIssues =0;
	
	private int sprintProgress =0;

	public int getTotalIssuesOpen() {
		return totalIssuesOpen;
	}

	public void setTotalIssuesOpen(int totalIssuesOpen) {
		this.totalIssuesOpen = totalIssuesOpen;
	}

	public int getTotalIssuesClose() {
		return totalIssuesClose;
	}

	public void setTotalIssuesClose(int totalIssuesClose) {
		this.totalIssuesClose = totalIssuesClose;
	}

	public int getTotalIssuesProgress() {
		return totalIssuesProgress;
	}

	public void setTotalIssuesProgress(int totalIssuesProgress) {
		this.totalIssuesProgress = totalIssuesProgress;
	}

	public int getTotalIssues() {
		return totalIssues;
	}

	public void setTotalIssues(int totalIssues) {
		this.totalIssues = totalIssues;
	}

	public int getSprintProgress() {
		return sprintProgress;
	}

	public void setSprintProgress(int sprintProgress) {
		this.sprintProgress = sprintProgress;
	}

}
