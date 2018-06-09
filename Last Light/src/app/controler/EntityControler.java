package app.controler;

import java.util.ArrayList;  

import app.modele.Game;
import app.modele.entity.Entity;
import app.vue.entity.AnimatedEntityView;
import app.vue.entity.BulletView;
import app.vue.entity.EnemyView;
import app.vue.entity.EntityView;
import app.vue.entity.InanimatedEntityView;
import app.vue.entity.NPCView;
import app.vue.entity.PlayerView;
import app.vue.entity.RockView;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class EntityControler {
	
	private static FadeTransition entityDisappearance;
	private static FadeTransition attackAnimation;
	private static boolean attackAnimationActive = false;
	
    public static void initializeEntities(Pane entityContainer, Game game, PlayerView playerView, ArrayList<EntityView> entitiesView) {
    	
    	entityDisappearance = new FadeTransition();
    	entityDisappearance.setFromValue(1.0);
 		entityDisappearance.setToValue(0.0);
 		entityDisappearance.setDuration(Duration.seconds(0.05));
 		entityDisappearance.setOnFinished(event -> {
			entitiesView.remove(entityDisappearance.getNode());
			entityDisappearance.setNode(null);
		});
 		
 		attackAnimation = new FadeTransition();
 		attackAnimation.setFromValue(1.0);
 		attackAnimation.setToValue(0.0);
 		attackAnimation.setDuration(Duration.seconds(0.2));
 		attackAnimation.setNode(playerView.getAttackImage());
		attackAnimation.setOnFinished(event2 -> {
			playerView.resetAnimationAttack();
			entityContainer.getChildren().remove(attackAnimation.getNode());
			game.getPlayer().resetIsAttacking();
			attackAnimationActive = false;
		});
    	
    	entitiesView.add(playerView);
    	entityContainer.getChildren().add(playerView);
    	
    	game.getEntities().addListener(new ListChangeListener<Entity>() {

			@Override
			public void onChanged(Change<? extends Entity> c) {
				
				while (c.next()) {
					if (c.wasAdded()) {
						switch (game.getEntities().get(game.getEntities().size() - 1).getId()) {
						case "walker" :
							entitiesView.add(new EnemyView(game.getEntities().get(game.getEntities().size() - 1)));
							break;
						case "rock" :
							entitiesView.add(new RockView(game.getEntities().get(game.getEntities().size() - 1)));
							break;
						case "sprite" :
							entitiesView.add(new NPCView(game.getEntities().get(game.getEntities().size() - 1)));
						default :
							break;
						}
						
						entityContainer.getChildren().add(entitiesView.get(entitiesView.size()-1));
					}
				}
				
			}
    		
    	});
    	
    	game.getInanimatedEntities().addListener(new ListChangeListener<Entity>() {

			@Override
			public void onChanged(Change<? extends Entity> c) {
				
				while (c.next()) {
					if (c.wasAdded()) {
						entitiesView.add(new InanimatedEntityView(game.getInanimatedEntities().get(game.getInanimatedEntities().size()-1)));
						entityContainer.getChildren().add(entitiesView.get(entitiesView.size()-1));
					}
				}
				
			}
    		
    	});
    	
    	playerView.getBullets().addListener(new ListChangeListener<BulletView>() {

			@Override
			public void onChanged(Change c) {
				while (c.next())
					if (c.wasAdded()) {
						entitiesView.add(playerView.getBullets().get(playerView.getBullets().size()-1));
						entityContainer.getChildren().add(entitiesView.get(entitiesView.size()-1));
					}
			}
    		
    	});
    	
    	game.getPlayer().getIsAttacking().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {

                if (newValue.booleanValue()) {
                    switch (game.getPlayer().getOrientation().get()) {
                    case 0 :
                        playerView.getAttackImage().setTranslateX(entitiesView.get(0).getTranslateX() - 32);
                        playerView.getAttackImage().setTranslateY(entitiesView.get(0).getTranslateY() + 5);
                        playerView.getAttackImage().setRotate(-90);
                        break;
                    case 1 :
                    	playerView.getAttackImage().setTranslateX(entitiesView.get(0).getTranslateX());
                    	playerView.getAttackImage().setTranslateY(entitiesView.get(0).getTranslateY() - 32);
                    	playerView.getAttackImage().setRotate(0);
                        break;
                    case 2 : 
                    	playerView.getAttackImage().setTranslateX(entitiesView.get(0).getTranslateX() + 32);
                    	playerView.getAttackImage().setTranslateY(entitiesView.get(0).getTranslateY() + 5);
                    	playerView.getAttackImage().setRotate(90);
                        break;
                    case 3 :
                    	playerView.getAttackImage().setTranslateX(entitiesView.get(0).getTranslateX());
                    	playerView.getAttackImage().setTranslateY(entitiesView.get(0).getTranslateY() + 32);
                    	playerView.getAttackImage().setRotate(180);
                        break;
                    default :
                        break;
                    }
                    
                }

            }

        });
    	
    	game.addKeyFrame(e -> {
    		
    		for (int i = 0 ; i < entitiesView.size() ; i++)
    			entitiesView.get(i).update();
    		
    		for (int i = 0 ; i < entitiesView.size() ; i++) 
    			if (entitiesView.get(i).getIsDead()) {
     				entityDisappearance.setNode(entitiesView.get(i));
     				break;
    			}
    		
    		entityDisappearance.play();
    		
    		if (game.getPlayer().getIsAttacking().get()) 
    			if (!attackAnimationActive) {
    				playerView.animationAttack();
    				attackAnimationActive = true; 
    				entityContainer.getChildren().add(attackAnimation.getNode());
    				attackAnimation.play();
    			}
    		
 		}, 0.017);
    	
    }
	
}
