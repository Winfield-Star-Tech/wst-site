package org.wst.shipbuilder;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Controller
@Configuration
public class ItemManufacture {

	static Logger log = Logger.getLogger(ItemManufacture.class.getName());
	 private JdbcTemplate jdbcTemplate;

	    @Autowired
	    public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	    }
	    Long findBlueprint(Long typeID) {
	    	Long blueprintID = this.jdbcTemplate.queryForObject(
	    			"select \"typeID\" from \"industryActivityProducts\" where \"activityID\"=1 and"
	    			+ " \"productTypeID\"=?", 
	    			new Object[] {typeID}, Long.class);
	    	return blueprintID;
	    }
		InvType findType(Long id) {
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
		
		List<TypeMaterial> findMaterials(InvType type) {
			String qry = "select \"typeName\",\"materialTypeID\",quantity from \"industryActivityMaterials\" as iam join \"invTypes\" on (iam.\"materialTypeID\"=\"invTypes\".\"typeID\") where \"activityID\"=1 and iam.\"typeID\"=?";
			List<TypeMaterial> materials = this.jdbcTemplate.query(
			        qry,
			        new Object[]{type.getId()},
			        new RowMapper<TypeMaterial>() {
			            public TypeMaterial mapRow(ResultSet rs, int rowNum) throws SQLException {
			            	TypeMaterial tm = new TypeMaterial();
			            	tm.setMaterialId(rs.getLong("materialTypeID"));
			            	tm.setMaterialName(rs.getString("typeName"));
			            	tm.setQuantity(rs.getLong("quantity"));
			                return tm;
			            }
			        });
			return materials; 
		}
		
		List<TypeMaterial> lookupPrices(List<TypeMaterial> materials) {
			log.info("looking up prices");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse("http://eve-marketdata.com/api/item_prices2.xml?char_name=demo&type_ids=34,12068,35,36&region_ids=10000002&buysell=s");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/emd/result/rowset/row");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				String price = n.getAttributes().getNamedItem("price").getNodeValue();
				log.info("Found price of " + price);
			}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE, "Error", e);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE, "Error", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE, "Error", e);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE, "Error", e);
			}
			return materials;
		}
		
		@RequestMapping("/buildItem")
		public String buildItem(@RequestParam(value="id", required=false, defaultValue="26") String id, Model model) {
	        model.addAttribute("id", id);
	        InvType it = findType(new Long(id));
	        model.addAttribute("type", it);
	        InvType blueprint = findType(findBlueprint(it.getId()));
	        List<TypeMaterial> materials = findMaterials(blueprint);
	        materials = lookupPrices(materials);
	        model.addAttribute("blueprint", blueprint);
	        model.addAttribute("materials", materials);
	        return "buildDetails";
	    }
	String qry ="select typeName,materialTypeID,quantity from industryActivityMaterials iam join invTypes on (iam.materialTypeID=invTypes.typeID) where activityid=1 and iam.typeid=1320";
}
