package com.gentics.kitchenoffice;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.gentics.kitchenoffice.data.Article;
import com.gentics.kitchenoffice.data.Meal;
import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.data.User;
import com.gentics.kitchenoffice.repository.ArticleRepository;
import com.gentics.kitchenoffice.repository.CommentRepository;
import com.gentics.kitchenoffice.repository.MealRepository;
import com.gentics.kitchenoffice.repository.RecipeRepository;
import com.gentics.kitchenoffice.repository.UserRepository;

import static org.junit.Assert.*;


@ContextConfiguration(locations = "classpath:/spring/kitchenContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RecipeTest {
	
	private static Logger log = Logger.getLogger(RecipeTest.class);
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private MealRepository mealRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private Neo4jTemplate template;
	
	@Rollback(false)
	@BeforeTransaction
	public void cleanUpGraph() {
		Neo4jHelper.cleanDb(template);
	}
	
	@Test
	public void firstTest() {
		
		log.debug("starting first Test");
		createSomeRecipes();
		
		
		log.debug("asserting");
		assertEquals(2, recipeRepository.count());
		assertEquals(5, articleRepository.count());
		
		
		Recipe result = recipeRepository.findAllByPropertyValue("name", "Tortellini mit Tomatensoße").single();
		List<Recipe> result2 = recipeRepository.findByNameContaining("Tomaten");
		
		assertEquals(result, result2.get(0));
		
		log.debug("result: " + result.toString());
		log.debug("incredients size: " + result.getIncredients().size());
		log.debug("finishing first Test");
	}
	
	@Test
	public void mealTest() {
		log.debug("starting meal test");
		
		createSomeRecipes();
		createSomeMealsAndComments();
		
		log.debug("mealcount: " + mealRepository.count());
		
		EndResult<Meal> meals = mealRepository.findAll();
		
		for (Meal meal : meals) {
			log.debug("meal: \n" + meal);
		}
		
		log.debug("comments: " + commentRepository.count());
	}
	
	
	private void createSomeRecipes() {
		
		Recipe r = new Recipe("Tortellini mit Tomatensoße", "lecker", 1, 5);
		recipeRepository.save(r);
		
		Article i1 = new Article("Tortellini Pasta", "Package, 500g", 0.89);
		articleRepository.save(i1);
		Article i2 = new Article("Tomaten in der Dose, gestückelt", "Dose, 400ml", 2.0);
		articleRepository.save(i2);
		Article i3 = new Article("Zwiebeln", "Package", 0.89);
		articleRepository.save(i3);
		Article i4 = new Article("Parmesan, Grana", "Package", 0.89);
		articleRepository.save(i4);
		
		// saving after each adding seems to be necessary
		r.addArticle(i1, 1);
		recipeRepository.save(r);
		r.addArticle(i2, 2);
		recipeRepository.save(r);
		r.addArticle(i3, 0.5);
		recipeRepository.save(r);
		r.addArticle(i4, 0.5);
		recipeRepository.save(r);
		
		Recipe r2 = new Recipe("Nudeln mit Tomatensoße", "lecker", 1, 10);
		
		Article i5 = new Article("Nudeln Pasta, irgendeine Sorte", "Package, 500g", 0.89);
		articleRepository.save(i5);
		
		r2.addArticle(i5, 1);
		recipeRepository.save(r2);
		r2.addArticle(i2, 1);
		recipeRepository.save(r2);
		r2.addArticle(i3, 1);
		recipeRepository.save(r2);
		r2.addArticle(i4, 1);
		recipeRepository.save(r2);
		
	}
	
	private void createSomeMealsAndComments() {
		
		User u1 = new User("max", "mustermann", "maxi", true);
		userRepository.save(u1);
		User u2 = new User ("Helmut", "Tester", "helmi", true);
		userRepository.save(u2);
		
		Recipe result = recipeRepository.findByPropertyValue("name", "Tortellini mit Tomatensoße");
		Recipe result2 = recipeRepository.findByPropertyValue("name", "Nudeln mit Tomatensoße");
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, 1);
		
		Meal m1 = new Meal(cal.getTime(), result);
		m1.addParticipant(u1, "einkaufen und Tellerwaschen");
		mealRepository.save(m1);
		
		m1.addComment(u2, "is gut geworden! nächstes mal mehr chili..");
		mealRepository.save(m1);
		
		cal.add(Calendar.DATE, 1);
		
		Meal m2 = new Meal(cal.getTime(), result2);
		m2.addParticipant(u1, "einkaufen");
		mealRepository.save(m2);
		m2.addParticipant(u2, "kochen");
		mealRepository.save(m2);
		
		m2.addComment(u1, "das sollten wir öfter machen!");
		mealRepository.save(m2);
		
	}
	
	

}
