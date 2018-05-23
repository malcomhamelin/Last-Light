package app.modele.entity;

import java.util.ArrayList;

import app.modele.field.Field;
import javafx.collections.ObservableList;

public class Ennemy extends AnimatedEntity {

	public Ennemy(int x, int y, int pv, int att, int v) {
		super(x, y, pv, att, v);
	}

	public void update(ObservableList<Entity> entities) {
		if (Math.random() < 0.25)
			this.moveDown(entities);
		else if (Math.random() < 0.5)
			this.moveLeft(entities);
		else if (Math.random() < 0.75)
			this.moveRight(entities);
		else
			this.moveUp(entities);
	}

}
