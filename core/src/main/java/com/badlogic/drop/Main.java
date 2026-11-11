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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {

    private static final float SQUARE_SIZE = 48f;
    private static final float NORMAL_SPEED = 240f;
    private static final float SPRINT_SPEED = 480;
    private static final float ROAD_WIDTH = 1400f;

    // These are the original shape integrations, showing the original concept
    private ShapeRenderer shapes;
    private float x_pos = 48f;
    private float y_pos = 0f;

    // For FitViewport, we could potentially later change this to
    // an ExtendViewport
    SpriteBatch spriteBatch;
    private Texture redCarTexture;
    private Texture background;
    private FitViewport viewport;

    @Override
    public void create() {
        shapes = new ShapeRenderer();

        redCarTexture = new Texture("red-car.png");
        background = new Texture("temporary/pixel_art_grass_field.png");
        spriteBatch = new SpriteBatch();
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

        // deltaTime is essentially helping us to make sure that regardless of hardware (for the most part),
        // everyone's vehicles and environments are moving at the same speed/distance.
        float deltaTime = Gdx.graphics.getDeltaTime();
        logic(deltaTime);

        int browserWidth = Gdx.graphics.getWidth();
        int browserHeight = Gdx.graphics.getHeight();

        input();
        logic(deltaTime);
        draw(browserWidth, browserHeight);


        // clamp the movement values so the "player" cannot escape
        // A clamp is basically trapping something between a min and a max value
        x_pos = MathUtils.clamp(x_pos, (float)(browserWidth * 0.5 - ROAD_WIDTH * 0.5), (float)(browserWidth * 0.5 + ROAD_WIDTH * 0.5) - SQUARE_SIZE);
        y_pos = MathUtils.clamp(y_pos, 0, browserHeight - (int)SQUARE_SIZE);

    }

    private void input(){

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
        spriteBatch.begin();

        spriteBatch.draw(background, 0, 0, browserWidth, browserHeight);
        spriteBatch.draw(redCarTexture, 100, 100, 64, 128);

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
        shapes.dispose();
    }
}
