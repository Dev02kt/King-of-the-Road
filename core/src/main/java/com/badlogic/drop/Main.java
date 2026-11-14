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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {

    private static final float VEHICLE_SIZE = 128f;
    private static final float NORMAL_SPEED = 180f;
    private static final float SPRINT_SPEED = 260f;


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
        logic(browserWidth, browserHeight);
        draw(browserWidth, browserHeight);

    }

    private void input(){
        // deltaTime is essentially helping us to make sure that regardless of hardware (for the most part),
        // everyone's vehicles and environments are moving at the same speed/distance.
        float delta = Gdx.graphics.getDeltaTime();
        float currentSpeed = delta * (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? SPRINT_SPEED : NORMAL_SPEED);


        // The movement logic of the vehicle
        if(Gdx.input.isKeyPressed(Keys.W)){
            redCar.translateY(currentSpeed);
        }
        else if(Gdx.input.isKeyPressed(Keys.S)){
            redCar.translateY(-currentSpeed);
        }

        if(Gdx.input.isKeyPressed(Keys.A)){
            redCar.translateX(-currentSpeed);
        }
        else if(Gdx.input.isKeyPressed(Keys.D)){
            redCar.translateX(currentSpeed);
        }
    }

    private void logic(int browserWidth, int browserHeight){
        // This is where we are clamping the car to the road
        redCar.setX(MathUtils.clamp(redCar.getX(), (float)browserWidth / 4, ((float)browserWidth / 2) * (float)(1.5) - VEHICLE_SIZE));
        redCar.setY(MathUtils.clamp(redCar.getY(), 0, browserHeight - VEHICLE_SIZE));
    }

    // This method is in charge of displaying the sprites onto the game screen
    private void draw(int browserWidth, int browserHeight){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);


        spriteBatch.begin();

        spriteBatch.draw(background, 0, 0, browserWidth, browserHeight);
        spriteBatch.draw(road, (float)browserWidth / 4, 0, (float)browserWidth / 2, browserHeight);
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
