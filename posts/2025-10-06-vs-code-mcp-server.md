---
title: Add an MCP Server to VS Code
summary: To register a MCP Server in VS Code open the command palette with (CMD + Shift + P) and search for "Add MCP"
date_published: 2025-10-03T15:35:18+01:00
keywords: mcp,idea,copilot
---

# [%title]

[%summary]

![](https://images.sergiodelamo.com/vs-code-preference-add-mcp-server.png)

Select your MCP Server transport (STDIO or HTTP)

![](https://images.sergiodelamo.com/vs-code-mcp-server-transport-selection.png)

If HTTP, enter your MCP Server URL.

![](https://images.sergiodelamo.com/vs-code-mcp-server-enter-url.png)

Select whether you want to install the MCP Server globally or for the current workspace only.

![](https://images.sergiodelamo.com/vs-code-select-global-or-workspace.png)

Enter your MCP Server in the `mcp.json` configuration file. 

The following example shows an MCP Server which uses Streamable HTTP transport.

```json
{
  "servers": {
    "micronautfun": {
      "url": "https://micronaut.fun/mcp",
      "type": "http"
    }
  },
  "inputs": []
}
```

You should see your MCP listed as installed.

![](https://images.sergiodelamo.com/vs-code-mcp-servers-installed-micronaut-fun.png)