<%--
  Created by IntelliJ IDEA.
  User: Dmitry
  Date: 30.07.2021
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Upload</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>
    <h2>Upload image</h2>
    <div class="form">

        <form method="post" enctype="multipart/form-data" action="<c:url value="/upload?id=${id}" />">
            Choose a file: <input type="file" name="file"/>
            <br/>
            <input type="submit" value="Upload"/>
        </form>

</body>
</html>
