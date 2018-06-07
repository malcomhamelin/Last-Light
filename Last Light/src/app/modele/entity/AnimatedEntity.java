package app.modele.entity;

import java.util.ArrayList;

import app.modele.Game;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;

public abstract class AnimatedEntity extends Entity {
	
	protected int attaque;
	protected IntegerProperty hp;
	protected BooleanProperty isAttacking;
	
	protected int nbFrame;
	protected int frameMax;
	
	public AnimatedEntity(String id, int x, int y, int hp, int att, int v, int nb, int fmax) {
		super(id, x, y);
		this.hp = new SimpleIntegerProperty(hp);
		this.attaque = att;
		this.velocity = v;
		this.nbFrame = nb;
		this.frameMax = fmax;
		
		this.isAttacking = new SimpleBooleanProperty(false);
	}
	
	public boolean moveLeft(ObservableList<AnimatedEntity> entities) {
		this.setOrientation(KeyCode.LEFT);
		if (canMove(entities)) {
			x.set(x.get() - velocity);
			return true;
		}
		return false;
	}
	
	public boolean moveRight(ObservableList<AnimatedEntity> entities) {
		this.setOrientation(KeyCode.RIGHT);
		if (canMove(entities)) {
			x.set(x.get() + velocity);
			return true;
		}
		return false;
	}

	public boolean moveDown(ObservableList<AnimatedEntity> entities) {
		this.setOrientation(KeyCode.DOWN);
		if (canMove(entities)) {
			y.set(y.get() + velocity);
			return true;
		}
		return false;
	}
	
	public boolean moveUp(ObservableList<AnimatedEntity> entities) {
		this.setOrientation(KeyCode.UP);
		if (canMove(entities)) {
			y.set(y.get() - velocity);
			return true;
		}
		return false;
	}
	
	public boolean canMove(ObservableList<AnimatedEntity> entities) {
		boolean canMove = false;
		boolean emptyTile = true;
		
		switch (this.orientation.getValue()) {
		case LEFT :
			emptyTile = tileIsEmpty(entities, LEFT);
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
			emptyTile = tileIsEmpty(entities, UP);
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
			emptyTile = tileIsEmpty(entities, DOWN);
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
			emptyTile = tileIsEmpty(entities, RIGHT);
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
	
	public boolean tileIsEmpty(ObservableList<AnimatedEntity> entities, int DIRECTION) {
    	
		switch (DIRECTION) {
		case LEFT :
			for (AnimatedEntity e : entities)
	    		if (this.getX().get() == e.getX().get() + 32 && 
	    			this.getY().get() >= e.getY().get() - 31 && this.getY().get() <= e.getY().get() + 31)
	    			return e.interact(LEFT, entities);
			break;
		case UP :
			for (AnimatedEntity e : entities)
	    		if (this.getY().get() == e.getY().get() + 32 && 
	    			this.getX().get() >= e.getX().get() - 31 && this.getX().get() <= e.getX().get() + 31)
	    			return e.interact(UP, entities);
			break;
		case RIGHT :
			for (AnimatedEntity e : entities)
	    		if (this.getX().get() == e.getX().get() - 32 && 
	    			this.getY().get() >= e.getY().get() - 31 && this.getY().get() <= e.getY().get() + 31)
	    			return e.interact(RIGHT, entities);
			break;
		case DOWN :
			for (AnimatedEntity e : entities)
	    		if (this.getY().get() == e.getY().get() - 32 && 
	    			this.getX().get() >= e.getX().get() - 31 && this.getX().get() <= e.getX().get() + 31)
	    			return e.interact(DOWN, entities);
			break;
		default :
			break;
		}
		
    	return true;
	}
	
	public void attack(ObservableList<AnimatedEntity> entities) {
		
	}
	
	public boolean interact(int DIRECTION, ObservableList<AnimatedEntity> entities) {
		return false;
	}
	
	public void resetIsAttacking() {
		this.isAttacking.set(false);
	}
	
	public BooleanProperty getIsAttacking() {
		return this.isAttacking;
	}
	
	public IntegerProperty getOrientation() {
		return this.orientation;
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
		if (this.hp.get() - a < 1)
			this.die();
		else
			this.hp.set(this.hp.get() - a);
	}

}
