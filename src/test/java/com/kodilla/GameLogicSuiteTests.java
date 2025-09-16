package com.kodilla;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameLogicSuiteTests {

    @Test
    void testOWinsInRow() {
        GameLogic game = new GameLogic();
        game.makeMove(0,0,'O');
        game.makeMove(0,1,'O');
        game.makeMove(0,2,'O');
        assertTrue(game.checkWin('O'));
    }

    @Test
    void testOWinsInColumn() {
        GameLogic game = new GameLogic();
        game.makeMove(0,1,'O');
        game.makeMove(1,1,'O');
        game.makeMove(2,1,'O');
        assertTrue(game.checkWin('O'));
    }

    @Test
    void testOWinsInDiagonal() {
        GameLogic game = new GameLogic();
        game.makeMove(0,0,'O');
        game.makeMove(1,1,'O');
        game.makeMove(2,2,'O');
        assertTrue(game.checkWin('O'));
    }

    @Test
    void testXWinsInRow() {
        GameLogic game = new GameLogic();
        game.makeMove(2,0,'X');
        game.makeMove(2,1,'X');
        game.makeMove(2,2,'X');
        assertTrue(game.checkWin('X'));
    }

    @Test
    void testXWinsInColumn() {
        GameLogic game = new GameLogic();
        game.makeMove(0,2,'X');
        game.makeMove(1,2,'X');
        game.makeMove(2,2,'X');
        assertTrue(game.checkWin('X'));
    }

    @Test
    void testXWinsInDiagonal() {
        GameLogic game = new GameLogic();
        game.makeMove(0,2,'X');
        game.makeMove(1,1,'X');
        game.makeMove(2,0,'X');
        assertTrue(game.checkWin('X'));
    }

    @Test
    void testDraw() {
        GameLogic game = new GameLogic();
        game.makeMove(0,0,'X');
        game.makeMove(0,1,'O');
        game.makeMove(0,2,'X');
        game.makeMove(1,0,'X');
        game.makeMove(1,1,'O');
        game.makeMove(1,2,'O');
        game.makeMove(2,0,'O');
        game.makeMove(2,1,'X');
        game.makeMove(2,2,'X');
        assertTrue(game.isBoardFull());
        assertFalse(game.checkWin('X'));
        assertFalse(game.checkWin('O'));
    }

    @Test
    void testInvalidMoveThrowsException() {
        GameLogic game = new GameLogic();
        game.makeMove(1,1,'X');
        assertThrows(IllegalArgumentException.class, () -> game.makeMove(1,1,'O'));
    }
}
