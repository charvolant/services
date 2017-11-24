<%--
  User: pmcneil
  Date: 15/09/14
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>NSL Export</title>
</head>

<body>
<div class="container">

  <h2>Export</h2>

  <ul>
    <g:each in="${exports}" var="export">
      <li><a href="${export.url}">export ${export.label}</a></li>
    </g:each>
  </ul>

</div>
</body>
</html>