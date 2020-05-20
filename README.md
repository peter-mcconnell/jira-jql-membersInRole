JIRA Plugin | Members in Role
=============================

This plugin was designed to allow you to use a projects role as a search parameter.

This has been built to work on JIRA 6.1.7

Syntax
------

membersInRole(`<PROJECT_KEY>`, `<PROJECT_ROLE>`)

- PROJECT_KEY (String). The KEY for the project that the role exists in
- PROJECT_ROLE (String). The ROLE that the user exists in

Example JQL Usage
------

`project = "BETA" AND assignee IN membersInRole("BETA", "Developers")`

JIRA Plugin creation output
---------------------------

You have successfully created an Atlassian Plugin!

Here are the SDK commands you'll use immediately:

* atlas-run   -- installs this plugin into the product and starts it on localhost
* atlas-debug -- same as atlas-run, but allows a debugger to attach at port 5005
* atlas-cli   -- after atlas-run or atlas-debug, opens a Maven command line window:
                 - 'pi' reinstalls the plugin into the running product instance
* atlas-help  -- prints description for all commands in the SDK

Full documentation is always available at:

https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK
