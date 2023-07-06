<%@ page import="model.Emp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <p>Bonjour, voici votre view</p>
    <p>Votre data est : <% out.println(request.getAttribute("data"));%></p>
</body>
</html>