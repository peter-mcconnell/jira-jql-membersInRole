package com.example.plugins.tutorial.jira.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.JiraDataType;
import com.atlassian.jira.JiraDataTypes;
import com.atlassian.jira.jql.operand.QueryLiteral;
import com.atlassian.jira.jql.query.QueryCreationContext;
import com.atlassian.jira.plugin.jql.function.AbstractJqlFunction;
import com.atlassian.jira.util.MessageSet;
import com.atlassian.jira.util.NotNull;
import com.atlassian.query.clause.TerminalClause;
import com.atlassian.query.operand.FunctionOperand;
import com.google.common.collect.Iterables;

import com.atlassian.crowd.embedded.api.User;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleActors;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.util.MessageSet;
import com.atlassian.jira.util.MessageSetImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Retrieves a list of users that match a given project and role
 */
public class MemberRolesFunction extends AbstractJqlFunction
{
    private static final Logger log = LoggerFactory.getLogger(MemberRolesFunction.class);
    private final ProjectManager projectManager;
    private final ProjectRoleManager projectRoleManager;
    public MemberRolesFunction( 
        ProjectManager projectManager, 
        ProjectRoleManager projectRoleManager)
    {
        this.projectManager = projectManager;
        this.projectRoleManager = projectRoleManager;
    }
    public MessageSet validate(User searcher, FunctionOperand operand, TerminalClause terminalClause)
    {
        MessageSet messages = new MessageSetImpl();
        messages.addMessageSet(validateNumberOfArgs(operand, 2));
        if (messages.hasAnyErrors()) {
            return messages;
        }
        final String projectName = operand.getArgs().get(0);
        final String projectRoleName = operand.getArgs().get(1);
        ProjectRole projectRole = projectRoleManager.getProjectRole(projectRoleName);
        if (projectRole == null) {
            messages.addErrorMessage("Project role name is invalid");
        }
        Project project = projectManager.getProjectObjByKey(projectName);
        if (project == null) {
            messages.addErrorMessage("Project key is invalid");
        }
        return messages;
    }
    public List<QueryLiteral> getValues(QueryCreationContext queryCreationContext, FunctionOperand operand, TerminalClause terminalClause)
    {
        if (!operand.getArgs().isEmpty()) {
            final String projectName = operand.getArgs().get(0);
            final String projectRoleName = operand.getArgs().get(1);

            Project project = projectManager.getProjectObjByKey(projectName);
            ProjectRole projectRole = projectRoleManager.getProjectRole(projectRoleName);
            ProjectRoleActors projectRoleActors = projectRoleManager.getProjectRoleActors(projectRole, project);

            final Set<QueryLiteral> usernames = new LinkedHashSet<QueryLiteral>();
            Set<User> users = projectRoleActors.getUsers();
            for(User user : users) {
                usernames.add(new QueryLiteral(operand, user.getName()));
            }
            return new ArrayList<QueryLiteral>(usernames);
        }
        return Collections.emptyList();
    }
    public int getMinimumNumberOfExpectedArguments()
    {
        return 2;
    }
    public JiraDataType getDataType()
    {
        return JiraDataTypes.USER;
    }
}