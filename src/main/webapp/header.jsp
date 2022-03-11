  <%@page import="ie.Iemdb"%>
  <%@page import="ie.util.types.Constant"%>


  <a href="<%=Constant.URLS.ROOT%>">Home</a>
  <p id="email">email: <%= Iemdb.loggedInUser.getId()%></p>