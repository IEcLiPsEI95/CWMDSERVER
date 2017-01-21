package ro.ubbcluj.cs.repository;

import org.springframework.jdbc.core.RowMapper;
import ro.ubbcluj.cs.domain.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by cluca on 21-Jan-17.
 * Project name CWMDSERVER
 * Email: luca_corneliu2003@yahoo.com
 */
public class SignDocumentRowMapper implements RowMapper<Document> {
    @Override
    public Document mapRow(ResultSet resultSet, int i) throws SQLException {
        Document document = new Document();
        document.setWhosNext(resultSet.getInt("currentSigningUser"));
        String groupOrder = resultSet.getString("groupOrder");
        String[] split = groupOrder.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String aSplit : split) {
            integers.add(Integer.valueOf(aSplit));
        }
        document.setSignOrder(integers);
        return document;
    }
}
