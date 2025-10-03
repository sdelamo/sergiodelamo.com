---
title: Add an MCP Server to Claude Desktop
summary: To register a MCP Server in Claude Desktop, click on `Settings`, click the `Developer`, and click `Edit Config`
date_published: 2025-10-03T15:35:18+01:00
keywords: mcp,idea,copilot
---

# [%title]

[%summary]

![](https://images.sergiodelamo.com/claude-desktop-mcp-server.png)

Enter your MCP Server in the `claude_desktop_config.json` configuration file. The following example shows an MCP Server packaged as a FAT jar which uses STDIO transport. 

```json
{
  "mcpServers": {
    "diskspace": {
      "command": "java",
      "args": ["-jar", "/Users/sdelamo/bin/diskspace-0.1-all.jar"]
    }
  }
}
```