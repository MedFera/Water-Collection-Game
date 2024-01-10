package io.medfer.helloworld;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.lang.Math;


public class GameScreen implements Screen {
    final Drop game;

    Texture emptyHeart;
    Texture fullHeart;
    Texture dropImage;
    Texture bucketImage;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    long lastDropTime;
    public int dropsGathered;
    private int dropsMissed;
    int nextDropTime = 700000000;
    int dropSpeed = 300;


    public GameScreen(final Drop game) {
        this.game = game;

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        emptyHeart = new Texture(Gdx.files.internal("emptyHeart.png"));
        fullHeart = new Texture(Gdx.files.internal("fullHeart.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("dropSND.mp3"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rainSND.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        bucket.width = 64;
        bucket.height = 64;

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<Rectangle>();
        spawnRaindrop();

    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();

    }

    private void changeDifficulty(){
        if(this.dropsGathered > 15){
            this.nextDropTime = 600000000;
            this.dropSpeed = 325;
        }
        else if(this.dropsGathered > 30){
            this.nextDropTime = 550000000;
            this.dropSpeed = 350;
        }
        else if(this.dropsGathered > 50){
            this.nextDropTime = 500000000;
            this.dropSpeed = 375;
        }
        else if(dropsGathered > 70){
            this.nextDropTime = 450000000;
            this.dropSpeed = 400;
        }
        else if(dropsGathered > 90){
            this.nextDropTime = 350000000;
            this.dropSpeed = 500;
        }
        else if(dropsGathered > 110){
            this.nextDropTime = 300000000;
            this.dropSpeed = 550;
        }
        else if(dropsGathered > 130){
            this.nextDropTime = 350000000;
            this.dropSpeed = 600;
        }
        else if(dropsGathered > 160){
            this.nextDropTime = 300000000;
        }
        else if(dropsGathered > 190){
            this.nextDropTime = 200000000;
            this.dropSpeed = 650;
        }
    }


    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 10, 470);
        switch (dropsMissed) {
            case 0:
                game.batch.draw(fullHeart, 10, 430);
                game.batch.draw(fullHeart, 40, 430);
                game.batch.draw(fullHeart, 70, 430);
                break;
            case 1:
                game.batch.draw(fullHeart, 10, 430);
                game.batch.draw(fullHeart, 40, 430);
                game.batch.draw(emptyHeart, 70, 430);
                break;
            case 2:
                game.batch.draw(fullHeart, 10, 430);
                game.batch.draw(emptyHeart, 40, 430);
                game.batch.draw(emptyHeart, 70, 430);
                break;
            default:
                LossScreen.setFinalScore(dropsGathered);
                game.setScreen(new LossScreen(game));
                dispose();
                break;
        }

        game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        game.batch.end();

        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            bucket.x -= 400 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            bucket.x += 400 * Gdx.graphics.getDeltaTime();

        // make sure the bucket stays within the screen bounds
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > nextDropTime)
            spawnRaindrop();


        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add a sound effect.
        Iterator<Rectangle> iter = raindrops.iterator();
            while (iter.hasNext()) {
                Rectangle raindrop = iter.next();
                raindrop.y -= dropSpeed * Gdx.graphics.getDeltaTime();
                if (raindrop.y + 64 < 0) {
                    iter.remove();
                    dropsMissed++;
                }
                if (raindrop.overlaps(bucket)) {
                    dropsGathered++;
                    dropSound.play();
                    iter.remove();
                }
            }

        changeDifficulty();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();
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
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        emptyHeart.dispose();
        fullHeart.dispose();
    }

}
