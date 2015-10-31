package com.fluig.statistics.jira;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.atlassian.jira.rest.client.IssueRestClient;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.SearchRestClient;
import com.atlassian.jira.rest.client.UserRestClient;
import com.atlassian.jira.rest.client.domain.BasicComponent;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueFieldId;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.domain.TimeTracking;
import com.atlassian.jira.rest.client.domain.User;
import com.atlassian.jira.rest.client.domain.Version;
import com.atlassian.jira.rest.client.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.domain.input.FieldInput;
import com.atlassian.jira.rest.client.domain.input.IssueInput;
import com.atlassian.jira.rest.client.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.domain.input.LinkIssuesInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

public class JiraRestManager {
	private static int TIMEOUT_TIME = 30;
	private static int PAGE_SIZE = 50;
	
	private JiraRestClient client;
	
	public JiraRestManager(URI jiraURI, String user, String pass) {
		AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		this.client = factory.createWithBasicHttpAuthentication(jiraURI, user, pass);
		
		
		
	}
	
	
	public User getUserInfo(String username) throws InterruptedException, ExecutionException{
		UserRestClient userClient = client.getUserClient();
		
		return userClient.getUser(username).get();
	}
	
	public List<String> searchIssues(String jql) throws InterruptedException, ExecutionException, TimeoutException {
		SearchRestClient searchClient = client.getSearchClient();
		
		ArrayList<String> issues = new ArrayList<String>();
		
		SearchResult finalResult;
		do {
			finalResult = searchClient.searchJql(jql, PAGE_SIZE, issues.size()).get(TIMEOUT_TIME, TimeUnit.SECONDS);
			
			Iterator<BasicIssue> it = finalResult.getIssues().iterator();
			while(it.hasNext()) {
				BasicIssue issue = it.next();
				issues.add(issue.getKey());
			}
			
			System.out.println(issues.size() + " of " + finalResult.getTotal() + " loaded...");
			
		} while(finalResult.getTotal() > issues.size());
		
		return issues;
	}
	
	
	public void listGroupTime(){
		SearchRestClient searchClient = client.getSearchClient();
	}
	
	public ArrayList<BasicIssue> searchBasicIssues(String jql) throws InterruptedException, ExecutionException, TimeoutException {
		SearchRestClient searchClient = client.getSearchClient();
		
		ArrayList<BasicIssue> issues = new ArrayList<BasicIssue>();
		
		SearchResult finalResult;
		do {
			finalResult = searchClient.searchJql(jql, PAGE_SIZE, issues.size()).get(TIMEOUT_TIME, TimeUnit.SECONDS);
			
			Iterator<BasicIssue> it = finalResult.getIssues().iterator();
			while(it.hasNext()) {
				BasicIssue issue = it.next();
				issues.add(issue);
			}
			
			System.out.println(issues.size() + " of " + finalResult.getTotal() + " loaded...");
			
		} while(finalResult.getTotal() > issues.size());
		
		return issues;
	}
	
	
	public Issue getIssueInfo(String issue) throws InterruptedException, ExecutionException, TimeoutException {
		IssueRestClient issueClient = client.getIssueClient();
		Issue fullIssue = issueClient.getIssue(issue).get(TIMEOUT_TIME, TimeUnit.SECONDS);
		
		return fullIssue;
	}
	
