package models;


import javax.swing.*;
import java.util.*;

public class Block extends JButton {

    //X and Y location of a block
    public int x;
    public int y;


    public int width = 80;
    public int height = 80;

    //محتوای این بلاک
    public List<enumEntity> lstContent = new ArrayList<>();

    public Stuff stuff;// جنس بلاک (انبار یا زمین)
}
