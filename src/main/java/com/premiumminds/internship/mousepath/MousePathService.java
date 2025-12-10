package com.premiumminds.internship.mousepath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class MousePathService implements IMousePathService  {

  private static final char CROSS = '+';
  private static final char H_TUNNEL = '-';
  private static final char V_TUNNEL = '|';
  private static final char START_END = 'X';

  /**
   * Method to validate the grid for a hamilton path
   *
   * @param grid to be evaluated
   * @return boolean which is the result of the validation 
   */
  @Override
  public boolean isValidGrid(char[][] grid){
    Coordinate start = null;
    int size = 0;
    int xs = 0;
    
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        int exits = 0;
        if (grid[i][j] == CROSS || grid[i][j] == H_TUNNEL || grid[i][j] == V_TUNNEL || grid[i][j] == START_END) {
          if (i + 1 < grid.length && grid[i + 1][j] == V_TUNNEL) {
              exits++;
          }
          if (i - 1 >= 0 && grid[i - 1][j] == V_TUNNEL) {
              exits++;
          }
          if (j + 1 < grid[0].length && grid[i][j + 1] == H_TUNNEL) {
              exits++;
          }
          if (j - 1 >= 0 && grid[i][j - 1] == H_TUNNEL) {
              exits++;
          }
          if (exits > 2) { // More than 2 entrys specific for this place make it impossible
            return false;
          }
          size++;
        }else if (grid[i][j] != ' '){ //illegal chars
          return false;
        }
        if (grid[i][j] == START_END) {
          xs++;
          start = new Coordinate(i, j);
        }
      }
    }
    if (xs != 2) { //illegal number of Xs
      return false;
    }
    grid[start.getX()][start.getY()] = 'x';
    Set<Coordinate> visited = new HashSet<>();
    List<Coordinate> path = new ArrayList<>();
    boolean ret;
    
    ret =  dfs(start, visited, path, grid, size);
    grid[start.getX()][start.getY()] = 'X';
    return ret;
  }

  /**
   * DFS method to see if there is an hamilton path in the given grid  
   * @param current the coordinate that is beeing visited in this iteration
   * @param visited a set where its saved all the coordinates already visited
   * @param path a list where its saved the path to get the current coordinate
   * @param grid the grid to be checked for 
   * @param size the number of all the coordinates that we have to visit to get to the objective
   * @return if there is an hamilton path in grid
   */
  private boolean dfs(Coordinate current, Set<Coordinate> visited, List<Coordinate> path, char[][] grid, int size) {
  
    visited.add(current);
    path.add(current);

    if (goalTest(current, grid)){
      visited.remove(current);
      path.remove(path.size() - 1);
      return visited.size() + 1 == size;
    }
    
    Coordinate prev = path.size() > 1 ? path.get(path.size() - 2) : null;
    Coordinate[] neighbors = expand(grid, prev, current, visited);
    for (Coordinate neighbor : neighbors) {
      if (dfs(neighbor, visited, path, grid, size)) {
        return true;
      }  
    }

    visited.remove(current);
    path.remove(path.size() - 1);
    return false;
  }
  /**
   * Function that tests if the current coordinate is the goal
   * @param current coordinate to be tested
   * @param grid map to see
   * @return if it is goal
   */
  public boolean goalTest(Coordinate current, char[][] grid) {
    return grid[current.getX()][current.getY()] == START_END;
  }

  /**
   * Function that gives all the possible coordinates where you can go from the current 
   * @param grid map to see
   * @param from coordinate where you came before the current
   * @param expanding coordinate that is beeing expanded
   * @param visited all the coordinates already visited
   * @return an array of all the possible coordinates to go to
   */
  public Coordinate[] expand(char[][] grid, Coordinate from, Coordinate expanding, Set<Coordinate> visited) {
    char expandingChar = grid[expanding.getX()][expanding.getY()];

    switch (expandingChar) {
        case 'x': {
          return getOrthogonalNeighbors(expanding, grid).toArray(new Coordinate[0]);
        }
        case CROSS: {
          List<Coordinate> ret = getOrthogonalNeighbors(expanding, grid);
          Coordinate opposite = Coordinate.add(Coordinate.subtract(expanding, from), expanding);
          
          ret.remove(opposite);
          ret.remove(from);
          ret.removeIf(visited::contains);

          return ret.toArray(new Coordinate[0]);
        }
        case H_TUNNEL: {
          return getTunnelNeighbors(expanding, from, V_TUNNEL, grid, visited);
        }
        case V_TUNNEL: {
          return getTunnelNeighbors(expanding, from, H_TUNNEL, grid, visited);
        }
        default:
          return new Coordinate[0];
    }
  }
  /**
   * Returns all the neighbors in the ortogonal directions if possible
   * @param coord coordinate to be checked for neighbours
   * @param grid map to see
   * @return an array with all the possible neighbours
   */
  private List<Coordinate> getOrthogonalNeighbors(Coordinate coord, char[][] grid) {
    List<Coordinate> neighbors = new ArrayList<>(4);
    int x = coord.getX();
    int y = coord.getY();

    if (x + 1 < grid.length && grid[x + 1][y] != ' ' && grid[x + 1][y] != H_TUNNEL) {
        neighbors.add(new Coordinate(x + 1, y));
    }
    if (x - 1 >= 0 && grid[x - 1][y] != ' ' && grid[x - 1][y] != H_TUNNEL) {
        neighbors.add(new Coordinate(x - 1, y));
    }
    if (y + 1 < grid[0].length && grid[x][y + 1] != ' ' && grid[x][y + 1] != V_TUNNEL) {
        neighbors.add(new Coordinate(x, y + 1));
    }
    if (y - 1 >= 0 && grid[x][y - 1] != ' ' && grid[x][y - 1] != V_TUNNEL) {
        neighbors.add(new Coordinate(x, y - 1));
    }
    return neighbors;
  }

  /**
   * Returns the oposing neighbour if possible
   * @param expanding coordinate to be checked for neighbours
   * @param from coordinate where you were before being in the current one
   * @param avoidChar the char that the neighbour cant be
   * @param grid the map to check
   * @param visited a set with all the visited coordinates
   * @return an array with all the possible neighbours
   */
  private Coordinate[] getTunnelNeighbors(Coordinate expanding, Coordinate from, char avoidChar, char[][] grid, Set<Coordinate> visited) {
  Coordinate ret = Coordinate.add(Coordinate.subtract(expanding, from), expanding);
  int i = ret.getX();
  int j = ret.getY();

  if (i < 0 || i >= grid.length ||
      j < 0 || j >= grid[0].length ||
      grid[i][j] == ' ' ||
      grid[i][j] == avoidChar ||
      visited.contains(ret)) {
    return new Coordinate[0];
  }
  return new Coordinate[]{ret};
  }
}

class Coordinate {
  private int x;
  private int y;

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }


  public static Coordinate subtract(Coordinate a, Coordinate b) {
    return new Coordinate(a.getX() - b.getX(), a.getY() - b.getY());
  }

  public static Coordinate add(Coordinate a, Coordinate b) {
    return new Coordinate(a.getX() + b.getX(), a.getY() + b.getY());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true; // same object
    if (obj == null || getClass() != obj.getClass()) return false;

    Coordinate other = (Coordinate) obj;
    return this.x == other.x && this.y == other.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}

