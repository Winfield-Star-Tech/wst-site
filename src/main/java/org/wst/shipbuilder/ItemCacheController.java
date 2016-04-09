package org.wst.shipbuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wst.shipbuilder.data.ItemManufactureDAO;


@Controller
public class ItemCacheController {
	private ItemManufactureDAO mfgDAO;
	 
	 @Autowired
	 public void setItemManufactureDAO(ItemManufactureDAO dao) {
		 this.mfgDAO = dao;
	 }
	 
	 @RequestMapping("/cache/refresh")
		public String refreshCache( Model model) {
		 mfgDAO.refreshPrices();
		 return "cacheStatus";
	 }
	 @RequestMapping("/cache/status")
		public String cacheStatus( Model model) {
		 return "cacheStatus";
	 }
}
