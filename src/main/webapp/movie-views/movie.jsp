<%@page import="ie.app.film.Film"%>
<%@page import="ie.generic.view.HtmlUtility"%>


<%
  Film movie = (Film)request.getAttribute("movie");

%>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <title>Movie</title>
  <style>
    li,
    td,
    th {
      padding: 5px;
    }
  </style>
</head>

<body>
  <jsp:include page="/header.jsp" />

  <ul>
    <li id="name">name: <%= movie.getName()%></li>
    <li id="summary"><%= movie.getSummary()%></li>
    <li id="releaseDate">releaseDate: <%= movie.getReleaseDate()%></li>
    <li id="director">director: <%= movie.getDirector()%></li>
    <li id="writers">writers: <%= HtmlUtility.getCSVFromList(movie.getWriters())%></li>
    <li id="genres">genres: <%= HtmlUtility.getCSVFromList(movie.getGenres())%></li>
    <li id="imdbRate">imdb Rate: <%= movie.getImdbRate()%></li>
    <li id="rating">rating: <%= movie.getAverageRating()%></li>
    <li id="duration">duration: <%= movie.getDuration()%> minutes</li>
    <li id="ageLimit">ageLimit: <%= movie.getAgeLimit()%></li>
  </ul>

</body>

</html>