package org.wst.shipbuilder.data;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wst.shipbuilder.data.entities.InvType;
import org.xml.sax.SAXException;

public class InvTypeDAO {
	static Logger log = Logger.getLogger(ItemManufactureDAO.class.getName());
	 private JdbcTemplate jdbcTemplate;

	    @Autowired
	    public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	    }

	    public Long findBlueprint(Long typeID) {
	    	Long blueprintID = this.jdbcTemplate.queryForObject(
	    			"select \"typeID\" from \"industryActivityProducts\" where \"activityID\"=1 and"
	    			+ " \"productTypeID\"=?", 
	    			new Object[] {typeID}, Long.class);
	    	return blueprintID;
	    }

	    public InvType findType(Long id) {
			InvType it = this.jdbcTemplate.queryForObject(
			        "select \"typeName\"  as typename, \"typeID\" id from \"invTypes\" where \"typeID\" = ?",
			        new Object[]{id},
			        new RowMapper<InvType>() {
			            public InvType mapRow(ResultSet rs, int rowNum) throws SQLException {
			            	InvType it = new InvType();
			            	it.setId(rs.getLong("id"));
			            	it.setName(rs.getString("typename"));

			                return it;
			            }
			        });
			return it;
		}
	    
	    public List<InvType> findAllShips() {
	    	final String qry = "select t.* from \"invGroups\" g, \"invTypes\" t where g.\"categoryID\"=6 and g.\"groupID\" = t.\"groupID\"";
	    	List<InvType> ships = new ArrayList<InvType>();
	    	ships = this.jdbcTemplate.query(
			        qry,
			        new Object[]{},
			        new RowMapper<InvType>() {
			            public InvType mapRow(ResultSet rs, int rowNum) throws SQLException {
			            	InvType it = new InvType();
			            	it.setId(rs.getLong("typeID"));
			            	it.setName(rs.getString("typeName"));

			                return it;
			            }
			        });
	    	
	    	return ships;
	    }
		

		


}
