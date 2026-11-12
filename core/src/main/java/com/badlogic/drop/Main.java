/*
    THINGS TO RESEARCH:
    - FitViewport() -> this seems to be an interesting method from libGDX that might solve
                       some of our potential issues
 */


package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static jdk.internal.org.jline.terminal.spi.SystemStream.Input;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {

    // SQUARE_SIZE
    private static final float VEHICLE_SIZE = 128f;
    private static final float NORMAL_SPEED = 240f;
    private static final float SPRINT_SPEED = 480;
    private static final float ROAD_WIDTH = 1400f;

    private float x_pos = 128f;
    private float y_pos = 0f;

    // For FitViewport, we could potentially later change this to
    // an ExtendViewport
    private SpriteBatch spriteBatch;
    private Sprite redCar;
    private Texture redCarTexture;
    private Texture background;
    private Texture road;
    private FitViewport viewport;

    @Override
    public void create() {
        redCarTexture = new Texture("red-car.png");
        background = new Texture("temporary/pixel_art_grass_field.png");
        road = new Texture("temporary/road.png");
        spriteBatch = new SpriteBatch();

        // For object's we will wish to rotate, scale, etc. it is better to be done as a Sprite
        redCar = new Sprite(redCarTexture);
        redCar.setSize(128, 128);
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        viewport.update(width, height, true);

        // Resize your application here. The parameters represent the new window size.
    }

    @Override
    public void render() {

        int browserWidth = Gdx.graphics.getWidth();
        int browserHeight = Gdx.graphics.getHeight();

        input();
        logic();
        draw(browserWidth, browserHeight);

    }

    private void input(){
        // deltaTime is essentially helping us to make sure that regardless of hardware (for the most part),
        // everyone's vehicles and environments are moving at the same speed/distance.

        // The movement logic of the vehicle
        if(Gdx.input.isKeyPressed(Keys.W)){
            //y_pos +=
        }
        else if(Gdx.input.isKeyPressed(Keys.S)){

        }
        else if(Gdx.input.isKeyPressed(Keys.A)){

        }
        else if(Gdx.input.isKeyPressed(Keys.D)){

        }
    }

    private void logic(float deltaTime)
    {
        // We will eventually need to potentially separate this I believe because sprint/nitro
        // should be capped or on a timer
        float current_speed = deltaTime * ( Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? SPRINT_SPEED : NORMAL_SPEED );


        // vertical movement
        if(Gdx.input.isKeyPressed(Keys.W))
            y_pos += current_speed;
        else if(Gdx.input.isKeyPressed(Keys.S))
            y_pos -= current_speed;

        // horizontal movement
        if(Gdx.input.isKeyPressed(Keys.A))
            x_pos -= current_speed;
        else if(Gdx.input.isKeyPressed(Keys.D))
            x_pos += current_speed;
    }

    private void logic(){

    }

    // This method is in charge of displaying the sprites onto the game screen
    private void draw(int browserWidth, int browserHeight){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        // clamp the movement values so the "player" cannot escape
        // A clamp is basically trapping something between a min and a max value
        x_pos = MathUtils.clamp(x_pos, (float)(browserWidth * 0.5 - ROAD_WIDTH * 0.5), (float)(browserWidth * 0.5 + ROAD_WIDTH * 0.5) - VEHICLE_SIZE);
        y_pos = MathUtils.clamp(y_pos, 0, browserHeight - (int)VEHICLE_SIZE);

        spriteBatch.begin();

        spriteBatch.draw(background, 0, 0, browserWidth, browserHeight);
        spriteBatch.draw(road, (float)browserWidth / 3, 0, (float)browserWidth / 3, browserHeight);
        redCar.draw(spriteBatch); // This is how to draw sprites

        spriteBatch.end();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
