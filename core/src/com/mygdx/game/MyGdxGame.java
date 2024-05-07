package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] birds;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;
    int gameState = 0;
    float gravity = 2;

    Texture topTube, bottomTube;
    float gap = 400;

    Texture gameOver;

    int numberOfTube = 4;
    float[] tubeOffset = new float[numberOfTube];
    Random random = new Random();
    float[] tubeX = new float[numberOfTube];
    float tubeVelocity = 12;
    float distanceBetweenTubes;
    int scores = 0;
    int scoringTube = 0;
    BitmapFont bitmapFont;

    ShapeRenderer shapeRenderer;
    Circle circle;

    Rectangle[] topTubeRectangle;
    Rectangle[] bottomTubeRectangle;

    private Sound soundWing, soundPoint, soundHit;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");

        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getData().scale(10);

        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        gameOver = new Texture("game_over.png");

        shapeRenderer = new ShapeRenderer();
        circle = new Circle();

        topTubeRectangle = new Rectangle[numberOfTube];
        bottomTubeRectangle = new Rectangle[numberOfTube];

        distanceBetweenTubes = Gdx.graphics.getWidth() / 4;

        for (int i = 0; i < numberOfTube; i++) {
            tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 300);

            tubeX[i] = (float) Gdx.graphics.getWidth() - topTube.getWidth() / 2 + i * distanceBetweenTubes;

            topTubeRectangle[i] = new Rectangle();
            bottomTubeRectangle[i] = new Rectangle();
            soundWing = Gdx.audio.newSound(Gdx.files.internal("wing.ogg"));
            soundHit = Gdx.audio.newSound(Gdx.files.internal("hit.ogg"));
            soundPoint = Gdx.audio.newSound(Gdx.files.internal("point.ogg"));
        }
    }

    public void renew() {
        velocity = 0;
        scores = 0;
        scoringTube = 0;

        for (int i = 0; i < numberOfTube; i++) {
            tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 300);

            tubeX[i] = Gdx.graphics.getWidth() - topTube.getWidth() / 2 + i * distanceBetweenTubes;
        }
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTube; i++) {
            tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 300);

            tubeX[i] = (float) Gdx.graphics.getWidth() - topTube.getWidth() / 2 + i * distanceBetweenTubes;

            topTubeRectangle[i] = new Rectangle();
            bottomTubeRectangle[i] = new Rectangle();
        }
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenUtils.clear(1, 0, 0, 1);

        //Game starts
        if (gameState == 1) {

            //Render hoạt ảnh vỗ cánh
            if (flapState == 0) {
                flapState = 1;
            } else {
                flapState = 0;
            }

            if (Gdx.input.justTouched()) {
                velocity = -25;
                soundWing.play(0.5f);
            }

            //Tube chạy qua 1 nửa màn hình sẽ +1đ
            if (tubeX[scoringTube] < (Gdx.graphics.getWidth() / 2 - birds[0].getWidth())) {
                scores++;
                soundPoint.play(1f);

                if (scoringTube < numberOfTube - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

            for (int i = 0; i < numberOfTube; i++) {
                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += numberOfTube * distanceBetweenTubes;
                } else {
                    tubeX[i] -= tubeVelocity;
                }

                batch.draw(topTube, tubeX[i],
                        Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i],
                        Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangle[i] = new Rectangle(tubeX[i],
                        Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],
                        topTube.getWidth(),
                        topTube.getHeight());
                bottomTubeRectangle[i] = new Rectangle(tubeX[i],
                        Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
                        bottomTube.getWidth(),
                        bottomTube.getHeight());
            }

            if (birdY > 0) {
                velocity += gravity;
                birdY -= velocity;
            } else {
                gameState = 3;
            }

        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 3) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                renew();
                gameState = 1;
            }
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, birdY);

        bitmapFont.draw(batch, Integer.toString(scores), 200, 200);

        batch.end();

        circle.set(Gdx.graphics.getWidth() / 2
                ,birdY + birds[flapState].getWidth() / 2
                ,birds[flapState].getWidth() / 2);

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(circle.x,circle.y,circle.radius);

        for (int i = 0 ; i < numberOfTube ; i++) {
/*
			shapeRenderer.rect(tubeX[i],
					Gdx.graphics.getHeight() / 2  + gap / 2 + tubeOffset[i],
					topTube.getWidth(),
					topTube.getHeight());

			shapeRenderer.rect(tubeX[i],
					Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
					bottomTube.getWidth(),
					bottomTube.getHeight());
*/
            if (Intersector.overlaps(circle,topTubeRectangle[i])
                    || Intersector.overlaps(circle,bottomTubeRectangle[i]) ) {
                gameState = 3;
                renew();
                soundHit.play(1f);
            }

        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
    }
}
