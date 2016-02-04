package org.wst.shipbuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wst.shipbuilder.data.InvTypeDAO;
import org.wst.shipbuilder.data.ItemManufactureDAO;
import org.wst.shipbuilder.data.entities.BOMEntry;
import org.wst.shipbuilder.data.entities.InvType;
import org.wst.shipbuilder.data.entities.ShipBOM;

@Controller

public class ItemManufactureController {
	static Logger log = Logger.getLogger(ItemManufactureController.class.getName());
	private ItemManufactureDAO itemManufactureDAO;
	private InvTypeDAO invTypeDAO;

	@Autowired
	public void setItemManufactureDAO(ItemManufactureDAO itemManufactureDAO) {
		this.itemManufactureDAO = itemManufactureDAO;
	}

	@Autowired
	public void setInvTypeDAO(InvTypeDAO invTypeDAO) {
		this.invTypeDAO = invTypeDAO;
	}

	@RequestMapping("/buildItem")
	public String buildItem(@RequestParam(value="id", required=false, defaultValue="26") String id, Model model) {
        model.addAttribute("id", id);
        InvType it = invTypeDAO.findType(new Long(id));
        model.addAttribute("type", it);
        InvType blueprint = invTypeDAO.findType(invTypeDAO.findBlueprint(it.getId()));
        List<BOMEntry> materials = itemManufactureDAO.findMaterials(blueprint);
        List<Long> regions = new ArrayList<Long>();
        regions.add(10000042L);
        List<InvType> items = new ArrayList<InvType>();
        items.add(it);
        for (InvType b : materials) {
        	items.add(b);
        }
        itemManufactureDAO.lookupPrices(items, regions);
        double totalCost = 0;
        for (BOMEntry b : materials) {
        	totalCost += b.getQuantity() * b.getPrice().getBuyPrice();
        }
        double profit = it.getPrice().getSellPrice() - totalCost;
        double margin = 100.0 * (profit / totalCost);
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("profit", profit);
        model.addAttribute("margin", margin);
        model.addAttribute("ship", it);
        model.addAttribute("blueprint", blueprint);
        model.addAttribute("materials", materials);
        model.addAttribute("region", "metropolis");
        return "buildDetails";
    }
	
	@RequestMapping("/buildItem/all")
	public String buildAllItems( Model model) {
		List<InvType> ships = invTypeDAO.findAllShips();
		List<Long> regions = new ArrayList<Long>();
        regions.add(10000042L);
        Set<InvType> items = new HashSet<InvType>();
        List<ShipBOM> shipBOMs = new ArrayList<ShipBOM>();
        int count = 0;
		for (InvType t : ships) {
			
			try {
			InvType blueprint = invTypeDAO.findType(invTypeDAO.findBlueprint(t.getId()));
			if(count++ > 50) break;
			items.add(t);
			List<BOMEntry> materials = itemManufactureDAO.findMaterials(blueprint);
	        for (InvType b : materials) {
	        	items.add(b);
	        }		
	        shipBOMs.add(new ShipBOM(t,blueprint, materials));
			} catch (EmptyResultDataAccessException e) {
				log.severe(e.getLocalizedMessage());
			}
		}
		
		itemManufactureDAO.lookupPrices(items, regions);
        for (ShipBOM s : shipBOMs) {
        	double buildCost = 0;
        	for (BOMEntry be : s.getMaterials()) {
        		buildCost += (be.getQuantity() * be.getPrice().getSellPrice());
        	}
        	s.setBuildCost(buildCost);
        }
        model.addAttribute("ships", shipBOMs);
		return "buildAll";
	}

}
