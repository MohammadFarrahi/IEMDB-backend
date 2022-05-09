package ie.iemdb.repository;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class Repo<T, PK> {
    protected ObjectNotFoundException notFoundException;

    abstract protected String getGetElementByIdStatement();

    abstract protected void fillGetElementByIdValues(PreparedStatement st, PK id) throws SQLException;

    abstract protected String getGetAllElementsStatement();


    abstract protected T convertResultSetToDomainModel(ResultSet rs) throws SQLException, CustomException;
    abstract protected String getAddElementStatement();

    abstract protected ArrayList<T> convertResultSetToDomainModelList(ResultSet rs) throws SQLException, CustomException;

    public abstract void addElement(T newObject) throws SQLException;

    public boolean isIdValid(PK id) {
        // TODO: refactor
        return true;
    }

    public T getElementById(PK id) throws SQLException, ObjectNotFoundException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getGetElementByIdStatement());
        try {
            fillGetElementByIdValues(st, id);
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                throw notFoundException;
            }
            T result = convertResultSetToDomainModel(resultSet);
            st.close();
            con.close();
            return result;
        } catch (SQLException e) {
            st.close();
            con.close();
            System.out.println("error in Repository.find query.");
            e.printStackTrace();
            throw e;
        }
    }

    public List<T> getElementsById(List<PK> ids) throws SQLException, ObjectNotFoundException {
        List<T> result = new ArrayList<>();
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getGetElementByIdStatement());
        try {
            for (var id : ids) {
                fillGetElementByIdValues(st, id);
                ResultSet resultSet = st.executeQuery();
                if (!resultSet.next()) {
                    st.close();
                    con.close();
                    throw notFoundException;
                }
                result.add(convertResultSetToDomainModel(resultSet));
            }
            st.close();
            con.close();
        } catch (SQLException e) {
            st.close();
            con.close();
            System.out.println("error in Repository.find query.");
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    public List<T> getAllElements() throws SQLException {
        List<T> result = new ArrayList<>();
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getGetAllElementsStatement());
        try {
            ResultSet resultSet = st.executeQuery();
            result = convertResultSetToDomainModelList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (SQLException e) {
            st.close();
            con.close();
            System.out.println("error in Repository.findAll query.");
            e.printStackTrace();
            throw e;
        }
    }

    protected void initTable(String createTableSql) {
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement createTableStatement = con.prepareStatement(createTableSql);
            createTableStatement.executeUpdate();
            createTableStatement.close();
            con.close();
        } catch (SQLException e) {
            //ignore
        }
    }

    protected ResultSet executeQuery(String sql, List<String> fillValues) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(sql);
        fillValues(st, fillValues);
        var result = st.executeQuery();
        st.close();
        con.close();
        return result;
    }

    protected int executeUpdate(String sql, List<String> fillValues) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(sql);
        fillValues(st, fillValues);
        var result = st.executeUpdate();
        st.close();
        con.close();
        return result;
    }

    protected void fillValues(PreparedStatement st, List<String> values) throws SQLException {
        int valuePosition = 1;
        for (var value : values) {
            st.setString(valuePosition, value);
            valuePosition++;
        }
    }
}