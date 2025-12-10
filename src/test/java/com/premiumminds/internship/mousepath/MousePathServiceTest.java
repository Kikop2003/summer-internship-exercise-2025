package com.premiumminds.internship.mousepath;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class MousePathServiceTest {

  /**
   * The corresponding implementations to test.
   *
   * If you want, you can make others :)
   *
   */
  public MousePathServiceTest() {
  };

  @Test
  void PersonNoChildrenTest() {
    final char[][] grid = this.convert(new String[] {
      "           ",
      "X---------X",
      "           ",
      "           "
      });
      ;
      assertTrue(new MousePathService().isValidGrid(grid));
  }
  @Test
  void easy() {
    final char[][] grid = this.convert(new String[] {
      "  ",
      "XX",
      "  ",
      });
      ;
      assertTrue(new MousePathService().isValidGrid(grid));
  }

  @Test
  void easyfalse() {
    final char[][] grid = this.convert(new String[] {
      "X  ",
      "|  ",
      "+-X",
      "|  "});
      ;
      assertFalse(new MousePathService().isValidGrid(grid));
  }

  @Test
  void breakNoFinishLab() {
    char[][] map = {
    {'X', '-', '-', '+', ' ', ' ', ' ', ' ', '+', '-', '-', '-', '+'},
    {' ', ' ', ' ', '|', '-', '-', '-', '-', '+', ' ', ' ', '+', '+'},
    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '+', 'X'}
};

      assertFalse(new MousePathService().isValidGrid(map));
  }
  @Test
  void loopPath() {
    final char[][] grid = this.convert(new String[] {
      "X-+ ",
      "  | ",
      "++++",
      "|+-+",
      "+--X"
    });
    assertTrue(new MousePathService().isValidGrid(grid));
  }
  @Test
  void Corner() {
    final char[][] grid = this.convert(new String[] {
      "           ",
      "X----+X",
      "           ",
      "           "
      });
      ;
      assertFalse(new MousePathService().isValidGrid(grid));
  }
  @Test
  void PersonOneChildTest() {
    final char[][] grid = this.convert(new String[] {
      " X  ",
      " |  ",
      " +  ",
      " X  "
      });
      assertFalse(new MousePathService().isValidGrid(grid));
  }

  @Test
  void DownUp() {
    final char[][] grid = this.convert(new String[] {
      " X X",
      " | |",
      " | |",
      " +-+"
      });
      assertTrue(new MousePathService().isValidGrid(grid));
  }
  @Test
  void ConnectedDownUp() {
    final char[][] grid = this.convert(new String[] {
      " X-X",
      " | |",
      " | |",
      " +-+"
      });
      assertFalse(new MousePathService().isValidGrid(grid));
  }
  @Test
  void ZigZag() {
    final char[][] grid = this.convert(new String[] {
      "XX ",
      "|| ",
      "|++",
      "+-+"
      });
      assertTrue(new MousePathService().isValidGrid(grid));
  }
  @Test
  void ZigZagW() {
    final char[][] grid = this.convert(new String[] {
      "XX ",
      "|| ",
      "|++",
      "+-+",
      "+-+"
      });
      assertFalse(new MousePathService().isValidGrid(grid));
  }

  private char[][] convert(String[] input) {
    if (input == null) {
      return new char[0][0];
    }
    int rows = input.length;
    int cols = 0;
    for (String s : input) {
      if (s != null) {
        cols = Math.max(cols, s.length());
      }
    }

    char[][] result = new char[rows][cols];

    for (int i = 0; i < rows; i++) {
      if (input[i] != null) {
        for (int j = 0; j < input[i].length(); j++) {
          result[i][j] = input[i].charAt(j);
        }
      }
    }

    return result;
  }
}
