package com.gentics.kitchenoffice.data;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity
public class Ingredient extends AbstractPersistable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8100798945864077147L;

	@Fetch
	@StartNode
    private Recipe recipe;
	
	@Fetch
	@EndNode
    private Article article;
    
    private double amount;
    
    public Ingredient(){
    	
    }

	public Ingredient(Article article, Recipe recipe, double amount) {
		super();
		this.article = article;
		this.recipe = recipe;
		this.amount = amount;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
    
	@Override
	public String toString() {
		return String.format("Incredient{name='%s', price=%s, recipe=%s}", article.getName(), String.valueOf(amount * article.getPrice()), recipe.getName());
	}
    
    
}
