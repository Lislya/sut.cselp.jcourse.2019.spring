package dkim.gameOfLife;

import edu.princeton.cs.introcs.StdDraw;

import java.util.*;

public class Game {

    private final int FRAME_DELAY = 200; // задержка отрисовки
    private Set<Cell> currentGen = new HashSet<>();
    private Set<Cell> nextGen = new HashSet<>();
    private Random random = new Random();

    public static void main(String[] args) {
        new Game().run();
    }

    void run() {

        //настраиваем Canvas
        StdDraw.setCanvasSize(1300, 700);
        StdDraw.setXscale(0, 1299);
        StdDraw.setYscale(0, 699);
        StdDraw.setPenRadius(0.1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.enableDoubleBuffering(); //буферизация с offscreen для анимации

        //генерируем игровое поле
        generate();
        show();

        int i = 1; //номер поколения
        while (true) {
//            try {
//                Thread.sleep(FRAME_DELAY);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            i++;
            long startLifeCycle = System.currentTimeMillis();
            life();
            long endLifeCycle = System.currentTimeMillis();

            StdDraw.textLeft(100,300, endLifeCycle - startLifeCycle + " ms");
            StdDraw.textLeft(100,350, i + " generation");
            show();
//
        }
    }

    /**
     * Генерация игрового поля
     */
    void generate() {
        /* Генерируем small exploder */
        Cell[] smallExploder = {
                new Cell(13, 13),
                new Cell(14, 13),
                new Cell(15, 13),
                new Cell(14, 14),
                new Cell(13, 12),
                new Cell(15, 12),
                new Cell(14, 11)
        };

        Cell[] block = {
                new Cell(10, 10),
                new Cell(10, 11),
                new Cell(11, 11),
                new Cell(11, 10),
        };

        Cell[] line = {
                new Cell(299, 300),
                new Cell(300, 300),
                new Cell(301, 300),
        };

        Cell[] glider = {
                new Cell(299,300),
                new Cell(300,300),
                new Cell(301,300),
                new Cell(301,301),
                new Cell(300,302)
        };
        currentGen.addAll(Arrays.asList(glider));

    }

    /**
     * Метод для подсчета количества живых соседей клетки
     *
     * @param neighbors - соседи клетки
     * @return количество "живых" соседей
     */
    int countNeighbors(Set<Cell> neighbors) {
        int count = 0;

        for (Cell cell : neighbors) {
            count += currentGen.contains(cell) ? 1 : 0;
            if (count > 3) {
                return count;
            }
        }

        return count;
    }

    /**
     * Метод для получения соседей клетки
     * @param cell - текущая клетка
     * @return множество соседей
     */
    Set<Cell> getNeighbors(Cell cell) {
        Set<Cell> neighbors = new HashSet<>();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i == 0) && (j == 0)) {
                    continue;
                }

                int nX = cell.x + i;
                int nY = cell.y + j;
                neighbors.add(new Cell(nX, nY));
            }
        }
        return neighbors;
    }

    /**
     * Смена поколения по правилам Conway's Game of Life
     */
    void life() {
        Set<Cell> potentials = new HashSet<>();

        for (Cell cell : currentGen) {
            Set<Cell> neighbors = getNeighbors(cell);
            int aliveNeighbors = countNeighbors(neighbors);

            if ((aliveNeighbors <= 3) && (aliveNeighbors >= 2)) {
                nextGen.add(cell);
            }

            for (Cell neighbor : neighbors) {
                if (!currentGen.contains(neighbor)) {
                    potentials.add(neighbor);
                }
            }

            for (Cell potential : potentials) {
                aliveNeighbors = countNeighbors(getNeighbors(potential));
                if (aliveNeighbors == 3) {
                    nextGen.add(potential);
                }
            }
        }

        currentGen.clear();
        currentGen.addAll(nextGen);
        nextGen.clear();

    }

    /**
     * Отрисовка следующего поколения
     */
    void show() {
        for (Cell cell : currentGen) {
            StdDraw.filledCircle(cell.x, cell.y,1);
        }

        StdDraw.show();
        StdDraw.clear(); //очистка offscreen
    }

    /* Класс живых клеток*/
    class Cell {
        private int x;
        private int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof Cell)) {
                return false;
            }

            Cell cell = (Cell) obj;
            return (this.x == cell.x) && (this.y == cell.y);
        }

        @Override
        public int hashCode() {
            return y * 31 + x;
        }

        @Override
        public String toString() {
            return "[x:" + x + " y:" + y + "]";
        }
    }
}