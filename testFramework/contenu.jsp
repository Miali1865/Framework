<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>

    <%
        try {
            String testlist = (String) session.getAttribute("isConnected");
            out.println("Profil connecte "+testlist);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    %>

    <h5>Sprint 7 avec v&eacute;rification SINGLETON</h5>
    <form action="http://localhost:8081/testFramework/EmpAll" method="post">
        <input type="text" name="Name" placeholder="Votre nom">
        <input type="submit" value="Valider">
    </form>

    <h5>Sprint 8 avec v&eacute;rification profil</h5>
    <form action="http://localhost:8081/testFramework/sprint8" method="post">
        <input type="text" name="name" placeholder="Votre nom">
        <input type="submit" value="Valider">
    </form>

    <h5>Upload File</h5>
    <form action="http://localhost:8081/testFramework/uploadFile" method="post" enctype="multipart/form-data">
        <input type="file" name="File">
        <input type="submit" value="Valider">
    </form>
</body>
</html>
