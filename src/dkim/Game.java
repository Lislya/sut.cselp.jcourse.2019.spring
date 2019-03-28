package dkim;

import edu.princeton.cs.introcs.StdDraw;

import java.util.*;

public class Game {

    private final int FIELD_SIZE = 25; // размер игрового поля
    private final int FRAME_DELAY = 200; // задержка отрисовки
    private boolean[][] currentGen = new boolean[FIELD_SIZE][FIELD_SIZE];
    private boolean[][] nextGen = new boolean[FIELD_SIZE][FIELD_SIZE];
    private Random random = new Random();

    public static void main(String[] args) {
        new Game().run();
    }

    void run() {

        //настраиваем Canvas
        StdDraw.setCanvasSize(1200, 700);
        StdDraw.setXscale(-300, 1199);
        StdDraw.setYscale(-200, 699);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.enableDoubleBuffering(); //буферизация с offscreen для анимации

        //генерируем игровое поле
        generate();
        show();

        int i = 1; //номер поколения
        while (true) {
            try {
                Thread.sleep(FRAME_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            long startLifeCycle = System.currentTimeMillis();
            life();
            long endLifeCycle = System.currentTimeMillis();

            StdDraw.textLeft(-100, 350, String.valueOf(i));
            StdDraw.textLeft(-100, 400, endLifeCycle - startLifeCycle + " ms");

            show();
            if (StdDraw.isMousePressed()) {
                StdDraw.pause(3000);
            }
        }
    }

    /**
     * Генерация игрового поля
     * Для того, чтобы сгенерировать Глайдер, раскомментировать блок "Glider"
     * Для того, чтобы сгенерировать Small Exploder, раскомменировать блок "Small Exploder"
     */
    void generate() {
        //Случайная генерация
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                currentGen[i][j] = random.nextBoolean();
            }
        }

        //Разкомментировать, чтобы сгенерировать конкретное поле
        /*
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (boolean[] row : currentGen) {
                Arrays.fill(row, false);
            }
        }
        */

        //Glider
        /*
        currentGen[3][3] = true;
        currentGen[4][3] = true;
        currentGen[5][3] = true;
        currentGen[5][2] = true;
        currentGen[4][1] = true;
        */

        //Small Exploder
        /*
        currentGen[13][13] = true;
        currentGen[14][13] = true;
        currentGen[15][13] = true;
        currentGen[14][14] = true;
        currentGen[13][12] = true;
        currentGen[15][12] = true;
        currentGen[14][11] = true;
        */
    }

    /**
     * Метод для подсчета количества живых соседей клетки
     *
     * @param x - координата X текущей клетки
     * @param y - координата Y текущей клетки
     * @return количество "живых" соседей
     */
    int countNeighbors(int x, int y) {
        int count = 0;
        //обходим поле 3*3 (все соседи + сама клетка)
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int newX = x + i;
                int newY = y + j;
                newX = (newX < 0) ? FIELD_SIZE - 1 : newX; //если координата выходит за
                newY = (newY < 0) ? FIELD_SIZE - 1 : newY; //границы поля, то новая координата
                newX = (newX > FIELD_SIZE - 1) ? 0 : newX; //появляется с противоположной стороны поля
                newY = (newY > FIELD_SIZE - 1) ? 0 : newY;

                count += currentGen[newX][newY] ? 1 : 0;
            }
        }
        if (currentGen[x][y]) {
            /* если текущая клетка живая, то вычитаем 1 из общего числа живых соседей */
            count--;
        }
        return count;
    }

    /**
     * Смена поколения по правилам Conway's dkim.Game of Life
     */
    void life() {
        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {

                int neighbors = countNeighbors(x, y);   //живые соседи текущей клетки
                nextGen[x][y] = currentGen[x][y];       //копируем клетку из текущего поколения в следующее

                if (neighbors == 3) {                   //если количество живых соседей равно 3,
                    nextGen[x][y] = true;               //то появляется новая клетка || выживает предыдущая
                } else {
                    nextGen[x][y] = nextGen[x][y];
                }

                if (neighbors < 2 || neighbors > 3) {   //в противном случае клетка умирает
                    nextGen[x][y] = false;
                } else {
                    nextGen[x][y] = nextGen[x][y];
                }
            }
        }

        //переносим новое поколение в текущее
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.arraycopy(nextGen[i], 0, currentGen[i], 0, FIELD_SIZE);
        }
    }

    /**
     * Отрисовка следующего поколения
     */
    void show() {

        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                //отрисовка сетки
                StdDraw.setPenRadius(0.0001);
                StdDraw.square(x * 20, y * 20, 10);

                //отрисовка точки
                if (currentGen[x][y]) {
                    StdDraw.setPenRadius(0.02);
                    StdDraw.point(x * 20, y * 20);
                }
            }
        }
        StdDraw.show();
        StdDraw.clear(); //очистка offscreen
    }
}