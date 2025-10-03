---
title: Add an MCP Server to GitHub Copilot in IntelliJ IDEA
summary: To register a MCP Server in GitHub Copilot for IntelliJ IDEA, change to `Agent` model, click the `Tools` icon and click `Add More Tools...`
date_published: 2025-10-03T15:35:18+01:00
keywords: mcp,idea,copilot
---

# [%title]

[%summary]

![](https://images.sergiodelamo.com/intellij-idea-copilot-mcp-server.png)

Enter your MCP Server in the `mcp.json` configuration file. The following example shows an MCP Server packaged as a FAT jar which uses STDIO transport. 

```json
{
	"servers": {
	  "diskspace": {
		"type": "stdio",
		"command": "java",
		"args": ["-jar", "/Users/sdelamo/bin/diskspace-0.1-all.jar"]
	  }

	}
}
```