package com.fluig.statistics.rest;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.Subtask;
import com.fluig.statistics.constants.JiraConstants;
import com.fluig.statistics.constants.JiraIssueTypes;
import com.fluig.statistics.jira.JiraRestManager;
import com.fluig.statistics.jira.JiraUtils;
import com.fluig.statistics.vo.JiraStoryStatusVo;
import com.fluig.statistics.vo.JiraSprintStatusVo;
import com.totvs.technology.wcm.sdk.rest.WCMRest;

/**
 * Jira - StoryService
 * @author danilo.ganzella
 *
 */
@Path("/story")
public class JiraStoryServiceRest extends WCMRest {

	
		@GET
	    @Produces(MediaType.APPLICATION_JSON)
	    @Path("/status/{sprintId}")
	    public Response sprintStatus(@PathParam("sprintId") String sprintId) {
			
    		try {

	        	String JIRA_SPRINT_QUERY_OPEN = "Sprint = "+sprintId+" AND status =\"Open\" ORDER BY fixVersion DESC";
	        	String JIRA_SPRINT_QUERY_CLOSED = "Sprint = "+sprintId+" AND (status =\"Closed\" OR status =\"Resolved\") ORDER BY fixVersion DESC";
	        	String JIRA_SPRINT_QUERY_PROGRESS = "Sprint = "+sprintId+" AND status =\"In Progress\" ORDER BY fixVersion DESC";
	        	
	    		
	    		JiraRestManager jiraManager= JiraUtils.getJiraManager();
	    		
	    		List<String> issuesOpen = jiraManager.searchIssues(JIRA_SPRINT_QUERY_OPEN);
	    		List<String> issuesClosed = jiraManager.searchIssues(JIRA_SPRINT_QUERY_CLOSED);
	    		List<String> issuesProgress = jiraManager.searchIssues(JIRA_SPRINT_QUERY_PROGRESS);
	    		
	    		JiraSprintStatusVo status = new JiraSprintStatusVo();
	    		
	    		status.setTotalIssuesOpen(issuesOpen.size());
	    		status.setTotalIssuesClose(issuesClosed.size());
	    		status.setTotalIssuesProgress(issuesProgress.size());
	    		
	    		status.setTotalIssues(status.getTotalIssuesClose()+status.getTotalIssuesOpen()+status.getTotalIssuesProgress());
	    		
	    		status.setSprintProgress((int)((status.getTotalIssuesClose()*100)/status.getTotalIssues()));
	    		
	    		return this.buildSuccessResponse(status);
	    		
			} catch (Exception e) {
	            return this.buildErrorResponse(e);
	        }
		}
	
	
	 	@GET
	    @Produces(MediaType.APPLICATION_JSON)
	    @Path("/listStorySprint/{sprintId}")
	    public Response listStorySprint(@PathParam("sprintId") String sprintId) {
	        try {
	            //List<JiraStoryStatusVo> lst = jiraStoryService.listSprintStory(sprintId);
	        	
	        	String JIRA_SPRINT_QUERY = "Sprint = "+sprintId+" AND type in (Story,\"Documentation Story\",\"Documentation FDD\",\"Feature-Driven Development\") ORDER BY fixVersion DESC";
	        	
	    		
	    		JiraRestManager jiraManager= JiraUtils.getJiraManager();
	    		
	    		List<String> userStories = jiraManager.searchIssues(JIRA_SPRINT_QUERY);
	    		
	    		List<JiraStoryStatusVo> stories = new ArrayList<JiraStoryStatusVo>();
	    		
	    		for(String us:userStories) {
	    			
	    			Issue userStory = jiraManager.getIssueInfo(us);
	    			JiraStoryStatusVo jiraIssue = new JiraStoryStatusVo();
	    			
	    			jiraIssue.setName(userStory.getSummary());
	    			jiraIssue.setStatus(userStory.getStatus().getName());
	    			jiraIssue.setLink(JiraConstants.JIRA_URL+"browse/"+userStory.getKey());
	    			
	    			
	    			jiraIssue.setTotalIssues(0);
	    			jiraIssue.setTotalIssuesClose(0);
	    			jiraIssue.setTotalIssuesOpen(0);
	    			
	    			ArrayList<Long> typesOpen = new ArrayList<Long>();
	    		
	    			for(Subtask sub:userStory.getSubtasks()){
	    				
	    				if(sub.getStatus().getName().equalsIgnoreCase("open")||sub.getStatus().getName().equalsIgnoreCase("In Progress")){
	    					jiraIssue.setTotalIssuesOpen(jiraIssue.getTotalIssuesOpen()+1);
	    					typesOpen.add(sub.getIssueType().getId());
	    				}
	    				
	    				if(sub.getStatus().getName().equalsIgnoreCase("Closed")){
	    					jiraIssue.setTotalIssuesClose(jiraIssue.getTotalIssuesClose()+1);
	    				}
	    				
	    				if(sub.getStatus().getName().equalsIgnoreCase("In Documentation")){
	    					jiraIssue.setStatus("In Documentation");
	    					jiraIssue.setTotalIssuesOpen(jiraIssue.getTotalIssuesOpen()+1);
	    				}
	    				
	    				jiraIssue.setTotalIssues(jiraIssue.getTotalIssues()+1);
	    			}
	    			
	    			if(typesOpen.contains(JiraIssueTypes.JIRA_CODIFICAR_INTERFACE)||typesOpen.contains(JiraIssueTypes.JIRA_CODIFICAR_NEGOCIO)){
	    				jiraIssue.setStatus("Em Construção");
	    				jiraIssue.setStyle("default");
	    			}else if(typesOpen.contains(JiraIssueTypes.JIRA_RETRABALHO)){
	    				jiraIssue.setStatus("Em retrabalho");
	    				jiraIssue.setStyle("danger");
	    			}else if(typesOpen.contains(JiraIssueTypes.JIRA_TESTE_UNIDADE)){
	    				jiraIssue.setStatus("Em Teste de par");
	    				jiraIssue.setStyle("info");
	    			}else if(typesOpen.contains(JiraIssueTypes.JIRA_TESTE_INTEGRADO)){
	    				jiraIssue.setStatus("Em Teste Integrado");
	    				jiraIssue.setStyle("warning");
	    			}else if(typesOpen.contains(JiraIssueTypes.JIRA_DEV_FLUIG)||typesOpen.contains(JiraIssueTypes.JIRA_MANUAL_USUARIO)
	    					||typesOpen.contains(JiraIssueTypes.JIRA_SAMPLES_GIT)||typesOpen.contains(JiraIssueTypes.JIRA_STYLE_GUIDE)){
	    				jiraIssue.setStatus("Em Documentação");
	    				jiraIssue.setStyle("default");
	    			}else if("In Documentation".equalsIgnoreCase(jiraIssue.getStatus())){
	    				jiraIssue.setStatus("Em Homologação");
	    				jiraIssue.setStyle("primary");
	    			}else if(userStory.getStatus().getName().equalsIgnoreCase("open")){
	    				jiraIssue.setStatus("Falta fechar");
	    				jiraIssue.setStyle("success");
	    			}else{
	    				jiraIssue.setStatus("Completo!!!");
	    				jiraIssue.setStyle("success");
	    			}
	    			
	    			stories.add(jiraIssue);
	    			
	    		}
	    		
	    		
	    		//Sorting
	    		Collections.sort(stories, new Comparator<JiraStoryStatusVo>() {
	    		        public int compare(JiraStoryStatusVo  storie1, JiraStoryStatusVo  storie2)
	    		        {

	    		            return  storie1.getTotalIssuesOpen() - (storie2.getTotalIssuesOpen());
	    		        }
	    		    });
	    		
	            return this.buildSuccessResponse(stories);
	        } catch (Exception e) {
	            return this.buildErrorResponse(e);
	        }
	    }
	 
	 public static void main(String[] args){
		 JiraStoryServiceRest r = new JiraStoryServiceRest();
		 
		 r.listStorySprint("313");
	 }
}
