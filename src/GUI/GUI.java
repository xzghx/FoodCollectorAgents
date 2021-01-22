package GUI;

import models.enumEntity;
import models.Block;
import models.Stuff;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    public Block blocks[][] = new Block[20][20];
    private int totalHerbsCollected = 0;
    private List<String> collectedHerbsLocation = new ArrayList<>();

    public GUI() {
        JFrame frame = new JFrame("THis is a Frame");

        //init 20 * 20 UI table . position 10,10 is Stock
        _createBoard(20, 20, frame);

        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Create 20 * 20 table

        //Adding Components to the frame.
//        frame.getContentPane().add(BorderLayout.SOUTH, panel);
//        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setLayout(new GridLayout(20, 20));
        frame.pack();
        frame.setVisible(true);


    }

    public static void main(String[] args) {
    }


    private void _createBoard(int row, int column, JFrame frame) {

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < column; c++) {
                blocks[r][c] = new Block();
                blocks[r][c].x = r;
                blocks[r][c].y = c;
                blocks[r][c].setBackground(Color.pink);
                blocks[r][c].setText(String.format("%d,%d", r, c));
                blocks[r][c].setFont(new Font("Tahoma", Font.PLAIN, 12));
                blocks[r][c].lstContent.add(enumEntity.Empty);
                blocks[r][c].stuff = Stuff.Ground;
                if (r == 10 & c == 10) {
                    blocks[r][c].stuff = Stuff.Stock;
                    blocks[r][c].setBackground(Color.darkGray);
                }
                frame.add(blocks[r][c]);
            }
        }


    }

    public void updateUi(int row, int column, enumEntity entityToLocateInUi) {

        switch (entityToLocateInUi) {
            case Herb:
                blocks[row][column].setBackground(Color.green);
                blocks[row][column].lstContent.add(enumEntity.Herb);
                break;
            case DetectedHerb:
                blocks[row][column].setBackground(Color.CYAN);
                blocks[row][column].lstContent.remove(enumEntity.Herb);
                blocks[row][column].lstContent.add(enumEntity.DetectedHerb);
                break;
            case SearcherAgent:
                blocks[row][column].setBackground(Color.blue);
                break;

            case GoingToHerb:
                blocks[row][column].setBackground(Color.magenta);
                break;
            case GoingToStock:
                blocks[row][column].setBackground(Color.LIGHT_GRAY);
                break;
            case CollectorAgent:
                blocks[row][column].setBackground(Color.red);
 /*               blocks[row][column].lstContent.remove(enumEntity.Herb);
                blocks[row][column].lstContent.add(enumEntity.DetectedHerb);
                blocks[row][column].setBackground(Color.CYAN);*/
                break;
            case Empty:
                blocks[row][column].lstContent.remove(enumEntity.Herb);
                blocks[row][column].setBackground(Color.pink);
                break;
            case Content:
                if (blocks[row][column].lstContent.contains(enumEntity.Herb))
                    blocks[row][column].setBackground(Color.yellow);//detected herb
                else if (blocks[row][column].stuff == Stuff.Stock)
                    blocks[row][column].setBackground(Color.darkGray);
                else if (blocks[row][column].lstContent.contains(enumEntity.DetectedHerb))
                    blocks[row][column].setBackground(Color.CYAN);
                else if (blocks[row][column].lstContent.contains(enumEntity.Empty))
                    blocks[row][column].setBackground(Color.pink);
                else
                    blocks[row][column].setBackground(Color.pink);

                break;
        }
    }

    /**
     * return best location for searcher agent how wants to achieve to herbs
     **/
    public int[] exploreAround(int currentX, int currentY, boolean[][] visited) {
        int row = currentX;
        int col = currentY;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r < 20 && c < 20 && r >= 0 && c >= 0)
                    if (blocks[r][c].lstContent.contains(enumEntity.Herb) && !visited[r][c]) {
                        int[] bestLocation = new int[2];
                        bestLocation[0] = r;
                        bestLocation[1] = c;
                        return bestLocation;
                    }
            }
        }

        return null;
    }


    public void updateTotalHerbCollected(int[] collectedHerbXY) {
        totalHerbsCollected++;
        collectedHerbsLocation.add(String.format("%d,%d", collectedHerbXY[0], collectedHerbXY[1]));

        updateUi(collectedHerbXY[0], collectedHerbXY[1], enumEntity.DetectedHerb);
        System.out.println(String.format("total Herbs Collected so far: %d", totalHerbsCollected));

        System.out.println("____________________________________________");
        System.out.println("HERB locations collected:");
        for (String location :
                collectedHerbsLocation) {
            System.out.println(location);
        }
        System.out.println("____________________________________________");

    }
}



/*
* public static void main(String args[]) {
        JFrame frame = new JFrame("THis is a Frame");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenuItem item1 = new JMenuItem("Start");
        JMenuItem item2 = new JMenuItem("Exit");
        JMenu menu = new JMenu("MORE");
        menu.add(item1);
        menu.add(item2);
        menuBar.add(menu);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Total food collected");
        JTextField tf = new JTextField(10); // accepts upto 10 characters
        JButton send = new JButton("Send");
        JButton reset = new JButton("Reset");
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        panel.add(reset);

        // Text Area at the Center
        JTextArea ta = new JTextArea();

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.setVisible(true);
    }
* */
