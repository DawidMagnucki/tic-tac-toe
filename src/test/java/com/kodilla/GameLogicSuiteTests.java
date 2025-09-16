package com.kodilla;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameLogicSuiteTests {

    @Test
    void testOWinsInRow() {
        //Given
        GameLogic game = new GameLogic();
        //When
        game.makeMove(0,0,'O');
        game.makeMove(0,1,'O');
        game.makeMove(0,2,'O');
        //Then
        assertTrue(game.checkWin('O'));
    }

    @Test
    void testOWinsInColumn() {
        //Given
        GameLogic game = new GameLogic();
        //When
        game.makeMove(0,1,'O');
        game.makeMove(1,1,'O');
        game.makeMove(2,1,'O');
        //Then
        assertTrue(game.checkWin('O'));
    }

    @Test
    void testOWinsInDiagonal() {
        //Given
        GameLogic game = new GameLogic();
        //When
        game.makeMove(0,0,'O');
        game.makeMove(1,1,'O');
        game.makeMove(2,2,'O');
        //Then
        assertTrue(game.checkWin('O'));
    }

    @Test
    void testXWinsInRow() {
        //Given
        GameLogic game = new GameLogic();
        //When
        game.makeMove(2,0,'X');
        game.makeMove(2,1,'X');
        game.makeMove(2,2,'X');
        //Then
        assertTrue(game.checkWin('X'));
    }

    @Test
    void testXWinsInColumn() {
        //Given
        GameLogic game = new GameLogic();
        //When
        game.makeMove(0,2,'X');
        game.makeMove(1,2,'X');
        game.makeMove(2,2,'X');
        //Then
        assertTrue(game.checkWin('X'));
    }

    @Test
    void testXWinsInDiagonal() {
        //Given
        GameLogic game = new GameLogic();
        //When
        game.makeMove(0,2,'X');
        game.makeMove(1,1,'X');
        game.makeMove(2,0,'X');
        //Then
        assertTrue(game.checkWin('X'));
    }

    @Test
    void testDraw() {
        //Given
        GameLogic game = new GameLogic();
        //When
        game.makeMove(0,0,'X');
        game.makeMove(0,1,'O');
        game.makeMove(0,2,'X');
        game.makeMove(1,0,'X');
        game.makeMove(1,1,'O');
        game.makeMove(1,2,'O');
        game.makeMove(2,0,'O');
        game.makeMove(2,1,'X');
        game.makeMove(2,2,'X');
        //Then
        assertTrue(game.isBoardFull());
        assertFalse(game.checkWin('X'));
        assertFalse(game.checkWin('O'));
    }

    @Test
    void testInvalidMoveThrowsException() {
        //Given
        GameLogic game = new GameLogic();
        //When
        game.makeMove(1,1,'X');
        //Then
        assertThrows(IllegalArgumentException.class, () -> game.makeMove(1,1,'O'));
    }
}
