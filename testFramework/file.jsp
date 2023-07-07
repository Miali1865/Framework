<%@ page import="etu1865.framework.FileUpload" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <p>La taille du fichier est 
    <%
        FileUpload file = (FileUpload) request.getAttribute("data");
        byte[] fileBytes = file.getFile();
        out.println(fileBytes);
    %>
    <p>
</body>
</html>