	public String createIssue(String project, String summary, String description, long typeId, Iterable<BasicComponent> basicComponents) throws InterruptedException, ExecutionException, TimeoutException {
		return this.createIssue(project, summary, description, typeId, basicComponents, null,null);
	}
	
	
	public String createIssue(String project, String summary, String description, long typeId, Iterable<BasicComponent> basicComponents,String epicLink,List<Version> releaseVersion) throws InterruptedException, ExecutionException, TimeoutException {
		IssueInputBuilder builder = new IssueInputBuilder(project, typeId);
		if(summary == null || summary.isEmpty()) {
			summary = "New Issue";
		}
		
		builder.setSummary(summary);
		builder.setDescription(description);
		
		if(basicComponents != null) {
			builder.setComponents(basicComponents);
		}
		
		if(epicLink!=null){
		    builder.setFieldValue("customfield_11182", epicLink);
		}
		
		
		
		if(releaseVersion!=null){
			builder.setFixVersions(releaseVersion);
		}
		
		IssueInput input = builder.build();
		
		
		
		BasicIssue newIssue = this.client.getIssueClient().createIssue(input).get(TIMEOUT_TIME, TimeUnit.SECONDS);
		
		
		
		return newIssue.getKey();
	}

	public String createSubtask(String project, String parentKey, long typeId, String summary,List<Version> releaseVersion) throws Exception {
		return this.createSubtask(project, parentKey,null, typeId, summary, releaseVersion, null,null);
	}
	
	public String createSubtask(String project, String parentKey,Iterable<BasicComponent> components, long typeId, String summary,List<Version> releaseVersion,String description, Integer time) throws Exception {
		IssueInputBuilder builder = new IssueInputBuilder(project, typeId);
		if(summary == null || summary.isEmpty()) {
			summary = "New Issue";
		}
		builder.setSummary(summary);
		
		Map<String, Object> parent = new HashMap<String, Object>();
	    parent.put("key", parentKey);
	    FieldInput parentField = new FieldInput("parent", new ComplexIssueInputFieldValue(parent));
	    builder.setFieldInput(parentField);
	    builder.setDescription(summary);
	    
	    
	    if(description!=null){
	    	builder.setDescription(description);
	    }
	    
	    if(components!=null){
	    	builder.setComponents(components);
	    }
	    
	    		
	    if(time != null) {
	    	
	    	builder.setFieldValue("timetracking", new TimeTracking(time, time, 0));
		}
	    
		IssueInput input = builder.build();
		
		BasicIssue newIssue = this.client.getIssueClient().createIssue(input).get(TIMEOUT_TIME, TimeUnit.SECONDS);
		return newIssue.getKey();
	}	
	
	
	public void linkIssues(String from, String to, String type) throws InterruptedException, ExecutionException, TimeoutException {
		LinkIssuesInput linkInput = new LinkIssuesInput(from, to, type);
		this.client.getIssueClient().linkIssue(linkInput).get(TIMEOUT_TIME, TimeUnit.SECONDS);
	}
	
//	
//
//	public static void doit() throws Exception {
//		
//		URI uri = new URI("http://jira.totvs.com");
//		
//		
//		IssueRestClient issueClient = client.getIssueClient();
//			
//		System.out.println("Total: " + finalResult.getTotal());
//		
//		while(it.hasNext()) {
//			BasicIssue issue = it.next();
//			
//			System.out.println(issue.getKey());
//			
//			Issue fullIssue = issueClient.getIssue(issue.getKey()).get(TIMEOUT_TIME, TimeUnit.SECONDS);
//			System.out.println("\tSummary: " + fullIssue.getSummary());
//			System.out.println("\tIssue Type: " + fullIssue.getIssueType().getId());
//			System.out.println("\tLinks: " + fullIssue.getIssueLinks());
//			
//			Iterator<Field> fields = fullIssue.getFields().iterator();
//			while(fields.hasNext()) {
//				System.out.println("\t\t" + fields.next().getName());
//			}
////			Field issueField = fullIssue.getFieldByName("issuelinks");
////			System.out.println("\tField: " + issueField);
//		}
//		
//		
//		
//		
//		
//		
//		LinkIssuesInput linkInput = new LinkIssuesInput(newIssue.getKey(), "TES-1", "Test");
//		issueClient.linkIssue(linkInput).get(TIMEOUT_TIME, TimeUnit.SECONDS);
//		
////		input.
////		
////		IssueRestClient issueClient = client.getIssueClient();
////		
////		issueClient.
//		
//	}
}
