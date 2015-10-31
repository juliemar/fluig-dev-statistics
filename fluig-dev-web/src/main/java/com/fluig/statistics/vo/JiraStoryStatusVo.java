package com.fluig.statistics.vo;

import java.io.Serializable;

public class JiraStoryStatusVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String name;
	
	private String status;
	
	private String link;
	
	private String style= "default";
	
	private int totalIssues = 0;
	
	private int totalIssuesOpen =0;
	
	private int totalIssuesClose =0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTotalIssues() {
		return totalIssues;
	}

	public void setTotalIssues(int totalIssues) {
		this.totalIssues = totalIssues;
	}

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

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	
	
}
