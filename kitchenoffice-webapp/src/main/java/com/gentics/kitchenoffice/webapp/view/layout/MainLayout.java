package com.gentics.kitchenoffice.webapp.view.layout;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import ru.xpoft.vaadin.VaadinView;

import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@Scope("session")
public class MainLayout extends VerticalLayout implements ViewDisplay {
	
	private static Logger log = Logger.getLogger(MainLayout.class);

	@AutoGenerated
	private Panel panel;
	@AutoGenerated
	private HorizontalLayout header;
	@AutoGenerated
	private Label greeting;
	@AutoGenerated
	private Label beta;
	@AutoGenerated
	private Label logo;

	private MenuBar menu;
	
	@Autowired
	private KitchenOfficeUserService userService;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public MainLayout() {
		buildMainLayout();

		// TODO add user code here
	}

	@PostConstruct
	public void initialize() throws ClassNotFoundException {

		log.debug("Initializing MainLayout Instance..");
		
		//build menu bar
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
				false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(VaadinView.class));

		Set<BeanDefinition> beans = scanner
				.findCandidateComponents("com.gentics.kitchenoffice.webapp.view");
		for (BeanDefinition bean : beans) {
			Class<? extends View> clazz = (Class<? extends View>) Class
					.forName(bean.getBeanClassName());
			VaadinView vaadinView = clazz.getAnnotation(VaadinView.class);
			final String viewName = vaadinView.value();

			menu.addItem(viewName, new Command(){

				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					//getUI().getNavigator().navigateTo(viewName);
					
					Page.getCurrent().setFragment("!" + viewName);
				}
				
				
			});
		}
		
		if(userService.getUserDetails() != null) {
			// set Greeting Value
			greeting.setValue("Welcome, " + userService.getUserDetails().getUsername());
			
		}
		
	}

	@AutoGenerated
	private void buildMainLayout() {
		// common part: create layout
		setImmediate(false);
		setWidth("100%");
		setHeight("100%");
		setMargin(true);

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		// header
		header = buildHeader();
		addComponent(header);

		// menu
		menu = new MenuBar();
		menu.setWidth("100%");
		addComponent(menu);

		// tabSheet
		panel = new Panel();
		panel.setImmediate(false);
		panel.setWidth("100.0%");
		panel.setHeight("100.0%");
		addComponent(panel);
		setExpandRatio(panel, 1.0f);

	}

	@AutoGenerated
	private HorizontalLayout buildHeader() {
		// common part: create layout
		header = new HorizontalLayout();
		header.setImmediate(false);
		header.setWidth("100.0%");
		header.setHeight("-1px");
		header.setSpacing(true);

		// logo
		logo = new Label();
		logo.setImmediate(false);
		logo.setWidth("-1px");
		logo.setHeight("-1px");
		logo.setValue("<h1>Kitchen Office</h1>");
		logo.setContentMode(ContentMode.HTML);
		header.addComponent(logo);
		header.setComponentAlignment(logo, new Alignment(33));

		// beta
		beta = new Label();
		beta.setImmediate(false);
		beta.setWidth("-1px");
		beta.setHeight("-1px");
		beta.setValue("<span style=\"color:red;\">alpha</span>");
		beta.setContentMode(ContentMode.HTML);
		header.addComponent(beta);
		header.setComponentAlignment(beta, new Alignment(33));

		// greeting
		greeting = new Label();
		greeting.setImmediate(false);
		greeting.setWidth("-1px");
		greeting.setHeight("-1px");
		greeting.setValue("greeting");
		header.addComponent(greeting);
		header.setExpandRatio(greeting, 1.0f);
		header.setComponentAlignment(greeting, new Alignment(34));

		return header;
	}

	@Override
	public void showView(View view) {
		if (view instanceof Component) {

			panel.removeAllComponents();
			panel.addComponent((Component) view);
			
			panel.setCaption(Page.getCurrent().getFragment().toString().substring(1));

		} else {
			throw new IllegalArgumentException("View is not a component: "
					+ view);
		}

	}

	public Panel getPanel() {
		return panel;
	}
	
	

}
