package com.fluig.statistics.rest;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.User;
import com.atlassian.jira.rest.client.domain.Worklog;
import com.fluig.statistics.jira.JiraRestManager;
import com.fluig.statistics.jira.JiraUtils;
import com.totvs.technology.wcm.sdk.rest.WCMRest;

/**
 * Jira - TimeService
 * 
 * @author danilo.ganzella
 *
 */
@Path("/time")
public class JiraTimeServiceRest extends WCMRest {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listStorySprint/{sprintId}")
	public Response listGroupTime(@PathParam("groupId") String sprintId) {

		try {
			JiraRestManager jiraManager = JiraUtils.getJiraManager();

		} catch (URISyntaxException e) {
			return this.buildErrorResponse(e);
		}

		return null;
	}

	private Map<String, Integer> getYesterdayTime(String groupId)
			throws URISyntaxException, InterruptedException,
			ExecutionException, TimeoutException {

		DateTime today = new DateTime().withTimeAtStartOfDay();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(today.getMillis());

		cal.add(Calendar.DATE, -1);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			cal.add(Calendar.DATE, -2);
		}

		String formatedDate = dateFormat.format(cal.getTime());
		String JIRA_TIME_QUERY = "(key in workedIssues(\"" + formatedDate
				+ "\", \"" + formatedDate + "\", \"" + groupId + "\"))";

		JiraRestManager jiraManager = JiraUtils.getJiraManager();

		List<String> timeIssues = jiraManager.searchIssues(JIRA_TIME_QUERY);

		Map<String, Integer> users = new HashMap<String, Integer>();

		for (String issue : timeIssues) {

			Issue userStory = jiraManager.getIssueInfo(issue);

			for (Worklog work : userStory.getWorklogs()) {

				User userInfo = jiraManager.getUserInfo(work.getAuthor()
						.getName());
				
				if (userInfo.getGroups().getItems().toString()
						.contains(groupId)) {
					if (users.get(work.getAuthor().getName()) == null) {
						users.put(work.getAuthor().getName(),
								Integer.valueOf(0));
					}

					if (dateFormat.format(
							new Date(work.getStartDate().getMillis()))
							.equalsIgnoreCase(formatedDate)) {
						users.put(
								work.getAuthor().getName(),
								Integer.valueOf(users.get(
										work.getAuthor().getName()).intValue()
										+ work.getMinutesSpent()));
					}
				}

			}
		}

		return users;
	}

	public static void main(String[] args) throws URISyntaxException,
	InterruptedException, ExecutionException, TimeoutException {
		
		//http://jira.totvs.com/rest/api/2/group?groupname=developers&expand=users
		
		JiraTimeServiceRest r = new JiraTimeServiceRest();

		System.out.println(r.getYesterdayTime("fluig-social").toString());
	}
}
