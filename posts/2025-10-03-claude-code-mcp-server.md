---
title: Add an MCP Server to Claude Code
summary: You can connect [Claude Code to tools via MCP]([%external_url]). You can use the `claude mcp add` command to add an MCP server.
date_published: 2025-10-03T15:35:18+01:00
keywords: mcp,claude
external_url: https://docs.claude.com/en/docs/claude-code/mcp
---

# [%title]

[%summary]

```
claude mcp add --transport http micronautfun https://micronaut.fun/mcp
```

The previous command adds an entry in the `mcpServers` of the project: 

```json
"mcpServers": {
   "micronautfun": {
     "type": "http",
     "url": "https://micronaut.fun/mcp"
   }
},
```

These commands modify the `vi $HOME/.claude.json` configuration file.

You can list your project MCP servers with the `claude mcp list` command.