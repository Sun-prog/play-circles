package com.example.rgb;

import java.util.ArrayList;

public class GameManager {
    public static final int MAX_CIRCLES = 10;
    private MainCircle mainCircle;
private ArrayList<EnemyCircle> circles;
    private CanvasView canvasView;
private static int  width;
private static  int height;

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public GameManager(CanvasView canvasView, int w, int h) {
        this.canvasView = canvasView;
        width = w;
        height = h;
        initMainCircle();
initEnemyCircles();

    }

    private void initEnemyCircles() {
        SimpleCircle mainCircleArea = mainCircle.getCircleArea();
        circles = new ArrayList<EnemyCircle>();
        for (int i = 0; i< MAX_CIRCLES; i++){
            EnemyCircle circle;


            do {
                circle = EnemyCircle.getRandomCircle();
            } while (circle.isIntersect(mainCircleArea));
            circles.add(circle);
        }
        calculateAndSetCirclesColor();
    }

    private void calculateAndSetCirclesColor() {
        for (EnemyCircle circle:circles){
            circle.setEnemyOrFoodColorDependsOn(mainCircle);
        }
    }

    private  void initMainCircle(){
        mainCircle = new MainCircle(width/2 ,height/2 );
    }

    public void onDraw() {
        canvasView.drawCircle(mainCircle);
        for (EnemyCircle circle :circles){
            canvasView.drawCircle(circle);
        }
    }

    public void onTouchEvent(int x, int y) {
        mainCircle.moveMainCircleWhenTouchAt(x,y);
        checkColision();
        moveCircles();
    }

    private void checkColision() {
        SimpleCircle circleForDel = null;
        for (EnemyCircle circle:circles){
            if (mainCircle.isIntersect(circle)){
                if (circle.isSmallerThan(mainCircle)){
                    mainCircle.growRadius(circle);
                    circleForDel = circle;
                    calculateAndSetCirclesColor();
                } else{
                    gameEnd("YOU LOSE");
                }
            }
        }
        if (circleForDel!=null) {
            circles.remove(circleForDel);
        }
        if (circles.isEmpty()){
            gameEnd("YOU WIN");
        }

    }

    private void gameEnd(String text) {
canvasView.showMessage(text);
        mainCircle.initRadius();
        initEnemyCircles();
        canvasView.redraw();
    }

    private void moveCircles() {
        for (EnemyCircle circle: circles){
            circle.moveOnStep();
        }
    }
}
