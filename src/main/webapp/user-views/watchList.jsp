<%@page import="java.util.List"%>
<%@page import="ie.app.film.Film"%>

<%
    List<Film> watchlistMovies = (List<Film>)request.getAttribute("movies");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Watch List</title>
</head>
<body>
    <jsp:include page="/header.jsp" />
    <table>
        <tr>
            <th>Movie</th>
            <th>releaseDate</th> 
            <th>director</th> 
            <th>genres</th> 
            <th>imdb Rate</th> 
            <th>rating</th> 
            <th>duration</th> 
            <th></th>
            <th></th>
        </tr>
        <%
            for (int idx = 0; idx < watchlistMovies.size(); idx++){
                Film film = watchlistMovies.get(idx);
        %>
        <tr>
            <td><%= film.getName()%></td>
            <td><%= film.getReleaseDate()%></td>
            <td><%= film.getDirector()%></td>
            <td>hello, folan</td>
            <td><%= film.getImdbRate()%></td>
            <td><%= film.getAverageRating()%></td>
            <td><%= film.getDuration()%></td>
            <td><a href="/movies/<%= film.getId() %>">Link</a></td>
            <td>        
                <form action="" method="POST" >
                    <input id="form_movie_id" type="hidden" name="movie_id" value="<%= film.getId() %>">
                    <button type="submit">Remove</button>
                </form>
            </td>
        </tr>
            <%}%>
    </table>
</body>
</html>