package app.modele.entity.animated;

import app.modele.Game;
import app.modele.GameData;
import app.modele.entity.Entity;
import app.modele.entity.inanimated.InanimatedEntity;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public abstract class AnimatedEntity extends Entity {
	
	protected int attaque;
	protected IntegerProperty hp;
	protected BooleanProperty isAttacking;
	
	protected int nbFrame;
	protected int frameMax;
	
	private Animation invicibilityFrame;
	protected boolean isInvicible;
	
	public AnimatedEntity(String id, int x, int y, int hp, int att, int v, int nb, int fmax) {
		super(id, x, y);
		this.hp = new SimpleIntegerProperty(hp);
		this.attaque = att;
		this.velocity = v;
		this.nbFrame = nb;
		this.frameMax = fmax;
		
		this.isAttacking = new SimpleBooleanProperty(false);
		this.isInvicible = false;
		
		this.invicibilityFrame = new PauseTransition(Duration.seconds(3));
		this.invicibilityFrame.setOnFinished(e -> this.setInvicible(false) );
	}
	
	public boolean moveLeft() {
		this.setOrientation(KeyCode.LEFT);
		if (canMove()) {
			x.set(x.get() - velocity);
			return true;
		}
		return false;
	}
	
	public boolean moveRight() {
		this.setOrientation(KeyCode.RIGHT);
		if (canMove()) {
			x.set(x.get() + velocity);
			return true;
		}
		return false;
	}

	public boolean moveDown() {
		this.setOrientation(KeyCode.DOWN);
		if (canMove()) {
			y.set(y.get() + velocity);
			return true;
		}
		return false;
	}
	
	public boolean moveUp() {
		this.setOrientation(KeyCode.UP);
		if (canMove()) {
			y.set(y.get() - velocity);
			return true;
		}
		return false;
	}
	
	public boolean canMove() {
		boolean canMove = false;
		boolean emptyTile = true;
		
		switch (this.orientation.getValue()) {
		case LEFT :
			emptyTile = tileIsEmpty(Game.getAnimatedEntities(), Game.getInanimatedEntities(), LEFT);
			if (x.get() % 32 != 0 && emptyTile)
				canMove = true;
			else if (x.get() % 32 == 0 && y.get() % 32 != 0) {
				if (Game.getMap().getNextTile(this.getIndiceY(), this.getIndiceX()-1).isCrossable() &&
					Game.getMap().getNextTile(this.getIndiceY()+1, this.getIndiceX()-1).isCrossable() && emptyTile)
					canMove = true;
			}
			else
				if (x.get() > LEFT_TOP_LIMIT && Game.getMap().getNextTile(this.getIndiceY(), this.getIndiceX()-1).isCrossable() && emptyTile)
					canMove = true;
			break;
		case UP :
			emptyTile = tileIsEmpty(Game.getAnimatedEntities(), Game.getInanimatedEntities(), UP);
			if (y.get() % 32 != 0 && emptyTile)
				canMove = true;
			else if (y.get() % 32 == 0 && x.get() % 32 != 0 && y.get() > 0) {
				if (Game.getMap().getNextTile(this.getIndiceY()-1, this.getIndiceX()).isCrossable() && 
					Game.getMap().getNextTile(this.getIndiceY()-1, this.getIndiceX()+1).isCrossable() && emptyTile)
					canMove = true;
			}
			else 
				if (y.get() > LEFT_TOP_LIMIT && Game.getMap().getNextTile(this.getIndiceY()-1, this.getIndiceX()).isCrossable() && emptyTile)
					canMove = true;
			break;
		case DOWN :
			emptyTile = tileIsEmpty(Game.getAnimatedEntities(), Game.getInanimatedEntities(), DOWN);
			if (y.get() % 32 != 0 && emptyTile) 
				canMove = true;
			else if (y.get() % 32 == 0 && x.get() % 32 != 0) {
				if (Game.getMap().getNextTile(this.getIndiceY()+1, this.getIndiceX()).isCrossable() && 
					Game.getMap().getNextTile(this.getIndiceY()+1, this.getIndiceX()+1).isCrossable() && emptyTile)
					canMove = true;
			}
			else 
				if (y.get() < RIGHT_BOTTOM_LIMIT && Game.getMap().getNextTile(this.getIndiceY()+1, this.getIndiceX()).isCrossable() && emptyTile)
					canMove = true;
			break;
		case RIGHT :
			emptyTile = tileIsEmpty(Game.getAnimatedEntities(), Game.getInanimatedEntities(), RIGHT);
			if (x.get() % 32 != 0 && emptyTile)
				canMove = true;
			else if (x.get() % 32 == 0 && y.get() % 32 != 0) {
				if (Game.getMap().getNextTile(this.getIndiceY(), this.getIndiceX()+1).isCrossable() &&
					Game.getMap().getNextTile(this.getIndiceY()+1, this.getIndiceX()+1).isCrossable() && emptyTile)
					canMove = true;
			}
			else
				if (x.get() < RIGHT_BOTTOM_LIMIT && Game.getMap().getNextTile(this.getIndiceY(), this.getIndiceX()+1).isCrossable() && emptyTile)
					canMove = true;
			break;
		default :
			break;
		}
		
		return canMove;
	}
	
	public void setOrientation(KeyCode k) {
		switch (k) {
		case LEFT :
			this.orientation.set(LEFT);
			break;
		case UP :
			this.orientation.set(UP);
			break;
		case RIGHT :
			this.orientation.set(RIGHT);
			break;
		case DOWN :
			this.orientation.set(DOWN);
			break;
		default :
			break;
		}
 	}
	
	public void setOrientation(int n) {
		this.orientation.set(n);
	}
	
	public boolean tileIsEmpty(ObservableList<AnimatedEntity> entities, ObservableList<InanimatedEntity> inanimatedEntities, int direction) {
		
		boolean emptyTile = true;
    	
		switch (direction) {
		case LEFT :
			for (AnimatedEntity e : entities)
	    		if (this.getX().get() == e.getX().get() + 32 && 
	    			this.getY().get() >= e.getY().get() - 31 && this.getY().get() <= e.getY().get() + 31)
	    			if (emptyTile)
	    				emptyTile = e.push(LEFT);
			for (InanimatedEntity e : inanimatedEntities)
	    		if (this.getX().get() == e.getX().get() + 32 && 
	    			this.getY().get() >= e.getY().get() - 31 && this.getY().get() <= e.getY().get() + 31)
	    			if (emptyTile)
	    				emptyTile = false;
			break;
		case UP :
			for (AnimatedEntity e : entities)
	    		if (this.getY().get() == e.getY().get() + 32 && 
	    			this.getX().get() >= e.getX().get() - 31 && this.getX().get() <= e.getX().get() + 31)
	    			if (emptyTile)
	    				emptyTile = e.push(UP);
			for (InanimatedEntity e : inanimatedEntities)
	    		if (this.getY().get() == e.getY().get() + 32 && 
	    			this.getX().get() >= e.getX().get() - 31 && this.getX().get() <= e.getX().get() + 31)
	    			if (emptyTile)
	    				emptyTile = false;
			break;
		case RIGHT :
			for (AnimatedEntity e : entities)
	    		if (this.getX().get() == e.getX().get() - 32 && 
	    			this.getY().get() >= e.getY().get() - 31 && this.getY().get() <= e.getY().get() + 31)
	    			if (emptyTile)
	    				emptyTile = e.push(RIGHT);
			for (InanimatedEntity e : inanimatedEntities)
	    		if (this.getX().get() == e.getX().get() - 32 && 
	    			this.getY().get() >= e.getY().get() - 31 && this.getY().get() <= e.getY().get() + 31)
	    			if (emptyTile)
	    				emptyTile = false;
			break;
		case DOWN :
			for (AnimatedEntity e : entities)
	    		if (this.getY().get() == e.getY().get() - 32 && 
	    			this.getX().get() >= e.getX().get() - 31 && this.getX().get() <= e.getX().get() + 31)
	    			if (emptyTile)
	    				emptyTile = e.push(DOWN);
			for (InanimatedEntity e : inanimatedEntities)
	    		if (this.getY().get() == e.getY().get() - 32 && 
	    			this.getX().get() >= e.getX().get() - 31 && this.getX().get() <= e.getX().get() + 31)
	    			if (emptyTile)
	    				emptyTile = false;
			break;
		default :
			break;
		}
		
    	return emptyTile;
	}
	
	public void attack() {
		return;
	}
	
	public boolean push(int DIRECTION) {
		return false;
	}
	
	public boolean interact() {
		return false;
	}
	
	public String getDialog() {
		return null;
	}
	
	public ObservableList<Bullet> getBullets() {
		return null;
	}
	
	public void resetIsAttacking() {
		this.isAttacking.set(false);
	}
	
	public BooleanProperty getIsAttacking() {
		return this.isAttacking;
	}
	
	public void setInvicible(boolean b) {
		this.isInvicible = b;
	}
	
	public boolean isInvicible() {
		return this.isInvicible;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getFrameMax() {
		return this.frameMax;
	}
	
	public int getNbFrame() {
		return this.nbFrame;
	}
	
	public int getFrame() {
		return frame;
	}
	
	public void incrementeFrame() {
		if (this.frame > this.frameMax-1)
			this.frame = 0;
		else 
			this.frame++;
	}
	
	public void resetFrame() {
		this.frame = 0;
	}
	
	public IntegerProperty getHP() {
		return this.hp;
	}
	
	public void loseHP(int a) {
		
		if (!this.isInvicible) {
			
			this.hp.set(this.hp.get() - a);
			
			if (this.hp.get() <= 0) {
				this.die();
				if (Math.random() < GameData.MONEY_DROP_RATE && Game.getPlayer() != null)
					Game.getPlayer().earnMoney(1);
			}
			
			if (!this.getIsDead().get()) {
				this.isInvicible = true;
				this.invicibilityFrame.play();
			}
			
		}
	}

}
