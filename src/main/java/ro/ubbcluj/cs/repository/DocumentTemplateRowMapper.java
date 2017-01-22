package ro.ubbcluj.cs.repository;

import org.springframework.jdbc.core.RowMapper;
import ro.ubbcluj.cs.domain.DocumentTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by cluca on 22-Jan-17.
 * Project name CWMDSERVER
 * Email: luca_corneliu2003@yahoo.com
 */
public class DocumentTemplateRowMapper implements RowMapper<DocumentTemplate> {
    @Override
    public DocumentTemplate mapRow(ResultSet resultSet, int i) throws SQLException {
        DocumentTemplate documentTemplate = new DocumentTemplate();
        documentTemplate.setId(resultSet.getInt("id"));
        documentTemplate.setName(resultSet.getString("name"));
        documentTemplate.setPath(resultSet.getString("docTemplatePath"));
        String groupOrder = resultSet.getString("groupOrder");
        String[] split = groupOrder.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String aSplit : split) {
            integers.add(Integer.valueOf(aSplit));
        }
        documentTemplate.setFlow(integers);
        return documentTemplate;
    }
}
