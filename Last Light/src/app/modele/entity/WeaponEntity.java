package app.modele.entity;

import app.modele.weapon.Lampe;
import app.modele.weapon.Weapon;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

public class WeaponEntity extends InanimatedEntity {

	public WeaponEntity(String id, int x, int y) {
		super(id, x, y);
	}
	
	// Dumb af cette methode mais est construite dans l'idée où une arme spawnera une et une seule fois dans le jeu
	public void interact(Player p) {
		switch (this.id) {
		case "lamp" :
			if (p.getWeapons().size() < 1) {
				p.getWeapons().add(new Lampe(1, 1));
				this.die();
			}
			break;
		default :
			break;
		}
	}

}
