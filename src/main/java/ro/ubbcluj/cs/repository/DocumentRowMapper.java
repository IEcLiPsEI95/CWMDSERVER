package ro.ubbcluj.cs.repository;

import org.springframework.jdbc.core.RowMapper;
import ro.ubbcluj.cs.domain.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by cluca on 22-Jan-17.
 * Project name CWMDSERVER
 * Email: luca_corneliu2003@yahoo.com
 */
public class DocumentRowMapper implements RowMapper<Document> {
    @Override
    public Document mapRow(ResultSet resultSet, int i) throws SQLException {
        Document document = new Document();
        document.setId(resultSet.getInt("idDoc"));
        document.setBaseName(resultSet.getString("path"));
        document.setVersionDraftMinor(resultSet.getInt("versionDraftMinor"));
        document.setVersionFinRevMinor(resultSet.getInt("versiuneFinRevMinor"));
        document.setStatus(resultSet.getInt("idStatus"));
        document.setWhosNext(resultSet.getInt("nextGroup"));
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
