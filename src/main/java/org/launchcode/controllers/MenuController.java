package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value= "", method = RequestMethod.GET)
    public String index(Model model){

      //  Below comments are just to test if these handlers are working with views
      //  Menu menu1;
      //  menuDao.save(menu1 = new Menu("food"));
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";

    }

    @RequestMapping(value="view", method = RequestMethod.GET)
    public String menulink(Model model, int id){

        Menu menu = menuDao.findOne(id);
        model.addAttribute("menu", menu);
        model.addAttribute("title", "Menu #" + id);

        return "menu/view-menu";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model){
        model.addAttribute("title", "Add a Menu");
        model.addAttribute(new Menu());

        return "menu/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddMenuForm(@ModelAttribute @Valid Menu newMenu,
                                     Errors errors, Model model){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);
        return "redirect:/menu/view?id=" + newMenu.getId();
    }

    @RequestMapping(value="add-item", method = RequestMethod.GET)
    public String addItem(Model model, int id){

        Menu current_menu = menuDao.findOne(id);
        model.addAttribute(new AddMenuItemForm());
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Add item to menu: " + current_menu.getName());
        model.addAttribute("menu", current_menu);

        return "menu/add-item";
    }

    @RequestMapping(value="add-item", method = RequestMethod.POST)
    public String processaddItem(Model model, @Valid AddMenuItemForm itemForm, Errors errors, Menu menu){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "menu/add-item";
        }

        Cheese currentCheese = cheeseDao.findOne(itemForm.getCheeseId());
        menu = menuDao.findOne(itemForm.getMenuId());

        menu.addItem(currentCheese);

        menuDao.save(menu);

        return "redirect:/menu/view?id=" + menu.getId();

    }

}
