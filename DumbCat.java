import java.awt.Color;

// Final constants for each variable
public class DumbCat {
	static final int CAT_MOVING = 1;
	static final int CAT_DEAD = 2;
	static final int CAT_WINS = 3;
	static final int CAT_CUTSCENE = 4;

	public static void main(String[] args) {           // (ZM)
		EZ.initialize(500, 700); 					   // Initialize the screen
		EZ.setBackgroundColor(new Color(250, 250, 0)); // Sets the background color
		Color c = new Color(0, 0, 200); 			   // Sets cutscene screen to yellow
		Color c2 = new Color(250, 250, 250);           // Sets second cutscene screen to black
		Color c3 = new Color(250, 0, 0);               // Sets third cutscene text to Red
		int fontsize = 20;                             // Sets font size

		// Creates Start Cutscene and Text along with it (ZM)
		int cat_state = CAT_CUTSCENE;
		Cat dumbcatstart = new Cat("images/dumbcat.png", 250, 350);
		EZText cutText = EZ.addText(250, 250, "WELCOME TO DUMB CAT!", c, fontsize);
		EZText cutText2 = EZ.addText(250, 470, "PRESS C TO START LE GAME!", c, fontsize);
		EZText cutText3 = EZ.addText(250, 690, "Use WASD to move", c, fontsize);

		// Initializes the sounds (DN)
		EZSound Slayer = EZ.addSound("sounds/Slayer.wav");
		EZSound Go = EZ.addSound("sounds/Go.wav");
		EZSound Win = EZ.addSound("sounds/Hallelujah.wav");
		EZSound BadDay = EZ.addSound("sounds/Baad Day.wav");
		EZSound Death = EZ.addSound("sounds/Death.wav");
		EZSound FinalZone = EZ.addSound("sounds/Zone3.wav");
		EZSound CarHit = EZ.addSound("sounds/Fan Smack Hit.wav");
		EZSound FireHit = EZ.addSound("sounds/Explosion.wav");
		EZSound LaserHit = EZ.addSound("sounds/Laser.wav");
		Slayer.loop(); // Loops the background music

		// While you're on the cutscene press c to start (ZM).
		while (cat_state == CAT_CUTSCENE) {
			if (EZInteraction.wasKeyPressed('c')) {
				cat_state = CAT_MOVING;
				dumbcatstart.dumbcat.hide();
				cutText.hide();
				cutText2.hide();
				cutText3.hide();
				Go.play();
			}
			EZ.refreshScreen();
		}

		// Creates each Safezone (DN)
		EZLine safeZone;
		safeZone = EZ.addLine(0, 690, 500, 690, Color.YELLOW, 60);
		EZLine roadZone;
		roadZone = EZ.addLine(0, 560, 500, 560, Color.DARK_GRAY, 200);
		EZLine safeZone2;
		safeZone2 = EZ.addLine(0, 470, 500, 470, Color.YELLOW, 40);
		EZLine lavaZone;
		lavaZone = EZ.addLine(0, 350, 500, 350, Color.RED, 200);
		EZLine safeZone3;
		safeZone3 = EZ.addLine(0, 250, 500, 250, Color.YELLOW, 40);
		EZLine spaceZone;
		spaceZone = EZ.addLine(0, 0, 500, 0, Color.BLACK, 460);
		EZLine finishZone;
		finishZone = EZ.addLine(0, 10, 500, 10, Color.WHITE, 30);

		// Puts cat at starting position and sets score, lives and count to 0 (DN)
		Cat dumbcat = new Cat("images/dumbcat.png", 250, 675);
		int score = 0;
		int catLives = 9;
		int count = 0;

		// First cutscene text (DN)
		EZText startText = EZ.addText(250, 250, "ARE YOU A DUMB CAT? LETS FIND OUT...", c, fontsize);
		EZText startText2 = EZ.addText(250, 470, "YOU HAVE 9 LIVES, SAVE 3 CATS TO WIN!", c, fontsize);

		// Constructors for Fireball and car obstacles
		catObstacle fireball[] = new catObstacle[3]; // ZM
		catObstacle killerCar[] = new catObstacle[3]; // DN
		catObstacle laser[] = new catObstacle[3]; // CW

		// For loops initializing number of each obstacle
		for (int i = 0; i < 3; i++)
			fireball[i] = new catObstacle("images/fireball.png", 500, 450, 300); // ZM
		for (int i = 0; i < 3; i++)
			killerCar[i] = new catObstacle("images/delorean.png", 500, 690, 520); // DN
		for (int i = 0; i < 3; i++)
			laser[i] = new catObstacle("images/laser.png", 500, 260, 40); // CW

		// Once the cat moves, text will hide (DN)
		while (cat_state == CAT_MOVING) {
			if (dumbcat.getX() != 250 || dumbcat.getY() != 675) {
				startText.hide();
				startText2.hide();
			}
			// Controls for cat (DN)
			if (dumbcat.getX() < 500 && dumbcat.getX() > 0 && dumbcat.getY() < 700 && dumbcat.getY() > 0) {
				dumbcat.ControlIt();
			} else {
				dumbcat.setPosition(250, 675);
			}
			// Resets cat to start postion once you make it to the top (DN)
			if (dumbcat.getY() < 0) {
				dumbcat.setPosition(250, 675);
				score++;
				FinalZone.play();
			}
			// If you saved 3 cats then you win (DN)
			if (score == 3) {
				cat_state = CAT_WINS;
				Slayer.stop();
				Win.loop();
			}

			// If either the killercar, fireball or laser hits the cat, then you
			// die and the cats position is reset
			for (int i = 0; i < 3; i++) {
				fireball[i].move(); // ZM
				killerCar[i].move(); // DN
				laser[i].move(); // CW
				if ((fireball[i].isInside(dumbcat.getX() - 5, // (ZM)
						dumbcat.getY() - 5)) || (fireball[i].isInside(dumbcat.getX() + 5, dumbcat.getY() - 5))
						|| (fireball[i].isInside(dumbcat.getX() - 5, dumbcat.getY() + 5))
						|| (fireball[i].isInside(dumbcat.getX() + 5, dumbcat.getY() + 5))) {
					dumbcat.setPosition(250, 675);
					count++;
					if (count == 1 || count == 2 || count == 3)
						catLives--;
					count = 0;
					FireHit.play();
					Death.play();
				}
				if ((killerCar[i].isInside(dumbcat.getX() - 5, // (DN)
						dumbcat.getY() - 5)) || (killerCar[i].isInside(dumbcat.getX() + 5, dumbcat.getY() - 5))
						|| (killerCar[i].isInside(dumbcat.getX() - 5, dumbcat.getY() + 5))
						|| (killerCar[i].isInside(dumbcat.getX() + 5, dumbcat.getY() + 5))) {
					dumbcat.setPosition(250, 675);
					count++;
					if (count == 1 || count == 2 || count == 3)
						catLives--;
					count = 0;
					CarHit.play();
					Death.play();
				}
				if ((laser[i].isInside(dumbcat.getX() - 5, // (CW)
						dumbcat.getY() - 5)) || (laser[i].isInside(dumbcat.getX() + 5, dumbcat.getY() - 5))
						|| (laser[i].isInside(dumbcat.getX() - 5, dumbcat.getY() + 5))
						|| (laser[i].isInside(dumbcat.getX() + 5, dumbcat.getY() + 5))) {
					dumbcat.setPosition(250, 675);
					count++;
					if (count == 1 || count == 2 || count == 3)
						catLives--;
					count = 0;
					LaserHit.play();
					Death.play();
				}

			}

			// If you run out of lives then it's gameover and CAT_DEAD cutscene starts (DN)
			if (catLives == 0) {
				cat_state = CAT_DEAD;
				Slayer.stop();
				BadDay.loop();

			}
			EZ.refreshScreen();
		}

		// Creates the gameover cutscene with it's corresponding text (PS)
		if (cat_state == CAT_DEAD) {
			EZLine loseScene;
			loseScene = EZ.addLine(0, 0, 500, 700, Color.BLACK, 1000);
			Cat dumbcatlose = new Cat("images/dumbcat.png", 250, 350);
			EZText endText = EZ.addText(250, 250, "YAH, YOU BE A DUMB CAT...", c3, fontsize);
			EZText endText2 = EZ.addText(250, 470, "GO TO COLLEGE YA KITTY!", c3, fontsize);
			EZText scoreText = EZ.addText(70, 50, "Cats Saved: " + score, c3, fontsize);
			EZText livesText = EZ.addText(420, 50, "Cats Wasted: " + (9 - catLives), c3, fontsize);
		}
		// Creates the Win cutscene ith its corresponding text (PS)
		if (cat_state == CAT_WINS) {
			EZLine winScene;
			winScene = EZ.addLine(0, 0, 500, 700, Color.BLACK, 1000);
			Cat dumbcatwin = new Cat("images/dumbcat.png", 250, 350);
			EZText winText = EZ.addText(250, 250, "OK, YOU NOT DUMB :)", c, fontsize);
			EZText winText2 = EZ.addText(250, 470, "WHAT A SMART KITTY!", c, fontsize);
			EZText scoreText = EZ.addText(70, 50, "Cats Saved: " + score, c2, fontsize);
			EZText livesText = EZ.addText(420, 50, "Cats Wasted: " + (9 - catLives), c2, fontsize);
		}
	}
}