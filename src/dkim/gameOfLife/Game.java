package dkim.gameOfLife;

import edu.princeton.cs.introcs.StdDraw;

import java.util.*;

public class Game {

//    private final int FIELD_WIDTH = 60; // ширина игрового поля
//    private final int FIELD_HEIGHT = 35; // высота игрового поля
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
        StdDraw.setXscale(-20, 1199);
        StdDraw.setYscale(-20, 699);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.enableDoubleBuffering(); //буферизация с offscreen для анимации

        //генерируем игровое поле
        generate();
        show();

//        int i = 1; //номер поколения
//        while (true) {
//            try {
//                Thread.sleep(FRAME_DELAY);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            i++;
//            long startLifeCycle = System.currentTimeMillis();
//            life();
//            long endLifeCycle = System.currentTimeMillis();
//
//            show();
//
//        }

        life();
        nextGen.forEach(System.out::println);
    }

    /**
     * Генерация игрового поля
     * Для того, чтобы сгенерировать Глайдер, раскомментировать блок "Glider"
     * Для того, чтобы сгенерировать Small Exploder, раскомменировать блок "Small Exploder"
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
                new Cell(10,10),
                new Cell(10,11),
                new Cell(11,11),
                new Cell(11,10),
        };

        Cell[] line = {
                new Cell(10, 10),
                new Cell(11, 10),
                new Cell(12, 10),
        };
        currentGen.addAll(Arrays.asList(line));

    }

    /**
     * Метод для подсчета количества живых соседей клетки
     *
     * @param cell - клетка, для которой считаются соседи
     * @return количество "живых" соседей
     */
    int countNeighbors(Cell cell) {
        int count = 0;

        dead:
        //обходим поле 3*3 (все соседи)
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {

                if ((i == 0) && (j == 0)) {
                    continue;
                }

                int newX = cell.x + i;
                int newY = cell.y + j;
                Cell neighbor = new Cell(newX, newY);

                count += currentGen.contains(neighbor) ? 1 : 0;

                if (count > 3) {
                    break dead;
                }
            }
        }

        return count;
    }


    /**
     * Смена поколения по правилам Conway's dkim.gameOfLife.Game of Life
     */
    void life() {
        Set<Cell> potentialCells = new HashSet<>();
        for (Cell cell : currentGen) {
            int aliveNeighbors = countNeighbors(cell);
            if ((aliveNeighbors <= 3) && (aliveNeighbors >= 2)) {
                nextGen.add(cell);
            }
        }

    }

    /**
     * Отрисовка следующего поколения
     */
    void show() {

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