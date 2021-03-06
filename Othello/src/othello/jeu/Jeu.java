package othello.jeu;

import java.awt.Point;

import othello.ui.Couleur;
import othello.ui.DiffDialog;
import othello.ui.Niveau;
import othello.ui.Plateau;

public class Jeu {

	public static Couleur tour;
	public static Couleur nonTour;
	private static int nombreTour;
	
	public Niveau niveau;
	private Plateau plateau;
	private IA iA;
	private Couleur gagnant;
	private DiffDialog debug; // A CHANGER

	public Jeu(Niveau niveau) {
	   this.niveau = niveau;
	   tour = Couleur.NOIR;
	   nonTour = (tour == Couleur.BLANC) ? Couleur.NOIR : Couleur.BLANC;
	   iA = new IA(niveau);
	}

	public void setPlateau(Plateau plateau) {
	   this.plateau = plateau;
	}
	
	public void setNiveau(Niveau niveau) {
	   this.niveau = niveau;
	}
	
	public Couleur getGagnant() {
       return gagnant;
	}
	
	public void setGagnant(Couleur gagnant) {
	   this.gagnant = gagnant;
	}

	public static void changeTour() {
	   Couleur inter = tour;
	   tour = nonTour;
	   nonTour = inter;
	   nombreTour++;
	}

	public void setCasesJouables() {
	   for (int i = 0; i < Plateau.PLATEAU_HEIGHT; i++) {
	      for (int j = 0; j < Plateau.PLATEAU_WIDTH; j++) {
		     if (plateau.getCases()[i][j].getCouleur() == tour)
			    search(i, j, -1, 1, -1, 1, false);
	      }
	   }
	}

	private void search(int posI, int posJ, int minI, int maxI, int minJ, int maxJ, boolean pionAdverse) {
	   for (int i = minI; i <= maxI; i++) {
	      for (int j = minJ; j <= maxJ; j++) {
		     if (posI + i < 0 || posI + i >= Plateau.PLATEAU_HEIGHT || posJ + j < 0 || posJ + j >= Plateau.PLATEAU_WIDTH)
			    continue;
			 if (pionAdverse && plateau.getCases()[posI + i][posJ + j].getCouleur() == Couleur.NONE) {
				plateau.getCases()[posI + i][posJ + j].setJouable(true);
				plateau.getCases()[posI + i][posJ + j].addDirection(new Point(-i, -j));
			 }
			 if (plateau.getCases()[posI + i][posJ + j].getCouleur() == nonTour)
				search(posI + i, posJ + j, i, i, j, j, true);
		  }
	   }
	}

	public boolean jeuIA() {
		State pion = iA.tour(plateau.getSampleCases(), nombreTour, debug); // A CHANGER
		if (pion != null) {
			System.out.println(pion.getSeconds().size());
			plateau.getCase(pion.getPrimary().y, pion.getPrimary().x).setCouleur(Couleur.BLANC);
			for (int i = 0; i < pion.getSeconds().size(); ++i) {
				plateau.getCase(pion.getSeconds().get(i).y, pion.getSeconds().get(i).x).setCouleur(Couleur.BLANC);
			}
			return true;
		}
		return false;
	}

	public void addDebug(DiffDialog dialog) {
		debug = dialog;
	}

}
