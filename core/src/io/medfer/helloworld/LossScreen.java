package io.medfer.helloworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.concurrent.TimeUnit;

public class LossScreen implements Screen {

    final Drop game;
    static int finalScore;
    OrthographicCamera camera;


    public static void setFinalScore(int num){
        finalScore = num;
    }

    // No create method in Main Menu Class so we use a constructor method
    // Instance of Drop used so we can call upon its methods and fields
    public LossScreen(final Drop game) {
        //Assign variable to game Drop
        this.game = game;
        //Creates new Camera
        camera = new OrthographicCamera();
        //Sets camera parameters
        camera.setToOrtho(false,800, 400);
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0,0,0.2f,1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "You lose!",360,240);
        game.font.draw(game.batch, "Tap anywhere to play again.",305,200);
        game.font.draw(game.batch, "You collected: " + finalScore + " Drops",320,160);
        game.batch.end();

        if (Gdx.input.justTouched()) {
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