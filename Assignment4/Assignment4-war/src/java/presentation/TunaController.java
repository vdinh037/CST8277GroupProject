package presentation;

import entity.Tuna;
import presentation.util.JsfUtil;
import presentation.util.PaginationHelper;
import business.TunaFacadeRemote;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 * Controller for Tuna
 * 
 * @author Vuong Dinh, Parthashokbh Chauhan, Khanh Vuviet, Aksharbhavin Chauhan, Shubham Sachdeva
 * Date: 11/30/2018
 */

@Named("TunaController")
@SessionScoped
public class TunaController implements Serializable {

    /* Tuna instance */
    private Tuna current;
    /* DataModel instance */
    private DataModel items = null;
    @EJB
    private business.TunaFacadeRemote ejbFacade;
     /* PaginationHelper instance */
    private PaginationHelper pagination;
    /* Variable for selected index */
    private int selectedItemIndex;

    /*
    * Constructor
    */
    public TunaController() {}

     /*
    * Return current tuna selected
    */
    public Tuna getSelected() {
        if (current == null) {
            current = new Tuna();
            selectedItemIndex = -1;
        }
        return current;
    }

    /*
    * Return TunaFacadeRemote
    */
    private TunaFacadeRemote getFacade() {
        return ejbFacade;
    }

    /*
    * Checks if data overlow, allows paging for tuna objects
    */
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    /*
    * Prepare the list to be retrieved
    */
    public String prepareList() {
        recreateModel();
        return "List";
    }

   /*
    * Prepare the list to be retrieved and viewed
    */
    public String prepareView() {
        current = (Tuna) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
   /*
    * Prepare the list to be retrieved and create
    */
    public String prepareCreate() {
        current = new Tuna();
        selectedItemIndex = -1;
        return "Create";
    }

    /*
    * Takes the current tuna object and create it
    */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TunaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
   /*
    * Prepare the list to be retrieved and edited
    */
    public String prepareEdit() {
        current = (Tuna) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

   /*
    * Prepare the list to be retrieved and updated
    */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TunaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /*
    * Deestroy the object when not in used
    */
    public String destroy() {
        current = (Tuna) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
   /*
    * Destroy the objects and view
    */
    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

   /*
    * Perform the destroy of objects
    */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TunaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

   /*
    * Updating current tuna objects
    */
    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

   /*
    * Get the data model
    */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }
    
   /*
    * Recreate model
    */
    private void recreateModel() {
        items = null;
    }
    
   /*
    * Recreate pagination
    */
    private void recreatePagination() {
        pagination = null;
    }
    
   /*
    * Get the next item
    */
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }
    
   /*
    * Get the previous item
    */
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
   /*
    * Get the selected items
    */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }
    
   /*
    * Get the selected item
    */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    
   /*
    * Retrieves the tuna object
    * @Return id
    */
    public Tuna getTuna(java.lang.Long id) {
        return ejbFacade.find(id);
    }
    
   /*
    * Converts fields into proper type
    */
    @FacesConverter(forClass = Tuna.class)
    public static class TunaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TunaController controller = (TunaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "TunaController");
            return controller.getTuna(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Tuna) {
                Tuna o = (Tuna) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Tuna.class.getName());
            }
        }

    }

}
