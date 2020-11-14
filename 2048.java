package com.codegym.games.game2048;
import com.codegym.engine.cell.*;
import java.util.*;

public class Game2048 extends Game {
    
    
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;
    

    
    
@Override 
public void initialize(){
    setScreenSize(SIDE, SIDE);
    createGame();
    drawScene();
    
    
}

private void win(){
    isGameStopped = true;
    showMessageDialog(Color.CYAN, "Congrat's you Won", Color.YELLOW, 35);
}

private void createGame(){
    gameField = new int[4][4];
    createNewNumber();
    createNewNumber();
    
}

private void drawScene(){
    for(int i =0; i < SIDE; i++){
        for(int j = 0; j< SIDE; j++){
        setCellColoredNumber(j, i, gameField[i][j]);
            
        }
    }
}

private void createNewNumber(){
    int randomCellX = getRandomNumber(SIDE);
    int randomCellY = getRandomNumber(SIDE);
    
    if( gameField[randomCellX][randomCellY] != 0){
        createNewNumber();
        
    }else{
        
       int value = getRandomNumber(10);
       if(value == 9){
           gameField[randomCellX][randomCellY] = 4;
       }else{
           gameField[randomCellX][randomCellY] = 2;
       }
    }
    if(getMaxTileValue() == 2048)
    win();
 }


private Color getColorByValue(int value){
    Color color = null;
    switch(value){
        case 0:
         color = Color.WHITE;
         break;
        case 2:
         color = Color.YELLOW;
        break;
        case 4:
         color = Color.ALICEBLUE;
        break;
        case 8:
         color = Color.BISQUE;
        break;
        case 16:
          color = Color.BLUE;
        break;
        case 32:
          color = Color.GREEN;
        break;
        case 64:
          color = Color.PURPLE;
        break;
        case 128:
          color = Color.RED;
        break;
        case 256:
          color = Color.BROWN;
        break;
        case 512:
          color = Color.AQUAMARINE;
        break;
        case 1024:
          color = Color.ORANGE;
        break;
        case 2048:
          color = Color.PINK;
        break;
       
    }
    return color;
}

private void setCellColoredNumber(int x, int y, int value){
        Color colorValue = getColorByValue(value);
        if(value > 0){
            setCellValueEx(x, y, colorValue, Integer.toString(value));   
        }else{
            setCellValueEx(x, y, colorValue, "");
        }
}


private boolean compressRow(int[] row){
    // int col = -1;
    int length = row.length;
    boolean hasChanged = false;
    
     for(int x=0; x< length; x++){
     
     for (int j = 0; j< length * length; j++){
         int i = j % length;
         
         if(i == length -1){
             continue;
         }
         if(row[i] == 0 && row[i +1] !=0){
             row[i] = row[i+1];
             row[i+1] =0;
             hasChanged = true;
         }
     }
     }
     return hasChanged;
    
}

private boolean mergeRow(int[] row){
    boolean moved = false;
    for (int i=0; i< row.length-1;i++)
        if ((row[i] == row[i+1])&&(row[i]!=0)){
            row[i] = 2*row[i];
            row[i+1] = 0;
            score += (row[i] + row[i+1]);
            setScore(score);
            moved = true;


        }

    return moved;
}  
private void moveLeft(){
    
    int count = 0;
    
    for(int i =0; i < SIDE; i++){
        
        boolean compress = compressRow(gameField[i]);
        boolean merge = mergeRow(gameField[i]);
        boolean compresses = compressRow(gameField[i]);
        
        if(compress || merge || compresses){
            count ++;
        }
    }
        if(count != 0){
            createNewNumber();
        }
    
}


private void moveRight(){
    rotateClockwise();
    rotateClockwise();
    moveLeft();
    rotateClockwise();
    rotateClockwise();
}
private void moveUp(){
    rotateClockwise();
    rotateClockwise();
    rotateClockwise();
    moveLeft();
    rotateClockwise();
    
}
private void moveDown(){
    rotateClockwise();
    moveLeft();
    rotateClockwise();
    rotateClockwise();
    rotateClockwise();
}

private void rotateClockwise(){
    for (int i =0; i < SIDE; i++){
        for (int j = i; j<SIDE - i - 1; j++){
            int temp = gameField[i][j];
            gameField[i][j] = gameField[SIDE -1 - j][i];
            gameField[SIDE -1 - j][i] = gameField[SIDE -1 -i][SIDE -1 - j];
            gameField[SIDE -1 -i][SIDE -1 - j] = gameField[j][SIDE - 1- i];
            gameField[j][SIDE -1 -i] = temp;
        }
    }
}

private int getMaxTileValue(){
    int max = 0;
    for (int i = 0; i<SIDE; i++){
        for(int j=0; j<SIDE; j++){
            if(gameField[i][j] > max){
                max = gameField[i][j];
            }
        }
    }
    return max;
}

private boolean canUserMove(){
    boolean hasMove = false;
    for (int x = 0; x <SIDE; x++){
        for(int y = 0; y < SIDE; y++){
            if(gameField[x][y] == 0){
                hasMove = true;
            }
        }
    }
    for (int x = 0; x <SIDE-1; x++){
        for(int y = 0; y < SIDE; y++){
            if(gameField[x][y] == gameField[x+1][y]){
                hasMove = true;
            }
        }
    }
    for (int x = 0; x <SIDE; x++){
        for(int y = 0; y < SIDE-1; y++){
            if(gameField[x][y] == gameField[x][y+1]){
                hasMove = true;
            }
        }
    }
    return hasMove;
}

private void gameOver(){
    isGameStopped = true;
    showMessageDialog(Color.GRAY, "You have no more Moves, You Lost", Color.RED, 34);
}

  @Override
    public void onKeyPress(Key key){
        if(isGameStopped){
            
            if(key == Key.SPACE){
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            }
        }
        else if( canUserMove() ){
        
            if (key == Key.LEFT){
            moveLeft();
            }
            else if (key == Key.RIGHT){
            moveRight();
            }
            else if (key == Key.UP){
                moveUp();
            }
            else if (key == Key.DOWN){
            moveDown();
            }
            drawScene();
            }
        else{
            gameOver(); }
    }
}