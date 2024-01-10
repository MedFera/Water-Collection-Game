package io.medfer.helloworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {

    final Drop game;
    OrthographicCamera camera;
    Button startGame;
    Button optionsMenu;
    Button exitGame;
    Stage stage;



    // No create method in Main Menu Class so we use a constructor method
    // Instance of Drop used so we can call upon its methods and fields
    public MainMenuScreen(final Drop game) {
        //Assign variable to game Drop
        this.game = game;
        //Creates new Camera
        camera = new OrthographicCamera();
        //Sets camera parameters
        camera.setToOrtho(false,800, 400);

        stage = new Stage(new ScreenViewport(camera));
        startGame = new Button();
        optionsMenu = new Button();
        exitGame = new Button();


        stage.addActor(startGame);
        stage.addActor(optionsMenu);
        stage.addActor(exitGame);
        stage.act();
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0,0,0.2f,1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Drop Game!!!",100,150);
        game.font.draw(game.batch, "Tap anywhere to begin",100,100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
