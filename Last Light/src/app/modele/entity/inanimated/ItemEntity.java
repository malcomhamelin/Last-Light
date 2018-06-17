package app.modele.entity.inanimated;

import java.io.BufferedWriter;  
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import app.modele.Game;
import app.modele.GameData;
import app.modele.entity.animated.Player;
import app.modele.weapon.Lamp;
import app.modele.weapon.Taser;

public class ItemEntity extends InanimatedEntity {

	public ItemEntity(String id, int x, int y, String dialog) {
		super(id, x, y, dialog);
	}
	
	public boolean interact(Player p) {
		boolean hasInteracted = true;
		
		switch (this.id) {
		case GameData.ENTITY_LAMP :
			p.getWeapons().add(new Lamp(1, 1));
			this.die();
			break;
		case GameData.ENTITY_TASER :
			p.getWeapons().add(new Taser(1, 1));
			this.die();
			break;
		case GameData.ENTITY_SODA :
			if (p.getPotion().getValue() < 3) {
				p.earnPotion();
				this.die();
			}
			break;
		case GameData.ENTITY_NECKLACE :
			p.setNecklace(true);
			this.die();
			break;
		case GameData.ENTITY_BOOTS :
			p.setBoots(true);
			this.die();
			break;
		case GameData.ENTITY_HEART :
			p.unlockHeart();
			this.die();
			break;
		case GameData.ENTITY_AMMUNITION :
			p.earnAmmunition();
			this.die();
			break;
 		default : 
 			this.die();
 			hasInteracted = false;
 			break;
		}
		
		// Remplis le fichier pour savoir si un item a ete pris
		try {
				
			File f = new File("src/map/takenItems.txt");
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(Game.getMapId() + "," + this.id.charAt(0) + "\n");
				
			bw.close();
			fw.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("takenItems : Fichier introuvable");
		} catch (IOException e) {
			System.out.println("takenItems : Erreur de lecture");
		}
		
		return hasInteracted;
	}

}
