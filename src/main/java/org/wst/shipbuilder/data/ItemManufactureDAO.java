package org.wst.shipbuilder.data;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wst.shipbuilder.data.entities.BOMEntry;
import org.wst.shipbuilder.data.entities.InvType;
import org.wst.shipbuilder.data.entities.ItemPriceCacheEntry;
import org.xml.sax.SAXException;


public class ItemManufactureDAO {

	static Logger log = Logger.getLogger(ItemManufactureDAO.class.getName());
	 private JdbcTemplate jdbcTemplate;
	 private InvTypeDAO invTypeDAO;
	 private ItemPriceCacheRepository priceCache;
	 
	 @Autowired
	 public void setPriceCache(ItemPriceCacheRepository repo) {
		 this.priceCache = repo;
	 }
	 
	 public void refreshPrices() {
		 
		 Map<Long, ItemPriceCacheEntry> entries = new HashMap<Long, ItemPriceCacheEntry>();
		 for(ItemPriceCacheEntry entry : priceCache.findAll()) {
			 entries.put(entry.getTypeId(), entry);
		 }
		 List<InvType> ships = invTypeDAO.findAllShips();
		 SortedMap<Long, InvType> thingsToFind = new TreeMap<Long,InvType>();
		 for(InvType ship : ships) {
			 thingsToFind.put(ship.getId(),ship);
			 try {
				 InvType blueprint = invTypeDAO.findType(invTypeDAO.findBlueprint(ship.getId()));
				 List<BOMEntry> boms = findMaterials(blueprint);
				 log.info("Blueprint " + blueprint.getName() + " has requirements of :");
				 StringBuilder sb = new StringBuilder();
				 for(BOMEntry b : boms) {
					 thingsToFind.put(b.getId(), b);
					 sb.append(",").append(b.getId());
				 }
				 log.info(sb.toString());
			 } catch (EmptyResultDataAccessException e) {
				 log.warning("Could not find blueprint for ship " + ship.getName());
			 }
		 }
		 Set<InvType> items = new HashSet<InvType>(thingsToFind.values());
		 log.info("Going to look up " + items.size() + " items");
		 for (InvType t : items) {
			 log.info("going to look for inv type " + t.getId());
		 }
		 List<Long> regions = new ArrayList<Long>();
	     regions.add(10000042L);
		 List<InvType> batch = new ArrayList<InvType>();
		 for(InvType item : items) {
			 batch.add(item);
			 if(batch.size() == 10) {
				 lookupPricesFromWeb(batch, regions);
				 batch.clear();
			 }	 
		 }
		 
		 
		 // Catch any stragglers
		 lookupPricesFromWeb(batch, regions);
		 
		 for(InvType item : items) {
			 ItemPriceCacheEntry ipce = entries.get(item.getId());
			 if (ipce != null) {
				 ipce.setBuyPrice(item.getPrice().getBuyPrice());
				 ipce.setSellPrice(item.getPrice().getSellPrice());
				 priceCache.save(ipce);
			 } else {
				 ipce = new ItemPriceCacheEntry(item.getId(), item.getPrice().getSellPrice(), item.getPrice().getBuyPrice());
				 priceCache.save(ipce);
			 }
		 }
		 
	 }
	    @Autowired
	    public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	    }
		@Autowired
		public void setInvTypeDAO(InvTypeDAO invTypeDAO) {
			this.invTypeDAO = invTypeDAO;
		}		
		public List<BOMEntry> findMaterials(InvType type) {
			String qry = "select \"typeName\",\"materialTypeID\",quantity from \"industryActivityMaterials\" as iam join \"invTypes\" on (iam.\"materialTypeID\"=\"invTypes\".\"typeID\") where \"activityID\"=1 and iam.\"typeID\"=?";
			List<BOMEntry> materials = this.jdbcTemplate.query(
			        qry,
			        new Object[]{type.getId()},
			        new RowMapper<BOMEntry>() {
			            public BOMEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
			            	BOMEntry tm = new BOMEntry();
			            	tm.setId(rs.getLong("materialTypeID"));
			            	tm.setName(rs.getString("typeName"));
			            	tm.setQuantity(rs.getLong("quantity"));
			                return tm;
			            }
			        });
			return materials; 
		}
		public void lookupPrices(Collection<? extends InvType> items) {
			for (InvType t : items) {
				ItemPriceCacheEntry e = priceCache.findByTypeId(t.getId());
				if (e != null) {
					t.getPrice().setBuyPrice(e.getBuyPrice());
					t.getPrice().setSellPrice(e.getSellPrice());
				}
			}
		}
		private void lookupPricesFromWeb(Collection<? extends InvType> items, List<Long> regions) {
			log.info("looking up prices");
			Map<Long, Double> sellPrices = lookupItemPrices(items, 's', regions);
			Map<Long, Double> buyPrices = lookupItemPrices(items, 'b', regions);
			for (InvType i : items) {
				if (sellPrices.containsKey(i.getId())) {
					i.getPrice().setSellPrice(sellPrices.get(i.getId()));
				} else {
					log.info("Could not find sell price for id=" + i.getId());
				}
				i.getPrice().setBuyPrice(buyPrices.get(i.getId()));
			}
/*			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		private Map<Long, Double> lookupItemPrices(Collection<? extends InvType> items, char buySell, List<Long> regions) {
			Map<Long,Double> result = new HashMap<Long, Double>();
			final String baseURL = "http://eve-marketdata.com/api/item_prices2.xml?char_name=demo&type_ids=";
			String dataURL = baseURL;
			StringBuilder sb = new StringBuilder();

			String loopDelim="";
			for (InvType i : items) {
				sb.append(loopDelim);
				sb.append(i.getId());
				loopDelim = ",";
			}
			
			dataURL += sb.toString();
			
			sb = new StringBuilder();
			loopDelim="";
			for (long id : regions) {
				sb.append(loopDelim);
				sb.append(id);
				loopDelim = ",";
			}
			dataURL += "&region_ids=";
			dataURL += sb.toString();
			dataURL += "&buysell=";
			dataURL += buySell;
			
			
			
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(dataURL);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/emd/result/rowset/row");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				String price = n.getAttributes().getNamedItem("price").getNodeValue();
				String idStr = n.getAttributes().getNamedItem("typeID").getNodeValue();
				Double d = new Double(price);
				Long id = new Long(idStr);
				result.put(id, d);
				log.info("Found price of " + d + " for id " + id);
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
			
			return result;
		}
}
