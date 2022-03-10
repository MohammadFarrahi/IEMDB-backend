<%@page import="ie.util.types.Constant"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <form action="<%=Constant.URLS.ServLet.LOGIN_USER%>" method="POST">
        <label>Email:</label>
        <input required type="emil" name="<%=Constant.FormInputNames.USER_ID%>" value="">
        <button type="submit">Login</button>
    </form>
</body>
</html>