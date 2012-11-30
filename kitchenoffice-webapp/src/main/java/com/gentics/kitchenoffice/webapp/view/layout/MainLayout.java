package com.gentics.kitchenoffice.webapp.view.layout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import ru.xpoft.vaadin.KitchenOfficeNavigator;
import ru.xpoft.vaadin.VaadinView;

import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.gentics.kitchenoffice.webapp.view.component.HTMLLabel;
import com.gentics.kitchenoffice.webapp.view.util.KitchenOfficeViewInterface;
import com.gentics.kitchenoffice.webapp.view.util.MenuEntrySortOrder;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component("MainLayout")
@Scope("prototype")
public class MainLayout extends VerticalLayout implements ViewDisplay, IUiBindable {
	
	private static Logger log = Logger.getLogger(MainLayout.class);

	@UiField
	private Panel panel;
	
	private CssLayout container;
	@UiField
	private HorizontalLayout header;
	@UiField
	private HTMLLabel greeting;
	@UiField
	private HTMLLabel beta;
	@UiField
	private HTMLLabel logo;
	@UiField
	private MenuBar menu;
	
	@Autowired
	private KitchenOfficeUserService userService;
	
	private Locale locale;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 * @param locale 
	 */
	public MainLayout(Locale locale) {
		
		this.locale = locale;
	}

	@PostConstruct
	public void initialize() throws ClassNotFoundException {
		
		log.debug("Initializing MainLayout Instance..");
		
		KitchenOfficeNavigator.bindUIToComponent(this, locale);
		
		buildMainLayout();
		buildMenuBar();
		
		if(userService.getUser() != null) {
			// set Greeting Value
			greeting.setValue("Welcome, " + userService.getUser().getFirstName() + " " + userService.getUser().getLastName());
		}
	}

	private void buildMenuBar() throws ClassNotFoundException {
		
		//build menu bar
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
				false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(VaadinView.class));

		Set<BeanDefinition> beans = scanner
				.findCandidateComponents("com.gentics.kitchenoffice.webapp.view");
		
		Map<Integer, String> menuEntries = new HashMap<Integer, String>();
		
		
		for (BeanDefinition bean : beans) {
			
			Class<? extends KitchenOfficeViewInterface> clazz = (Class<? extends KitchenOfficeViewInterface>) Class
					.forName(bean.getBeanClassName());
			
			VaadinView vaadinView = clazz.getAnnotation(VaadinView.class);
			final String viewName = vaadinView.value();
			
			MenuEntrySortOrder sortOrderValue = clazz.getAnnotation(MenuEntrySortOrder.class);
			int sortOrder = sortOrderValue.value();
			
			menuEntries.put(sortOrder, viewName);

		}
		
		Map<Integer, String> sortedMenuEntries = new TreeMap<Integer, String>(menuEntries);
		
		for( final Entry<Integer, String> entry  : sortedMenuEntries.entrySet()) {
			
			menu.addItem(entry.getValue(), new Command(){

				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					//getUI().getNavigator().navigateTo(viewName);
					Page.getCurrent().setUriFragment("!" + entry.getValue());
				}
			});
		}
	}

	@AutoGenerated
	private void buildMainLayout() {
		
		container = new CssLayout();
		container.setSizeFull();
		
		panel.setContent(container);
		

		setMargin(true);
		setSizeFull();
		setExpandRatio(panel, 1.0f);
		
		header.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(beta, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(greeting, Alignment.MIDDLE_RIGHT);
		header.setExpandRatio(greeting, 1.0f);

	}

	@Override
	public void showView(View view) {
		if (view instanceof Component) {

			container.removeAllComponents();
			container.addComponent((Component) view);
			
			panel.setCaption(Page.getCurrent().getUriFragment().substring(1));

		} else {
			throw new IllegalArgumentException("View is not a component: "
					+ view);
		}

	}

	public CssLayout getContainer() {
		return container;
	}
	
	

}
