package io.medfer.helloworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    public void create(){
        // Start by instantiating a SpriteBatch and BitmapFont
        //SpriteBatch used to render objects on the screen.
        batch = new SpriteBatch();
        // Uses bitmap files to render fonts on screen
        font = new BitmapFont();
        // We set the screen for the game to the main menu screen object
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        //This supersedes the create() method override in the main game file in the Game class.
        //Will follow GameScreen.java
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
