package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {

    private static final float SQUARE_SIZE = 48f;
    private static final float NORMAL_SPEED = 240f;
    private static final float SPRINT_SPEED = 480;

    private ShapeRenderer shapes;
    private float x_pos = 48f;
    private float y_pos = 0f;

    @Override
    public void create() {
        shapes = new ShapeRenderer();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
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

    private static final float ROAD_WIDTH = 1400f;

    @Override
    public void render() {
        ScreenUtils.clear(0f, 150/255f, 0f, 255f);

        // deltaTime is essentially helping us to make sure that regardless of hardware (for the most part),
        // everyone's vehicles and environments are moving at the same speed/distance.
        float deltaTime = Gdx.graphics.getDeltaTime();
        logic(deltaTime);

        // width and height of the visible screen given the current size
        // subject to change on window resize?
        int width  = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // clamp the movement values so the "player" cannot escape
        // A clamp is basically trapping something between a min and a max value
        x_pos = MathUtils.clamp(x_pos, (float)(width * 0.5 - ROAD_WIDTH * 0.5), (float)(width * 0.5 + ROAD_WIDTH * 0.5) - SQUARE_SIZE);
        y_pos = MathUtils.clamp(y_pos, 0, height - (int)SQUARE_SIZE);

        // drawling slop
        shapes.getProjectionMatrix().setToOrtho2D(0f, 0f, width, height);

        shapes.begin(ShapeRenderer.ShapeType.Filled);

            // background

            // road part
            shapes.setColor(100 / 255f, 100/255f, 100/255f, 1f);
            shapes.rect( (float)(width * 0.5 - ROAD_WIDTH * 0.5), 0, ROAD_WIDTH, height);

            // the yellow lines for the road
            shapes.setColor(1f, 1f, 0f, 255f);
            shapes.rect( (float)(width * 0.5) - 10f, 0, 5, height );
            shapes.rect( (float)(width * 0.5) + 10f, 0, 5, height );

            // the "player"
            shapes.setColor(255, 0, 0,255);
            shapes.rect(x_pos, y_pos, SQUARE_SIZE, SQUARE_SIZE);

        shapes.end();

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
