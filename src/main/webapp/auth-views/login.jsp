<%@page import="ie.util.types.Constant"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <form action="<%=Constant.URLS.LOGIN_USER%>" method="POST">
        <label>Email:</label>
        <input type="text" name="email" value="">
        <button type="submit">Login</button>
    </form>
</body>
</html>