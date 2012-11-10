package com.gentics.kitchenoffice.webapp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.DiscoveryNavigator;

import com.gentics.kitchenoffice.webapp.view.HomeView;
import com.gentics.kitchenoffice.webapp.view.SecurityViewChangeListener;
import com.gentics.kitchenoffice.webapp.view.StandardErrorView;
import com.gentics.kitchenoffice.webapp.view.layout.MainLayout;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Component
@Scope("request")
@Theme("runo")
@Title("KitchenOffice WebApp")
public class WebAppUI extends UI
{
	
	@Autowired
	private MainLayout layout;
	
	@Autowired
	private SecurityViewChangeListener viewChangeListener;
	
	private DiscoveryNavigator navigator;
	
	private static Logger log = Logger.getLogger(WebAppUI.class);

    @Override
    protected void init(VaadinRequest request) {
        
    	log.debug("initializing WebApp instance");
    	
        setContent(layout);
        setSizeFull();
        
        initializeNavigator();
        
    }

	private void initializeNavigator() {
		
		navigator = new DiscoveryNavigator(this, layout.getPanel());
        
        navigator.addViewChangeListener(viewChangeListener);
        
        navigator.setErrorView(StandardErrorView.class);

        if(Page.getCurrent().getFragment().isEmpty()) {
        	// Navigate to standard view
            navigator.navigateTo(HomeView.NAME);
        } else {
        	// Navigate to view specified by fragment
        	navigator.navigateTo(Page.getCurrent().getFragment());
        }
	}
   

}
