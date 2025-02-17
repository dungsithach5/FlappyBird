package com.example.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flapState = 0;

	float birdY = 0;
	float velocity = 0;
	int  gameState = 0;
	float gravity = 2;

	Texture topTube, bottomTube;
	float gap = 400;



	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
	}

	@Override
	public void render () {

		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState != 0) {
			if(Gdx.input.justTouched()) {
				velocity = -20;
			}

			batch.draw(topTube,Gdx.graphics.getWidth()/2 - topTube.getWidth()/2, Gdx.graphics.getHeight()/2 + gap/2);
			batch.draw(bottomTube,Gdx.graphics.getWidth()/2 - bottomTube.getWidth()/2, Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight());

			if(birdY > 0 || velocity < 0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}


		} else {
			if(Gdx.input.justTouched()) {
				Gdx.app.log("touched","yes you touched me");
				gameState = 1;
			}
		}

		if(flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}

		batch.draw(birds[flapState],Gdx.graphics.getWidth()/2,birdY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
