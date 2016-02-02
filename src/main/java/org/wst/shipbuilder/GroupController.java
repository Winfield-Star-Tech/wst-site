package org.wst.shipbuilder;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Configuration
public class GroupController {
	@Autowired  String myBean;

	 private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	@RequestMapping("/group")
	public String greeting(@RequestParam(value="id", required=false, defaultValue="26") String id, Model model) {
        model.addAttribute("id", id);
        String groupName = this.jdbcTemplate.queryForObject(
                "select \"groupName\" from \"invGroups\" where \"groupID\" = ?",
                new Object[]{new Long(id)}, String.class);
        model.addAttribute("groupName", groupName);
        return "group";
    }
	
	
	
}
