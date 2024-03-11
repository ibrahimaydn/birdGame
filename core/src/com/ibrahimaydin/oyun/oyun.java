package com.ibrahimaydin.oyun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class oyun extends ApplicationAdapter {

    SpriteBatch batch;
    Texture arkaPlan;
    Texture bird;
    Texture dusman1;
    Texture dusman2;
    Texture dusman3;
    float birdX = 0;
    float birdY = 0;
    int gameState = 0;
    float birdSpeed = 0;
    float gravity = 0.15f;
    float enemyVelocity = 4;
    Random random;
    int score = 0;
    int bestScore = 0;
    Preferences preferences;

    int dusmanSkor = 0;
    BitmapFont font;
    BitmapFont font2;

    Circle birdCircle;

    ShapeRenderer shapeRenderer;

    int numberOfEnemies = 4;
    float[] dusmanX = new float[numberOfEnemies];
    float[] dusmanOffSet = new float[numberOfEnemies];
    float[] dusmanOffSet2 = new float[numberOfEnemies];
    float[] dusmanOffSet3 = new float[numberOfEnemies];
    float distance = 0;

    Circle[] dusmanCircles;
    Circle[] dusmanCircles2;
    Circle[] dusmanCircles3;


    @Override
    public void create() {
        batch = new SpriteBatch();
        arkaPlan = new Texture("arkaplan.png");
        bird = new Texture("bird.png");
        dusman1 = new Texture("dusman.png");
        dusman2 = new Texture("dusman.png");
        dusman3 = new Texture("dusman.png");

        preferences = Gdx.app.getPreferences("MyGamePreferences");
        bestScore = preferences.getInteger("bestScore", 0);

        distance = Gdx.graphics.getWidth() / 2;
        random = new Random();


        birdX = Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2;
        birdY = Gdx.graphics.getHeight() / 3;


        shapeRenderer = new ShapeRenderer();


        birdCircle = new Circle();
        dusmanCircles = new Circle[numberOfEnemies];
        dusmanCircles2 = new Circle[numberOfEnemies];
        dusmanCircles3 = new Circle[numberOfEnemies];


        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(4);

        font2 = new BitmapFont();
        font2.setColor(Color.WHITE);
        font2.getData().setScale(6);


        for (int i = 0; i < numberOfEnemies; i++) {


            dusmanOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            dusmanOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            dusmanOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

            dusmanX[i] = Gdx.graphics.getWidth() - dusman1.getWidth() / 2 + i * distance;


            dusmanCircles[i] = new Circle();
            dusmanCircles2[i] = new Circle();
            dusmanCircles3[i] = new Circle();

        }


    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(arkaPlan, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {


            if (dusmanX[dusmanSkor] < Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2) {
                score++;
                bestScore = score;
                preferences.putInteger("bestScore", bestScore);
                preferences.flush(); // Saves the preferences immediately
                if (dusmanSkor < numberOfEnemies - 1) {
                    dusmanSkor++;
                } else {
                    dusmanSkor = 0;
                }

            }


            if (Gdx.input.justTouched()) {

                birdSpeed = -7;

            }
            // Engelleme: Kuşun yukarı çıkmasını engelle
            if (birdY < Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 10) {
                birdSpeed = birdSpeed + gravity;
                birdY = birdY - birdSpeed;
            } else {
                birdY = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 10;

                // Yere değdiği anda hemen düşmeye başlamak için velocity'yi sıfırlayın
                birdSpeed = 0;

            }
            for (int i = 0; i < numberOfEnemies; i++) {


                if (dusmanX[i] < Gdx.graphics.getWidth() / 15) {
                    dusmanX[i] = dusmanX[i] + numberOfEnemies * distance;

                    dusmanOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    dusmanOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    dusmanOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);


                } else {
                    dusmanX[i] = dusmanX[i] - enemyVelocity;
                }


                batch.draw(dusman1, dusmanX[i], Gdx.graphics.getHeight() / 2 + dusmanOffSet[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
                batch.draw(dusman2, dusmanX[i], Gdx.graphics.getHeight() / 2 + dusmanOffSet2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
                batch.draw(dusman3, dusmanX[i], Gdx.graphics.getHeight() / 2 + dusmanOffSet3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);


                dusmanCircles[i] = new Circle(dusmanX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + dusmanOffSet[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
                dusmanCircles2[i] = new Circle(dusmanX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + dusmanOffSet2[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
                dusmanCircles3[i] = new Circle(dusmanX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + dusmanOffSet3[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);


            }


            if (birdY > 0) {
                birdSpeed = birdSpeed + gravity;
                birdY = birdY - birdSpeed;
            } else {
                gameState = 2;
            }


        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {

            font2.draw(batch, "Game Over! Tap To Play Again!", 200, Gdx.graphics.getHeight() / 2);

            if (Gdx.input.justTouched()) {
                gameState = 1;

                birdY = Gdx.graphics.getHeight() / 3;


                for (int i = 0; i < numberOfEnemies; i++) {


                    dusmanOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    dusmanOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    dusmanOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

                    dusmanX[i] = Gdx.graphics.getWidth() - dusman1.getWidth() / 2 + i * distance;


                    dusmanCircles[i] = new Circle();
                    dusmanCircles2[i] = new Circle();
                    dusmanCircles3[i] = new Circle();

                }

                birdSpeed = 0;
                dusmanSkor = 0;
                score = 0;

            }
        }


        batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);

        font.draw(batch, String.valueOf(score), 100, 200);
        font.draw(batch, String.valueOf("Best skor: " + bestScore), 100, 100);

        batch.end();

        birdCircle.set(birdX + Gdx.graphics.getWidth() / 30, birdY + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.BLACK);
        //shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);


        for (int i = 0; i < numberOfEnemies; i++) {
            //shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30,  Gdx.graphics.getHeight()/2 + enemyOffSet[i] + Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
            //shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30,  Gdx.graphics.getHeight()/2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
            //shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30,  Gdx.graphics.getHeight()/2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);

            if (Intersector.overlaps(birdCircle, dusmanCircles[i]) || Intersector.overlaps(birdCircle, dusmanCircles2[i]) || Intersector.overlaps(birdCircle, dusmanCircles3[i])) {
                gameState = 2;
            }
        }

        //shapeRenderer.end();

    }

    @Override
    public void dispose() {

    }
}