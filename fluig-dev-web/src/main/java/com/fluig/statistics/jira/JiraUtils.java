package com.fluig.statistics.jira;

import java.net.URI;
import java.net.URISyntaxException;

import com.fluig.statistics.constants.JiraConstants;

public class JiraUtils {

	public static JiraRestManager getJiraManager() throws URISyntaxException{
		return new JiraRestManager(new URI(JiraConstants.JIRA_URL), JiraConstants.JIRA_LOGIN, JiraConstants.JIRA_PASS);
	}
}
