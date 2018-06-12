package app.controler;

import java.util.ArrayList;

import app.modele.Game;
import app.vue.FieldView;
import app.vue.entity.EntityView;
import app.vue.entity.PlayerView;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class FieldControler {
	
    private static FadeTransition mapChangeTransition;
    private static Rectangle mapChangeTransitionNode;
    
    public static void initializeScrollField(EntityView playerView, FieldView field, Game game, int SCROLL_WIDTH, int SCROLL_HEIGHT, int PANE_HEIGHT, int PANE_WIDTH, Pane tileContainer, Pane entityContainer, Pane interfaceContainer) {
    	
    	// Map transition
        mapChangeTransition = new FadeTransition();
        mapChangeTransitionNode = new Rectangle(600,600);
        creatingAnimation(interfaceContainer);
        
    	tileContainer.getChildren().addAll(field.getFieldView());
    	
    	setScrollX((int) playerView.getTranslateX() - SCROLL_WIDTH / 2, tileContainer, entityContainer);
		setScrollY((int) playerView.getTranslateY() - SCROLL_HEIGHT / 2, tileContainer, entityContainer);
    	
    	Game.getMapChanged().addListener(new ChangeListener<Boolean>() {
    		
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				AnimationTransitionMap(0.2);
				field.refreshField();
				
				switch (Game.getPlayer().getOrientation().get()) {
				case 0:
					if (playerView.getTranslateY() - SCROLL_HEIGHT / 2 < 0)
						setScrollY(0, tileContainer, entityContainer);
					else if (playerView.getTranslateY() + SCROLL_HEIGHT / 2 > PANE_HEIGHT)
						setScrollY(800 - SCROLL_HEIGHT, tileContainer, entityContainer);
					else
						setScrollY((int) playerView.getTranslateY() - SCROLL_HEIGHT / 2, tileContainer, entityContainer);
					setScrollX((int) playerView.getTranslateX() - SCROLL_WIDTH + 32, tileContainer, entityContainer);
					break;
				case 1:
					if (playerView.getTranslateX() - SCROLL_WIDTH / 2 < 0)
						setScrollX(0, tileContainer, entityContainer);
					else if (playerView.getTranslateX() + SCROLL_WIDTH / 2 > PANE_WIDTH)
						setScrollX(800 - SCROLL_WIDTH, tileContainer, entityContainer);
					else
						setScrollX((int) playerView.getTranslateX() - (SCROLL_WIDTH / 2), tileContainer, entityContainer);
					setScrollY((int) playerView.getTranslateY() - SCROLL_HEIGHT + 32, tileContainer, entityContainer);
					break;
				case 2:
					if (playerView.getTranslateY() - SCROLL_HEIGHT / 2 < 0)
						setScrollY(0, tileContainer, entityContainer);
					else if (playerView.getTranslateY() + SCROLL_HEIGHT / 2 > PANE_HEIGHT)
						setScrollY(800 - SCROLL_HEIGHT, tileContainer, entityContainer);
					else
						setScrollY((int) playerView.getTranslateY() - SCROLL_HEIGHT / 2, tileContainer, entityContainer);
					setScrollX((int) playerView.getTranslateX(), tileContainer, entityContainer);
					break;
				case 3:
					if (playerView.getTranslateX() - SCROLL_WIDTH / 2 < 0)
						setScrollX(0, tileContainer, entityContainer);
					else if (playerView.getTranslateX() + SCROLL_WIDTH / 2 > PANE_WIDTH)
						setScrollX(800 - SCROLL_WIDTH, tileContainer, entityContainer);
					else
						setScrollX((int) playerView.getTranslateX() - SCROLL_WIDTH / 2, tileContainer, entityContainer);
					setScrollY((int) playerView.getTranslateY(), tileContainer, entityContainer);
					break;
				}
				
			}
    		
    	});
    	
    	moveScroll(playerView, tileContainer, field, entityContainer, SCROLL_WIDTH, SCROLL_HEIGHT, PANE_HEIGHT, PANE_WIDTH);
    	
    }
    
    public static void moveScroll(EntityView playerView, Pane tileContainer, FieldView field, Pane entityContainer, int SCROLL_WIDTH, int SCROLL_HEIGHT, int PANE_HEIGHT, int PANE_WIDTH) {
    	
    	Game.getPlayer().getX().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	    		if (playerView.getTranslateX() - SCROLL_WIDTH / 2 + 1 > 0 && playerView.getTranslateX() + SCROLL_WIDTH / 2 < PANE_WIDTH)
	    			setScrollX((int) playerView.getTranslateX() - SCROLL_WIDTH / 2, tileContainer, entityContainer);
			}
    		
		});
    	
    	Game.getPlayer().getY().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (playerView.getTranslateY() - SCROLL_HEIGHT / 2 + 1 > 0 && playerView.getTranslateY() + SCROLL_HEIGHT / 2 < PANE_HEIGHT)
	    			setScrollY((int) playerView.getTranslateY() - SCROLL_HEIGHT / 2, tileContainer, entityContainer);
			}
		});
    	
    }
    
    private static void setScrollX(int a, Pane tileContainer, Pane entityContainer) {
    	tileContainer.setTranslateX(-a);
		entityContainer.setTranslateX(-a);
    }
    
    private static void setScrollY(int a, Pane tileContainer, Pane entityContainer) {
    	tileContainer.setTranslateY(-a);
		entityContainer.setTranslateY(-a);
    }
    
	public static void AnimationTransitionMap(Double i) {
	   	mapChangeTransition.setDuration(Duration.seconds(i));
		mapChangeTransition.play();
	}
	 
	private static void creatingAnimation(Pane interfaceContainer) {
		mapChangeTransition.setFromValue(1.0);
		mapChangeTransition.setToValue(0.0);
		mapChangeTransition.setNode(mapChangeTransitionNode);
		
		mapChangeTransitionNode.setFill(Color.BLACK);
		interfaceContainer.getChildren().add(mapChangeTransitionNode);
	}
	

}
